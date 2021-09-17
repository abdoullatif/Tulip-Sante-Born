package com.example.tulipsante.pulse.oximeter.device.usb2serial;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import com.example.tulipsante.R;
import com.example.tulipsante.pulse.oximeter.device.usb2serial.bean.BaudRate;


public class UsbMgr {
    private final String TAG = "usb";
    private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";
    private static UsbMgr mInstance;
    private Context mContext;
    private CH34xUARTDriver mDriver;
    private boolean isOpened = false;
    private ReadThread mReadThread;
    private OnDeviceDataCallback mCallback;

    private UsbMgr(Context context) {
        mContext = context.getApplicationContext();
        UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        mDriver = new CH34xUARTDriver(usbManager, mContext, ACTION_USB_PERMISSION);
    }

    public static UsbMgr getInstance(Context context) {
        if (mInstance == null) {
            synchronized (UsbMgr.class) {
                if (mInstance == null) {
                    mInstance = new UsbMgr(context);
                }
            }
        }
        return mInstance;
    }

    public boolean UsbFeatureSupported() {
        return mDriver.UsbFeatureSupported();
    }

    public void resumeUsbPermission() {
        if (!mDriver.isConnected()) {
            if (-2 == mDriver.ResumeUsbPermission()) {
              //  Toast.makeText(mContext, mContext.getString(R.string.toast_no_usb_permission), Toast.LENGTH_SHORT).show();

            }
        }
    }

    public CH34xUARTDriver  checkPermission() {
               return  mDriver;
    }

    public void setDataListener(OnDeviceDataCallback callback) {
        this.mCallback = callback;
    }

    public boolean openDevice(BaudRate baudRate) {
        if (baudRate == null) {

         //   Toast.makeText(mContext, mContext.getString(R.string.toast_baud_rate_null), Toast.LENGTH_SHORT).show();
            return true;
        }

        if (isOpened) {

            return true;
        }

        int retVal = mDriver.ResumeUsbList();
        if (retVal == -1) { // 枚举CH34X设备并打开设备

         //   Toast.makeText(mContext, mContext.getString(R.string.toast_open_usb_fail), Toast.LENGTH_SHORT).show();
            mDriver.CloseDevice();
        } else if (retVal == 0) {
            if (!mDriver.UartInit()) {

               // Toast.makeText(mContext, mContext.getString(R.string.toast_usb_init_fail), Toast.LENGTH_SHORT).show();
                return false;
            }
            isOpened = true;

          //  Toast.makeText(mContext, mContext.getString(R.string.toast_open_usb_success), Toast.LENGTH_SHORT).show();

            if (mDriver.SetConfig(baudRate.getBaudRate(), baudRate.getDataBit(), baudRate.getStopBit(), baudRate.getParity(), baudRate.getFlowControl())) {

            } else {

            }

            if (mReadThread == null) {
                mReadThread = new ReadThread();
                mReadThread.start();
            }

            return true;
        } else {

           // Toast.makeText(mContext, mContext.getString(R.string.toast_usb_unauthorized), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void closeDevice() {
        isOpened = false;
        mDriver.CloseDevice();
        if (mReadThread != null) {
            mReadThread.close();
            mReadThread = null;
        }
    }

    private class ReadThread extends Thread {
        private boolean isRunning;

        public ReadThread() {
            this.isRunning = true;
        }

        @Override
        public synchronized void start() {
            super.start();

        }

        public synchronized void close() {
            isRunning = false;

        }

        @Override
        public void run() {
            super.run();
            byte[] buff = new byte[512];

            while (isRunning) {
                if (!isOpened) break;
                if (!mDriver.isConnected()) break;
                int len = mDriver.ReadData(buff, 512);
                if (mCallback != null) {
                    mCallback.onData(buff, len);
                }
            }
        }
    }

    public interface OnDeviceDataCallback {
        void onData(byte[] data, int len);
    }
}
