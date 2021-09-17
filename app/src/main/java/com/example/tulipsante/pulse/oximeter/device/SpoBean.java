package com.example.tulipsante.pulse.oximeter.device;

public class SpoBean {
    private int PlethWave;
    private int PR;
    private int SpO2;
    private boolean FingerOff;
    private boolean HeartFlag;
    private boolean SearchTooLong;
    private boolean ProbeOff;
    private boolean Searching;
    private byte BarGraph;
    private byte Strength;

    public SpoBean() {
        this.HeartFlag = false;
    }

    public int getPlethWave() {
        return PlethWave;
    }

    public void setPlethWave(int plethWave) {
        PlethWave = plethWave;
    }

    public int getPR() {
        return PR;
    }

    public void setPR(int PR) {
        this.PR = PR;
    }

    public int getSpO2() {
        return SpO2;
    }

    public void setSpO2(int spO2) {
        SpO2 = spO2;
    }

    public boolean isFingerOff() {
        return FingerOff;
    }

    public void setFingerOff(boolean fingerOff) {
        FingerOff = fingerOff;
    }

    public boolean isHeartFlag() {
        return HeartFlag;
    }

    public void setHeartFlag(boolean heartFlag) {
        HeartFlag = heartFlag;
    }

    public boolean isSearchTooLong() {
        return SearchTooLong;
    }

    public void setSearchTooLong(boolean searchTooLong) {
        SearchTooLong = searchTooLong;
    }

    public boolean isProbeOff() {
        return ProbeOff;
    }

    public void setProbeOff(boolean probeOff) {
        ProbeOff = probeOff;
    }

    public boolean isSearching() {
        return Searching;
    }

    public void setSearching(boolean searching) {
        Searching = searching;
    }

    public byte getBarGraph() {
        return BarGraph;
    }

    public void setBarGraph(byte barGraph) {
        BarGraph = barGraph;
    }

    public byte getStrength() {
        return Strength;
    }

    public void setStrength(byte strength) {
        Strength = strength;
    }

    @Override
    public String toString() {
        return "SpoBean{" +
                "PlethWave=" + PlethWave +
                ", PR=" + PR +
                ", SpO2=" + SpO2 +
                ", FingerOff=" + FingerOff +
                ", HeartFlag=" + HeartFlag +
                ", SearchTooLong=" + SearchTooLong +
                ", ProbeOff=" + ProbeOff +
                ", Searching=" + Searching +
                ", BarGraph=" + BarGraph +
                ", Strength=" + Strength +
                '}';
    }
}
