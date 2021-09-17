package com.example.tulipsante.pulse.oximeter.device.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import com.example.tulipsante.pulse.oximeter.device.bluetooth.bean.BleDevice;
import com.example.tulipsante.pulse.oximeter.device.bluetooth.bean.EBleState;
import com.example.tulipsante.pulse.oximeter.device.bluetooth.bean.ESwitchState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BleClient implements IBleClient, BluetoothAdapter.LeScanCallback {

    private static final String TAG = "BleClient";

    private Context mContext;
    private BluetoothAdapter mAdapter;
    private EBleState mConnectState;
    private boolean mScanning;
    private BleConfig mBleConfig;
    private BleConfig mBleDefaultConfig;

    private volatile OnSwitchListener mSwitchListener;
    private volatile OnScanListener mScanListener;
    private volatile OnEventListener mEventListener;
    private volatile OnRecvCallback mRecvCallback;
    private List<BleDevice> mScanList;
    private BluetoothLeService mBluetoothService;

    private static BleClient mSingleton;

    public static BleClient getSingleton(Context ctx) {
        if (mSingleton == null) {
            synchronized (BleClient.class) {
                if (mSingleton == null) {
                    mSingleton = new BleClient(ctx.getApplicationContext());
                }
            }
        }

        return mSingleton;
    }

    private BleClient(Context ctx) {
        mContext = ctx;
        mAdapter = getAdapter();
        mScanning = false;
        mScanList = new ArrayList<>();

        // 注册蓝牙状态广播接收器
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        ctx.registerReceiver(new BlueToothStateReceiver(), filter);

        // 给连接状态设置默认值
        mConnectState = EBleState.disconnected;

        // 绑定蓝牙操作service
        mBluetoothService = BluetoothLeService.getSingleton();
        if (mBluetoothService == null) {
            Intent intent = new Intent(ctx, BluetoothLeService.class);
            ctx.bindService(intent, new BlueToothServiceConnection(), Context.BIND_AUTO_CREATE);
            ctx.startService(intent);
        } else {
            mConnectState = mBluetoothService.isConnected() ? EBleState.connected : EBleState.disconnected;
            mBluetoothService.setHandler(mBleHandler);
        }
    }

    /**
     * 扫描设备回调
     * 接口调用没有在主线程中；
     * 返回的设备会重复，需要自己做去重（设备信息是一致的，但是设备信号强度发送变化）；
     *
     * @param device     设备
     * @param rssi       信号
     * @param scanRecord scanRecord
     */
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        BleDevice dev = new BleDevice(device.getName(), device.getAddress(), rssi);
        String name = dev.getName();
        String filterName = getConfig().getBroadcastName();
        // 如果没有指定广播名，则可以显示广播名为空的设备
        if (TextUtils.isEmpty(filterName)) {
            if (TextUtils.isEmpty(name)) {
                name = "未知设备";
            }
        }

        // 广播名不为空 & 包含指定的字符
        if (!TextUtils.isEmpty(name) && name.contains(filterName)) {
            // 广播列表去重
            if (!mScanList.contains(dev)) {
                mScanList.add(dev);
                Collections.sort(mScanList);

                mBleHandler.post(() -> {
                    if (mScanListener != null && mScanning) {
                        mScanListener.onScan(mScanList);
                    }
                });
            }
        }
    }

    public BleConfig getConfig() {
        if (mBleConfig != null) {
            return mBleConfig;
        } else {
            if (mBleDefaultConfig == null) {
                mBleDefaultConfig = new BleConfig();
            }
            return mBleDefaultConfig;
        }
    }

    @Override
    public IBleClient setBleConfig(BleConfig config) {
        mBleConfig = config;
        if (mBluetoothService != null) {
            mBluetoothService.setConfig(mBleConfig);
        }
        return this;
    }

    @Override
    public IBleClient setBleSwitchListener(OnSwitchListener switchListener) {
        mSwitchListener = switchListener;
        return this;
    }

    @Override
    public IBleClient setBleScanListener(OnScanListener scanListener) {
        mScanListener = scanListener;
        return this;
    }

    @Override
    public IBleClient setBleEventListener(OnEventListener eventListener) {
        mEventListener = eventListener;
        return this;
    }

    @Override
    public IBleClient setBleRecvCallback(OnRecvCallback recvCallback) {
        mRecvCallback = recvCallback;
        return this;
    }

    @Override
    public boolean isSupportBle() {
        if (mAdapter == null) {
            Log.e(TAG, "该设备不支持BLE");
        }

        return mAdapter != null;
    }

    @Override
    public boolean isOpenBle() {
        if (isSupportBle() && mAdapter.isEnabled()) {
            return true;
        }
        return false;
    }

    @Override
    public void openBle() {
        if (!isSupportBle()) {
            return;
        }

        // 方式一：设置页面打开
        // Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mContext.startActivity(intent);

        // 方式二：直接打开
        if (mAdapter.isEnabled()) {
            Log.e(TAG, "蓝牙已打开");
        } else {
            boolean res = mAdapter.enable();
            if (res == true) {
                Log.e(TAG, "申请打开蓝牙...");
            } else {
                Log.e(TAG, "申请打开蓝牙失败");
            }
        }
    }

    @Override
    public boolean scanDevice() {
        if (!isSupportBle()) {
            return false;
        }

        if (mScanning) {
            Log.e(TAG, "已经在扫描...");
            return false;
        } else {
            Log.e(TAG, "开始扫描...");
        }

        mBleHandler.postDelayed(mScanRunnable, getConfig().getScanTime());
        mScanning = true;
        return mAdapter.startLeScan(this);
    }

    @Override
    public void stopScanDevice() {
        if (!isSupportBle()) {
            return;
        }

        if (mScanning) {
            mScanning = false;
            mScanList.clear();
            mBleHandler.removeCallbacks(mScanRunnable);
            if (mScanListener != null) {
                Log.e(TAG, "主动停止扫描...");
                mScanListener.onEnd();
            }
            mAdapter.stopLeScan(this);
        }
    }

    @Override
    public EBleState getConnectState() {
        if (!isSupportBle()) {
            mConnectState = EBleState.unknown;
        }

        return mConnectState;
    }

    @Override
    public boolean connectDevice(String mac) {
        if (!isSupportBle()) {
            return false;
        }

        return mBluetoothService != null && mBluetoothService.connect(mac);
    }

    @Override
    public void disconnectDevice() {
        if (!isSupportBle()) {
            return;
        }
        if (mBluetoothService != null) {
            if (mBluetoothService.isConnected()) {
                Log.e(TAG, "is connected, disconnecting");
                mBluetoothService.disconnect();
            } else {
                Log.e(TAG, "not connected, close");
                mBluetoothService.close();
            }
        } else {
            Log.e(TAG, "disconnect error, mBluetoothService is null");
        }
    }

    @Override
    public void close() {
        if (!isSupportBle()) {
            return;
        }
        if (mBluetoothService != null) {
            mBluetoothService.close();
        }
    }

    @Override
    public boolean sendData(byte[] data) {
        if (!isSupportBle()) {
            return false;
        }

        return mBluetoothService != null && mBluetoothService.writeCharacteristic(data);
    }

    private BluetoothAdapter getAdapter() {
        // 检查当前手机是否支持ble 蓝牙
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.e(TAG, "该设备不支持BLE Feature");
            return null;
        }

        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        BluetoothManager mgr = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter adapter = mgr != null ? mgr.getAdapter() : null;
        if (adapter == null) {
            Log.e(TAG, "该设备不支持蓝牙");
            return null;
        }

        return adapter;
    }

    private void setSwitchEvent(ESwitchState event) {
        if (mSwitchListener != null) {
            mSwitchListener.onSwitch(event);
        }
    }

    private void setBleEvent(EBleState event) {
        if (mEventListener != null) {
            mEventListener.onEvent(event);
        }
    }

    private class BlueToothStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action == null) return;
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.e(TAG, "手机蓝牙正在打开");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Log.e(TAG, "手机蓝牙已打开");
                            setSwitchEvent(ESwitchState.opened);
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Log.e(TAG, "手机蓝牙正在关闭");
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            Log.e(TAG, "手机蓝牙已关闭");
                            setSwitchEvent(ESwitchState.closed);
                            break;
                    }
                    break;
            }

        }
    }

    private class BlueToothServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothService = ((BluetoothLeService.LocalBinder) service).getService();
            mBluetoothService.setHandler(mBleHandler);
            if (mBleConfig != null) {
                mBluetoothService.setConfig(mBleConfig);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            mBluetoothService = null;
        }
    }

    private Runnable mScanRunnable = new Runnable() {
        @Override
        public void run() {
            if (mScanning) {
                mScanning = false;
                mScanList.clear();
                if (mScanListener != null) {
                    Log.e(TAG, "扫描超时...");
                    mScanListener.onEnd();
                }
                mAdapter.stopLeScan(BleClient.this);
            }
        }
    };

    public static final int MSG_RECEIVE = 100;
    private Handler mBleHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;

            if (what == EBleState.connecting.getValue()) {
                mConnectState = EBleState.connecting;
                setBleEvent(EBleState.connecting);
            } else if (what == EBleState.connected.getValue()) {
                mConnectState = EBleState.connected;
                setBleEvent(EBleState.connected);
            } else if (what == EBleState.disconnecting.getValue()) {
                mConnectState = EBleState.disconnecting;
                setBleEvent(EBleState.disconnecting);
            } else if (what == EBleState.disconnected.getValue()) {
                mConnectState = EBleState.disconnected;
                setBleEvent(EBleState.disconnected);
            } else if (what == 100) {
                if (mRecvCallback != null) {
                    byte[] buffer = (byte[]) msg.obj;
                    if (buffer != null) {
                        mRecvCallback.onRecv(buffer, buffer.length);
                    }
                }
            }
        }
    };
}
