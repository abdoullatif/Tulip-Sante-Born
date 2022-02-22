package com.example.tulipsante.utils;

public class SpoBean {
    private byte BarGraph;

    private boolean FingerOff;

    private boolean HeartFlag = false;

    private int PR;

    private int PlethWave;

    private boolean ProbeOff;

    private boolean SearchTooLong;

    private boolean Searching;

    private int SpO2;

    private byte Strength;

    public byte getBarGraph() {
        return this.BarGraph;
    }

    public int getPR() {
        return this.PR;
    }

    public int getPlethWave() {
        return this.PlethWave;
    }

    public int getSpO2() {
        return this.SpO2;
    }

    public byte getStrength() {
        return this.Strength;
    }

    public boolean isFingerOff() {
        return this.FingerOff;
    }

    public boolean isHeartFlag() {
        return this.HeartFlag;
    }

    public boolean isProbeOff() {
        return this.ProbeOff;
    }

    public boolean isSearchTooLong() {
        return this.SearchTooLong;
    }

    public boolean isSearching() {
        return this.Searching;
    }

    public void setBarGraph(byte paramByte) {
        this.BarGraph = paramByte;
    }

    public void setFingerOff(boolean paramBoolean) {
        this.FingerOff = paramBoolean;
    }

    public void setHeartFlag(boolean paramBoolean) {
        this.HeartFlag = paramBoolean;
    }

    public void setPR(int paramInt) {
        this.PR = paramInt;
    }

    public void setPlethWave(int paramInt) {
        this.PlethWave = paramInt;
    }

    public void setProbeOff(boolean paramBoolean) {
        this.ProbeOff = paramBoolean;
    }

    public void setSearchTooLong(boolean paramBoolean) {
        this.SearchTooLong = paramBoolean;
    }

    public void setSearching(boolean paramBoolean) {
        this.Searching = paramBoolean;
    }

    public void setSpO2(int paramInt) {
        this.SpO2 = paramInt;
    }

    public void setStrength(byte paramByte) {
        this.Strength = paramByte;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SpoBean{PlethWave=");
        stringBuilder.append(this.PlethWave);
        stringBuilder.append(", PR=");
        stringBuilder.append(this.PR);
        stringBuilder.append(", SpO2=");
        stringBuilder.append(this.SpO2);
        stringBuilder.append(", FingerOff=");
        stringBuilder.append(this.FingerOff);
        stringBuilder.append(", HeartFlag=");
        stringBuilder.append(this.HeartFlag);
        stringBuilder.append(", SearchTooLong=");
        stringBuilder.append(this.SearchTooLong);
        stringBuilder.append(", ProbeOff=");
        stringBuilder.append(this.ProbeOff);
        stringBuilder.append(", Searching=");
        stringBuilder.append(this.Searching);
        stringBuilder.append(", BarGraph=");
        stringBuilder.append(this.BarGraph);
        stringBuilder.append(", Strength=");
        stringBuilder.append(this.Strength);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}