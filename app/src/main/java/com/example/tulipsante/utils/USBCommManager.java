package com.example.tulipsante.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.hoho.android.usbserial.driver.CdcAcmSerialDriver;
import com.hoho.android.usbserial.driver.ProbeTable;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

public class USBCommManager {
   public String TAG = USBCommManager.class.getSimpleName();
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

   private static Context mContext;
   private static USBCommManager usbCommManager;
   private USBCommListener mListener;

   private boolean mIsPlugged;
   private List<UsbSerialDriver> availableDrivers;

//    private OnDeviceDataCallback mCallback;

   private final UsbManager mUsbManager;
   private UsbSerialPort mSerialPort;
   ProbeTable customTable = new ProbeTable();

    private PendingIntent mPermissionIntent = null;


    private USBCommManager() {
       mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);

   }

   public static USBCommManager getUSBManager(Context context) {
       System.out.println("Getting manager");
       if(usbCommManager == null) {
           mContext = context;
           usbCommManager = new USBCommManager();
       }
       System.out.println(usbCommManager);
       return usbCommManager;
   }

   public void setListener(USBCommListener listener) {
       mListener = listener;
   }

   public void initConnection() {
       customTable.addProduct(6790,29987, CdcAcmSerialDriver.class);
       UsbSerialProber prober = new UsbSerialProber(customTable);
       System.out.println(mUsbManager.getDeviceList());
       availableDrivers = prober.findAllDrivers(mUsbManager);
       System.out.println(availableDrivers.size());
       if(availableDrivers.size() > 0) {
           requestPermission();
//           buildConnection();
       }
   }

   private void startScan() {
       new Thread(() -> {
           while (mIsPlugged) {
               availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);
               if(availableDrivers.size() == 0) {
                   mIsPlugged = false;
               }

               try {
                   Thread.sleep(100);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

           }

           mListener.onUSBStateChanged(mIsPlugged);
       }).start();
   }


   private void requestPermission() {
        mPermissionIntent = PendingIntent.getActivity(mContext,0,new Intent(ACTION_USB_PERMISSION),0);
       UsbSerialDriver driver = availableDrivers.get(0);
       UsbDevice usbDevice = driver.getDevice();
       if(!mUsbManager.hasPermission(usbDevice)) {
           try {
               mUsbManager.requestPermission(usbDevice,mPermissionIntent);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       else if (mUsbManager.hasPermission(usbDevice)) {
           buildConnection();
           this.mIsPlugged = true;
           this.mListener.onUSBStateChanged(this.isPlugged());

           startScan();
       }
   }

    private void buildConnection() {
        UsbSerialDriver     driver     = availableDrivers.get(0);
        UsbDevice           usbDevice  = driver.getDevice();
        UsbDeviceConnection connection = mUsbManager.openDevice(usbDevice);
        this.mSerialPort = driver.getPorts().get(0);
        try {
            mSerialPort.open(connection);
            mSerialPort.setParameters(19200, 8, 1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            while (true) {
//                if(USBCommManager.this.mIsPlugged) {
                try {
                    byte[] buffer = new byte[512];
                    int numBytesRead = mSerialPort.read(buffer,100);
                    if (numBytesRead > 0) {
                        byte[] dat = new byte[numBytesRead];
                        System.arraycopy(buffer, 0, dat, 0, numBytesRead);
                        Log.d("USB_SERIAL", "Read " + numBytesRead + " bytes.");
                        System.out.println("===== reading the serial data bruv =====");
                        mListener.onReceiveData(buffer, numBytesRead);
                    }
                } catch (IOException e) { e.printStackTrace(); }
//                    continue;
//                }
//                return;
            }
        }).start();
    }

   public boolean isPlugged() {
       return mIsPlugged;
   }

   public interface USBCommListener {
       void onReceiveData(byte[] dat, int paramInt);
       void onUSBStateChanged(boolean isPlugged);
   }
}
