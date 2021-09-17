package com.example.tulipsante.pulse.oximeter;

public class Config {

    // device
    public static final int cSpo2Max = 100;
    public static final int cSpo2Min = 70;
    public static final int cPrMax = 250;
    public static final int cPrMin = 25;
    public static final float cPiMax = 20;
    public static final float cPiMin = 0.2f;
    public static final String cSpo2DefaultRange = "80-100";
    public static final String cPrDefaultRange = "50-100";
    public static final int cFreq = 60; // 设备采集频率60，每秒采集60个点

    // broadcast
    public static final String RECEIVE_SETTING_PARAM = "receive.setting.param";
    public static final String RECEIVE_NEW_SPO_STATS = "receive.new.spo.stats";
}
