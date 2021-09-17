package com.example.tulipsante.pulse.oximeter.device.bluetooth;

public class BleConfig {
    private int scanTime = 10000;                   //指定扫描的超时时间，默认10s
    private String broadcastName = "";              //指定扫描的广播名，默认是空
    private String serverUUID = "fff0";             //指定透传服务的UUID，小写
    private String writeCharacteristicUUID = "fff2";    //指定透传write特征的UUID，小写
    private String notifyCharacteristicUUID = "fff1";   //指定透传notify特征的UUID，小写

    public BleConfig() {
    }

    public int getScanTime() {
        return scanTime;
    }

    public BleConfig setScanTime(int scanTime) {
        this.scanTime = scanTime;
        return this;
    }

    public String getBroadcastName() {
        return broadcastName;
    }

    public BleConfig setBroadcastName(String broadcastName) {
        this.broadcastName = broadcastName;
        return this;
    }

    public String getServerUUID() {
        return serverUUID;
    }

    public BleConfig setServerUUID(String serverUUID) {
        this.serverUUID = serverUUID;
        return this;
    }

    public String getWriteCharacteristicUUID() {
        return writeCharacteristicUUID;
    }

    public BleConfig setWriteCharacteristicUUID(String writeCharacteristicUUID) {
        this.writeCharacteristicUUID = writeCharacteristicUUID;
        return this;
    }

    public String getNotifyCharacteristicUUID() {
        return notifyCharacteristicUUID;
    }

    public BleConfig setNotifyCharacteristicUUID(String notifyCharacteristicUUID) {
        this.notifyCharacteristicUUID = notifyCharacteristicUUID;
        return this;
    }
}
