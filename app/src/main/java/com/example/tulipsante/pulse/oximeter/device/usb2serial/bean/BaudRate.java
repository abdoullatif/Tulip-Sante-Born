package com.example.tulipsante.pulse.oximeter.device.usb2serial.bean;

import android.text.TextUtils;

public class BaudRate {
    private int baudRate;
    private byte dataBit;
    private byte stopBit;
    private byte parity;
    private byte flowControl;

    public BaudRate() {
    }

    public BaudRate(int baudRate, byte dataBit, byte stopBit, byte parity, byte flowControl) {
        this.baudRate = baudRate;
        this.dataBit = dataBit;
        this.stopBit = stopBit;
        this.parity = parity;
        this.flowControl = flowControl;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public byte getDataBit() {
        return dataBit;
    }

    public byte getStopBit() {
        return stopBit;
    }

    public byte getParity() {
        return parity;
    }

    public byte getFlowControl() {
        return flowControl;
    }

    @Override
    public String toString() {
        return baudRate + "-" + dataBit + "-" + stopBit + "-" + parity + "-" + flowControl;
    }

    public static BaudRate parseStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }

        String[] arr = str.split("-");
        if (arr.length != 5) {
            return null;
        }

        return new BaudRate(Integer.parseInt(arr[0]), Byte.parseByte(arr[1]), Byte.parseByte(arr[2]), Byte.parseByte(arr[3]), Byte.parseByte(arr[4]));
    }
}
