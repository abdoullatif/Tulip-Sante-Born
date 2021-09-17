package com.example.tulipsante.pulse.oximeter.device.bluetooth.bean;

public enum EBleState {
    connecting(1),      //正在连接中
    connected(2),       //已经连接成功
    disconnecting(3),   //正在断开中
    disconnected(4),    //已经断开成功
    unknown(5);         //未知状态
    private int value;

    EBleState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
