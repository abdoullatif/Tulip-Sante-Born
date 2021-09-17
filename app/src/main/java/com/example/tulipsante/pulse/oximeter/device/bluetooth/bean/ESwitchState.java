package com.example.tulipsante.pulse.oximeter.device.bluetooth.bean;

public enum ESwitchState {
    opened(1), //蓝牙已打开
    closed(2); //蓝牙已关闭

    private int value;

    ESwitchState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
