package com.example.tulipsante.pulse.oximeter.device.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


import com.example.tulipsante.pulse.oximeter.device.bluetooth.bean.EBleState;

import java.util.List;
import java.util.UUID;

public class BluetoothLeService extends Service {
    private final static String TAG = "BluetoothLeService";
    private final static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    BluetoothGattCharacteristic mWrite;
    private BleConfig mConfig;
    private BleConfig mDefaultConfig;
    private Handler mHandler;
    private boolean isConnected = false;
    private static BluetoothLeService mBluetoothLeService;

    public static BluetoothLeService getSingleton() {
        return mBluetoothLeService;
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public void setConfig(BleConfig config) {
        mConfig = config;
    }

    public BleConfig getConfig() {
        if (mConfig != null) {
            return mConfig;
        } else {
            if (mDefaultConfig == null) {
                mDefaultConfig = new BleConfig();
            }
            return mDefaultConfig;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        mBluetoothLeService = this;
        initialize();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        mBluetoothLeService = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        //连接状态改变
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            // 129、133蓝牙常见的连接错误
            if (newState == BluetoothProfile.STATE_CONNECTED && status != 133 && status != 129) {
                Log.e(TAG, "Connected to GATT server.");
                Log.e(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // 连接断开，释放资源
                Log.e(TAG, "连接断开，释放资源");
                close();
                isConnected = false;
                sendMessage(EBleState.disconnected.getValue());
            } else {
                // 连接异常，断开连接，释放资源
                Log.e(TAG, "连接异常，断开连接，释放资源");
                disconnect();
                close();
                isConnected = false;
                sendMessage(EBleState.disconnected.getValue());
            }
        }

        private BluetoothGattService getServer(BluetoothGatt gatt, String uuid) {
            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                if (service.getUuid().toString().toLowerCase().contains(uuid)) {
                    return service;
                }
            }
            return null;
        }

        private BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service, String uuid) {
            List<BluetoothGattCharacteristic> charas = service.getCharacteristics();
            for (BluetoothGattCharacteristic chara : charas) {
                if (chara.getUuid().toString().toLowerCase().contains(uuid)) {
                    return chara;
                }
            }
            return null;
        }

        //发现服务
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService service = getServer(gatt, getConfig().getServerUUID());
                if (service != null) {
                    BluetoothGattCharacteristic notify = getCharacteristic(service, getConfig().getNotifyCharacteristicUUID());
                    BluetoothGattCharacteristic write = getCharacteristic(service, getConfig().getWriteCharacteristicUUID());
                    if (write != null && notify != null) {
                        mWrite = write;
                        setCharacteristicNotification(notify, true);

                        Log.e(TAG, "onServicesDiscovered success");
                        isConnected = true;
                        sendMessage(EBleState.connected.getValue());
                    } else {
                        // 发现指定特征值失败，断开连接，释放资源
                        Log.e(TAG, "发现指定特征值失败，断开连接，释放资源");
                        disconnect();
                        close();
                        isConnected = false;
                        sendMessage(EBleState.disconnected.getValue());
                    }
                } else {
                    // 发现指定服务失败，断开连接，释放资源
                    Log.e(TAG, "发现指定服务失败，断开连接，释放资源");
                    disconnect();
                    close();
                    isConnected = false;
                    sendMessage(EBleState.disconnected.getValue());
                }
            } else {
                // 发现服务失败，断开连接，释放资源
                Log.e(TAG, "发现服务失败，断开连接，释放资源。status = " + status);
                disconnect();
                close();
                isConnected = false;
                sendMessage(EBleState.disconnected.getValue());
            }
        }

        //被读
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

        }

        //特性改变
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            sendMessage(BleClient.MSG_RECEIVE, characteristic.getValue());
            Log.e(TAG, "onCharacteristicChanged recv ...");
        }

        //特性书写
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onCharacteristicWrite success");

            } else {
                Log.e(TAG, "onCharacteristicWrite error");
            }
        }
    };

    /**
     * 判断设备是否连接
     *
     * @return
     */
    public boolean isConnected() {
        if (mBluetoothGatt == null) {
            return false;
        }

        return isConnected;
    }

    /**
     * 初始化本地蓝牙适配器
     *
     * @return 如果初始化成功，返回true
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * 连接远程GATTserver
     *
     * @param address mac地址
     * @return 如果初始化成功，返回true。
     * 回调触发函数BluetoothGattCallback#onConnectionStateChange
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.e(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
/*
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            Log.e(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                sendMessage(IBleClient.eBleEvent.connecting.getValue());
                return true;
            } else {
                return false;
            }
        }
*/

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.e(TAG, "Device not found.  Unable to connect.");
            return false;
        }

        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.e(TAG, "Trying to create a new connection.");
        Log.e(TAG, "BluetoothGatt is " + (mBluetoothGatt == null ? "null" : "not null"));
        mBluetoothDeviceAddress = address;
        sendMessage(EBleState.connecting.getValue());
        return true;
    }

    /**
     * 断开连接远程GATTserver
     * 回调触发函数BluetoothGattCallback#onConnectionStateChangea
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        isConnected = false;
        sendMessage(EBleState.disconnecting.getValue());
    }

    /**
     * 结束连接ble设备后，释放资源
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }

        mBluetoothGatt.close();
        mBluetoothGatt = null;
        isConnected = false;
        sendMessage(EBleState.disconnected.getValue());
    }

    /**
     * 读取characteristic
     *
     * @param characteristic 回调触发函数BluetoothGattCallback
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * 发送characteristic
     *
     * @return 回调触发函数BluetoothGattCallback#onCharacteristicWrite
     */
    public boolean writeCharacteristic(byte[] value) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        if (mWrite == null) {
            Log.e(TAG, "writeCharacteristic is null");
            return false;
        }

        mWrite.setValue(value);
        return mBluetoothGatt.writeCharacteristic(mWrite);
    }

    /**
     * 开启或者关闭notification
     * 有两种模式writeDescriptor或setCharacteristicNotification
     *
     * @param characteristic 特征值
     * @param enabled        开启/关闭
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }

        if (characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG)) != null) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);

        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

    }

    private boolean sendMessage(int what) {
        if (mHandler == null) {
            Log.e(TAG, "sendMessage handle is null");
            return false;
        }

        Message message = mHandler.obtainMessage();
        message.what = what;
        mHandler.sendMessage(message);
        return true;
    }

    private boolean sendMessage(int what, byte[] data) {
        if (mHandler == null) {
            Log.e(TAG, "sendMessage handle is null");
            return false;
        }

        Message message = mHandler.obtainMessage();
        message.what = what;
        message.obj = data;
        mHandler.sendMessage(message);
        return true;
    }
}
