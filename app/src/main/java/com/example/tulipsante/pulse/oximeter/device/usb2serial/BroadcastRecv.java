package com.example.tulipsante.pulse.oximeter.device.usb2serial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

final class BroadcastRecv extends BroadcastReceiver {
    private CH34xUARTDriver ch34xUARTDriver;

    BroadcastRecv(CH34xUARTDriver cH34xUARTDriver) {
        this.ch34xUARTDriver = cH34xUARTDriver;
    }

    public final void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
            Log.e("CH34xAndroidDriver", "Step1!\n");
        }
        // 判断用户是否允许进行通信
        else if (this.ch34xUARTDriver.mStringPermission.equals(action)) {
            Log.e("CH34xAndroidDriver", "Step2!\n");
            synchronized (CH34xUARTDriver.class) {
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED,false)) {
                    this.ch34xUARTDriver.InitUsbDevice(usbDevice);
                }
                // 用户选择不连接
                else {
                    Toast.makeText(this.ch34xUARTDriver.mContext, "Deny USB Permission", Toast.LENGTH_SHORT).show();
                    Log.d("CH34xAndroidDriver", "permission denied");
                }
            }
        }
        // USB断开时发送此广播信息
        else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
            Log.e("CH34xAndroidDriver", "Step3!\n");
            UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            Log.e("CH34xAndroidDriver", usbDevice.getDeviceName());

            // 判断是否是当前接入的USB断开
            for (int i = 0; i < this.ch34xUARTDriver.mUsbDeviceNum; i++) {
                if (String.format("%04x:%04x", new Object[]{Integer.valueOf(usbDevice.getVendorId()), Integer.valueOf(usbDevice.getProductId())}).equals(this.ch34xUARTDriver.mListUsbDevice.get(i))) {
                    Toast.makeText(this.ch34xUARTDriver.mContext, "Disconnect", Toast.LENGTH_SHORT).show();
                    this.ch34xUARTDriver.CloseDevice();
                }
            }
        } else {
            Log.e("CH34xAndroidDriver", "......");
        }
    }
}
