package com.example.tulipsante.utils;

import android.text.TextUtils;

public class BaudRate {
    private int baudRate;

    private byte dataBit;

    private byte flowControl;

    private byte parity;

    private byte stopBit;

    public BaudRate() {}

    public BaudRate(int paramInt, byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4) {
        this.baudRate = paramInt;
        this.dataBit = paramByte1;
        this.stopBit = paramByte2;
        this.parity = paramByte3;
        this.flowControl = paramByte4;
    }

    public static BaudRate parseStr(String paramString) {
        if (TextUtils.isEmpty(paramString))
            return null;
        String[] arrayOfString = paramString.split("-");
        return (arrayOfString.length != 5) ? null : new BaudRate(Integer.parseInt(arrayOfString[0]), Byte.parseByte(arrayOfString[1]), Byte.parseByte(arrayOfString[2]), Byte.parseByte(arrayOfString[3]), Byte.parseByte(arrayOfString[4]));
    }

    public int getBaudRate() {
        return this.baudRate;
    }

    public byte getDataBit() {
        return this.dataBit;
    }

    public byte getFlowControl() {
        return this.flowControl;
    }

    public byte getParity() {
        return this.parity;
    }

    public byte getStopBit() {
        return this.stopBit;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.baudRate);
        stringBuilder.append("-");
        stringBuilder.append(this.dataBit);
        stringBuilder.append("-");
        stringBuilder.append(this.stopBit);
        stringBuilder.append("-");
        stringBuilder.append(this.parity);
        stringBuilder.append("-");
        stringBuilder.append(this.flowControl);
        return stringBuilder.toString();
    }
}
