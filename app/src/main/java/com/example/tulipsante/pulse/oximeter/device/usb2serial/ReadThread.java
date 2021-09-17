package com.example.tulipsante.pulse.oximeter.device.usb2serial;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbRequest;
import android.util.Log;

import java.nio.ByteBuffer;

final class ReadThread extends Thread {
    private CH34xUARTDriver mCH34xUARTDriver;
    private UsbEndpoint mUsbEndpoint;
    private UsbDeviceConnection mUsbDeviceConn;

    ReadThread(CH34xUARTDriver cH34xUARTDriver, UsbEndpoint usbEndpoint, UsbDeviceConnection usbDeviceConnection) {
        this.mCH34xUARTDriver = cH34xUARTDriver;
        this.mUsbEndpoint = usbEndpoint;
        this.mUsbDeviceConn = usbDeviceConnection;
        for (int i = 0; i < 20; i++) {
            cH34xUARTDriver.mUsbRequests[i] = new UsbRequest();
            cH34xUARTDriver.mUsbRequests[i].initialize(this.mUsbDeviceConn, this.mUsbEndpoint);
            cH34xUARTDriver.mByteBuffers[i] = ByteBuffer.allocate(32);
        }
        setPriority(10);
    }

    public final void run() {
        for (int i = 0; i < 20; i++) {
            this.mCH34xUARTDriver.mUsbRequests[i].queue(this.mCH34xUARTDriver.mByteBuffers[i], 32);
        }

        while (this.mCH34xUARTDriver.mIsThreadRunning) {
            while (this.mCH34xUARTDriver.mRecvBuffCount > mCH34xUARTDriver.RecvBuffSize - 200) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (this.mUsbEndpoint == null) continue;

            for (int i = 0; i < 20; i++) {
                if (this.mUsbDeviceConn.requestWait() == this.mCH34xUARTDriver.mUsbRequests[i]) {
                    this.mCH34xUARTDriver.mTmpBuffer = this.mCH34xUARTDriver.mByteBuffers[i].array();
                    this.mCH34xUARTDriver.mTmpBuffLen = this.mCH34xUARTDriver.mByteBuffers[i].position();
                    if (this.mCH34xUARTDriver.mTmpBuffLen > 0) {
                        try {
                            this.mCH34xUARTDriver.mSemaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        mCH34xUARTDriver.mRecvBuffCount = mCH34xUARTDriver.mRecvBuffCount + this.mCH34xUARTDriver.mTmpBuffLen;
                        for (int j = 0; j < this.mCH34xUARTDriver.mTmpBuffLen; j++) {
                            this.mCH34xUARTDriver.mRecvBuffer[this.mCH34xUARTDriver.mRecvBuffHead] = this.mCH34xUARTDriver.mTmpBuffer[j];
                            mCH34xUARTDriver.mRecvBuffHead = mCH34xUARTDriver.mRecvBuffHead + 1;
                            mCH34xUARTDriver.mRecvBuffHead = mCH34xUARTDriver.mRecvBuffHead % mCH34xUARTDriver.RecvBuffSize;
                        }

                        if (mCH34xUARTDriver.mRecvBuffHead >= mCH34xUARTDriver.mRecvBuffTail) {
                            mCH34xUARTDriver.mRecvBuffCount = mCH34xUARTDriver.mRecvBuffHead - mCH34xUARTDriver.mRecvBuffTail;
                        } else {
                            mCH34xUARTDriver.mRecvBuffCount = (mCH34xUARTDriver.RecvBuffSize - mCH34xUARTDriver.mRecvBuffTail) + mCH34xUARTDriver.mRecvBuffHead;
                        }

                        this.mCH34xUARTDriver.mSemaphore.release();
                    } else if (this.mCH34xUARTDriver.mTmpBuffLen < 0) {
                        Log.e("CH34xAndroidDriver", "read error " + this.mCH34xUARTDriver.mTmpBuffLen);
                    }

                    this.mCH34xUARTDriver.mUsbRequests[i].queue(this.mCH34xUARTDriver.mByteBuffers[i], 32);
                }
            }
        }
    }
}
