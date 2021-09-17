package com.example.tulipsante.pulse.oximeter.device.usb2serial;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.util.Log;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

// https://blog.csdn.net/tianruxishui/article/details/37905313 这个文章介绍USB比较好
public class CH34xUARTDriver {
    protected Semaphore mSemaphore = new Semaphore(1);
    private final BroadcastReceiver mBroadcastRecv = new BroadcastRecv(this);
    private UsbManager mUsbManager;
    private PendingIntent mPendingIntent;
    private UsbDevice mUsbDevice;
    private UsbInterface mUsbInterface;
    private UsbEndpoint mEndpointIn;
    private UsbEndpoint mEndpointOut;
    private UsbDeviceConnection mUsbDeviceConn;
    protected String mStringPermission;
    private Object mObjectReadSem = new Object();
    private Object mObjectWriteSem = new Object();
    private boolean mIsUsbTached = false;
    protected boolean mIsThreadRunning = false;
    private ReadThread mReadThread;
    protected Context mContext;
    protected final int RecvBuffSize = 655360;
    protected byte[] mRecvBuffer = new byte[RecvBuffSize];
    protected byte[] mTmpBuffer = new byte[1024];
    protected int mRecvBuffHead = 0;
    protected int mRecvBuffTail = 0;
    protected int mTmpBuffLen;
    protected int mRecvBuffCount = 0;
    protected ArrayList mListUsbDevice = new ArrayList();
    protected int mUsbDeviceNum;
    private int mMaxPacketSize;
    private int mWriteTimeout;
    private int mWriteCfgTimeout = 500;
    UsbRequest[] mUsbRequests = new UsbRequest[20];
    ByteBuffer[] mByteBuffers = new ByteBuffer[20];

    public CH34xUARTDriver(UsbManager usbManager, Context context, String str) {
        this.mUsbManager = usbManager;
        this.mContext = context;
        this.mStringPermission = str;
        this.mWriteTimeout = 10000;
        AddDeviceType("1a86:7523");
        AddDeviceType("1a86:5523");
        AddDeviceType("1a86:5512");
    }

    private int CH34xConfig(int request, int value, int index) {
        return this.mUsbDeviceConn.controlTransfer(64, request, value, index, null, 0, this.mWriteCfgTimeout);
    }

    private int CH34xConfig(int request, int value, int index, byte[] buffer, int length) {
        return this.mUsbDeviceConn.controlTransfer(192, request, value, index, buffer, length, this.mWriteCfgTimeout);
    }

    protected void InitUsbDevice(UsbDevice usbDevice) {
        if (usbDevice != null) {
            UsbInterface usbInterface = null;
            if (this.mUsbDeviceConn != null) {
                if (this.mUsbInterface != null) {
                    this.mUsbDeviceConn.releaseInterface(mUsbInterface);
                    this.mUsbInterface = null;
                }

                this.mUsbDeviceConn.close();
                this.mUsbDevice = null;
                this.mUsbInterface = null;
            }

            if (usbDevice == null) {
                usbInterface = null;
            } else {
                boolean bFind = false;
                for (int i = 0; i < usbDevice.getInterfaceCount(); i++) {
                    usbInterface = usbDevice.getInterface(i);
                    if (usbInterface.getInterfaceClass() == 255 && usbInterface.getInterfaceSubclass() == 1 && usbInterface.getInterfaceProtocol() == 2) {
                        bFind = true;
                        break;
                    }
                }
                if (!bFind) usbInterface = null;
            }

            if (usbDevice != null && usbInterface != null) {
                UsbDeviceConnection openDevice = this.mUsbManager.openDevice(usbDevice);
                if (openDevice != null && openDevice.claimInterface(usbInterface, true)) {
                    this.mUsbDevice = usbDevice;
                    this.mUsbDeviceConn = openDevice;
                    this.mUsbInterface = usbInterface;

                    int index = 0;
                    if (usbInterface != null) {
                        while (index < usbInterface.getEndpointCount()) {
                            UsbEndpoint endpoint = usbInterface.getEndpoint(index);
                            if (endpoint.getType() == 2 && endpoint.getMaxPacketSize() == 32) {
                                if (endpoint.getDirection() == UsbConstants.USB_DIR_IN) {
                                    mEndpointIn = endpoint;
                                } else {
                                    mEndpointOut = endpoint;
                                }
                                mMaxPacketSize = endpoint.getMaxPacketSize();
                            } else {
                                endpoint.getType();
                            }
                            index++;
                        }

                        index = 1;
                    }

                    if (index != 0 && !this.mIsThreadRunning) {
                        mIsThreadRunning = true;
                        mReadThread = new ReadThread(this, mEndpointIn, mUsbDeviceConn);
                        mReadThread.start();
                    }
                }
            }
        }
    }

    // 增加可用的VID-PID
    private void AddDeviceType(String str) {
        this.mListUsbDevice.add(str);
        this.mUsbDeviceNum = this.mListUsbDevice.size();
    }

    public void CloseDevice() {
        if (this.mIsThreadRunning) {
            this.mIsThreadRunning = false;
        }

        if (this.mUsbDeviceConn != null) {
            if (this.mUsbInterface != null) {
                this.mUsbDeviceConn.releaseInterface(this.mUsbInterface);
                this.mUsbInterface = null;
            }
            this.mUsbDeviceConn.close();
        }

        if (this.mUsbDevice != null) {
            this.mUsbDevice = null;
        }

        if (this.mUsbManager != null) {
            this.mUsbManager = null;
        }

        if (this.mIsUsbTached) {
            this.mContext.unregisterReceiver(this.mBroadcastRecv);  // 注销广播
            this.mIsUsbTached = false;
        }
    }

    public UsbDevice EnumerateDevice() {
        this.mUsbManager = (UsbManager) this.mContext.getSystemService(Context.USB_SERVICE);
        this.mPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(this.mStringPermission), 0);
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        if (deviceList.isEmpty()) {
          //  Toast.makeText(this.mContext, "No Device Or Device Not Match", Toast.LENGTH_LONG).show();
            return null;
        }
        for (UsbDevice usbDevice : deviceList.values()) {
            for (int i = 0; i < this.mUsbDeviceNum; i++) {
                // 找到我们希望的设备后，则注册用户选择广播(提示用户是否允许连接)
                if (String.format("%04x:%04x", new Object[]{Integer.valueOf(usbDevice.getVendorId()), Integer.valueOf(usbDevice.getProductId())}).equals(this.mListUsbDevice.get(i))) {
                    IntentFilter intentFilter = new IntentFilter(this.mStringPermission);
                    intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
                    this.mContext.registerReceiver(this.mBroadcastRecv, intentFilter);  // 注册广播
                    this.mIsUsbTached = true;
                    return usbDevice;
                }

                Log.d("CH34xAndroidDriver", "String.format not match");
            }
        }
        return null;
    }

    public void OpenDevice(UsbDevice usbDevice) {
        this.mPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(this.mStringPermission), 0);

        // 判断当前USB是否有访问权限，没有则询问用户是否授予权限
        if (this.mUsbManager.hasPermission(usbDevice)) {
            InitUsbDevice(usbDevice);
            return;
        }
        synchronized (this.mBroadcastRecv) {
            /* 调用requestPermission()方法时，系统会显示一个对话框，询问用户是否允许跟该USB设备进行连接。
            当用户回应这个对话框时，我们的广播接收器就会收到一个包含用一个boolean值来表示结果的EXTRA_PERMISSION_GRANTED字段的意图。
            在您连接设备之前检查这个字段的值是否为true。*/
            this.mUsbManager.requestPermission(usbDevice, this.mPendingIntent);
        }
    }

    public int ReadData(byte[] buff, int len) {
        int i2 = 0;
        synchronized (this.mObjectReadSem) {
            try {
                this.mSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (len > 0) {
                if (this.mRecvBuffCount != 0) {
                    int i3 = len > this.mRecvBuffCount ? this.mRecvBuffCount : len;
                    this.mRecvBuffCount -= i3;
                    while (i2 < i3) {
                        buff[i2] = this.mRecvBuffer[this.mRecvBuffTail];
                        this.mRecvBuffTail++;
                        this.mRecvBuffTail %= RecvBuffSize;
                        i2++;
                    }
                    this.mSemaphore.release();
                    return i3;
                }
            }
            this.mSemaphore.release();
            return 0;
        }
    }

    public int ResumeUsbList() {
        this.mUsbManager = (UsbManager) this.mContext.getSystemService(Context.USB_SERVICE);
        this.mPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(this.mStringPermission), 0);
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        if (deviceList.isEmpty()) {
           // Toast.makeText(this.mContext, "No Device Or Device Not Match", Toast.LENGTH_LONG).show();
            return -1;
        }

        for (UsbDevice usbDevice : deviceList.values()) {
            for (int i = 0; i < this.mUsbDeviceNum; i++) {
                if (String.format("%04x:%04x", new Object[]{Integer.valueOf(usbDevice.getVendorId()), Integer.valueOf(usbDevice.getProductId())}).equals(this.mListUsbDevice.get(i))) {
                    IntentFilter intentFilter = new IntentFilter(this.mStringPermission);
                    intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
                    this.mContext.registerReceiver(this.mBroadcastRecv, intentFilter);
                    this.mIsUsbTached = true;
                    if (this.mUsbManager.hasPermission(usbDevice)) {
                        InitUsbDevice(usbDevice);
                        return 0;
                    }
                  //  Toast.makeText(this.mContext, "No Perssion!", Toast.LENGTH_LONG).show();
                    synchronized (this.mBroadcastRecv) {
                        this.mUsbManager.requestPermission(usbDevice, this.mPendingIntent);
                    }
                    return -2;
                }
                Log.d("CH34xAndroidDriver", "String.format not match");
            }
        }
        return -1;
    }

    public int ResumeUsbPermission() {
        this.mUsbManager = (UsbManager) this.mContext.getSystemService(Context.USB_SERVICE);
        this.mPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(this.mStringPermission), 0);
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        if (deviceList.isEmpty()) {
         //   Toast.makeText(this.mContext, "No Device Or Device Not Match", Toast.LENGTH_LONG).show();
            return -1;
        }
        for (UsbDevice usbDevice : deviceList.values()) {
            int i = 0;
            while (i < this.mUsbDeviceNum) {
                if (!String.format("%04x:%04x", new Object[]{Integer.valueOf(usbDevice.getVendorId()), Integer.valueOf(usbDevice.getProductId())}).equals(this.mListUsbDevice.get(i))) {
                    Log.d("CH34xAndroidDriver", "String.format not match");
                    i++;
                } else if (this.mUsbManager.hasPermission(usbDevice)) {
                    return 0;
                } else {
                    synchronized (this.mBroadcastRecv) {
                        this.mUsbManager.requestPermission(usbDevice, this.mPendingIntent);
                    }
                    return -2;
                }
            }
        }
        return -1;
    }

    public boolean SetConfig(int baudRate, byte dataBit, byte stopBit, byte parity, byte flowControl) {
        int i2;
        int i3 = 100;
        int i4 = 2;

        switch (parity) {
            case (byte) 0:  // none
                i2 = 0;
                break;
            case (byte) 1:  // Odd
                i2 = 8;
                break;
            case (byte) 2:  // Even
                i2 = 24;
                break;
            case (byte) 3:  // Mark
                i2 = 40;
                break;
            case (byte) 4:  // Space
                i2 = 56;
                break;
            default:
                i2 = 0;
                break;
        }

        // 0:1个停止位；1:2个停止位
        if (stopBit == (byte) 2) {
            i2 = (char) (i2 | 4);
        }

        switch (dataBit) {
            case (byte) 5:
                i2 = (char) i2;
                break;
            case (byte) 6:
                i2 = (char) (i2 | 1);
                break;
            case (byte) 7:
                i2 = (char) (i2 | 2);
                break;
            case (byte) 8:
                i2 = (char) (i2 | 3);
                break;
            default:
                i2 = (char) (i2 | 3);
                break;
        }

        i2 = (((char) (i2 | 192)) << 8) | 156;
        switch (baudRate) {
            case 50:
                i3 = 22;
                i4 = 0;
                break;
            case 75:
                i4 = 0;
                break;
            case 110:
                i3 = 150;
                i4 = 0;
                break;
            case 135:
                i3 = 169;
                i4 = 0;
                break;
            case 150:
                i3 = 178;
                i4 = 0;
                break;
            case 300:
                i3 = 217;
                i4 = 0;
                break;
            case 600:
                i4 = 1;
                break;
            case 1200:
                i3 = 178;
                i4 = 1;
                break;
            case 1800:
                i3 = 204;
                i4 = 1;
                break;
            case 2400:
                i3 = 217;
                i4 = 1;
                break;
            case 4800:
                break;
            case 9600:
                i3 = 178;
                break;
            case 19200:
                i3 = 217;
                break;
            case 38400:
                i4 = 3;
                break;
            case 57600:
                i3 = 152;
                i4 = 3;
                break;
            case 115200:
                i3 = 204;
                i4 = 3;
                break;
            case 230400:
                i3 = 230;
                i4 = 3;
                break;
            case 460800:
                i3 = 243;
                i4 = 3;
                break;
            case 500000:
                i3 = 244;
                i4 = 3;
                break;
            case 921600:
                i4 = 7;
                i3 = 243;
                break;
            case 1000000:
                i3 = 250;
                i4 = 3;
                break;
            case 2000000:
                i3 = 253;
                i4 = 3;
                break;
            case 3000000:
                i3 = 254;
                i4 = 3;
                break;
            default:
                i3 = 178;
                break;
        }

        i3 = CH34xConfig(161, i2, (i3 << 8) | ((i4 | 136) | 0));
        if (flowControl == (byte)1) {
            CH34xConfig(164, -97, 0);
        }
        return i3 >= 0;
    }

    public boolean SetTimeOut(int writeTimeout, int readTimeout) {
        this.mWriteTimeout = writeTimeout;
        return true;
    }

    public boolean UartInit() {
        byte[] buffer = new byte[8];

        CH34xConfig(161, 0, 0);
        if (CH34xConfig(95, 0, 0, buffer, 2) < 0) {
            return false;
        }

        CH34xConfig(154, 4882, 55682);
        CH34xConfig(154, 3884, 4);

        if (CH34xConfig(149, 9496, 0, buffer, 2) < 0) {
            return false;
        }

        CH34xConfig(154, 10023, 0);
        CH34xConfig(164, 255, 0);
        return true;
    }

    public boolean UsbFeatureSupported() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.usb.host");
    }

    public int WriteData(byte[] buff, int len) {
        return WriteData(buff, len, this.mWriteTimeout);
    }

    public int WriteData(byte[] buff, int len, int timeout) {
        int i3 = 0;
        synchronized (this.mObjectWriteSem) {
            if (this.mEndpointOut == null) {
                return -1;
            }
            int i4 = len;
            while (i3 < len) {
                int min = Math.min(i4, this.mMaxPacketSize);
                byte []obj = new byte[min];
                if (i3 == 0) {
                    System.arraycopy(buff, 0, obj, 0, min);
                } else {
                    System.arraycopy(buff, i3, obj, 0, min);
                }
                int bulkTransfer = this.mUsbDeviceConn.bulkTransfer(this.mEndpointOut, obj, min, timeout);
                if (bulkTransfer < 0) {
                    return -2;
                }
                i4 -= bulkTransfer;
                i3 += bulkTransfer;
            }
            return i3;
        }
    }

    protected UsbDevice getUsbDevice() {
        return this.mUsbDevice;
    }

    public boolean isConnected() {
        return (this.mUsbDevice == null || this.mUsbInterface == null || this.mUsbDeviceConn == null) ? false : true;
    }
}
