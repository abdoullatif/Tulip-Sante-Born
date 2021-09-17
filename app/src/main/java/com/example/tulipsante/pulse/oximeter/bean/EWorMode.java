package com.example.tulipsante.pulse.oximeter.bean;

import java.util.ArrayList;
import java.util.List;

public enum EWorMode {
    usb(0), ble(1);

    private int value = 0;

    EWorMode(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static EWorMode getEnum(String str) {
        if ("ble".equals(str)) {
            return ble;
        }
        return usb;
    }

    public static List<String> getList() {
        List<String> list = new ArrayList<>();
        list.add(usb.toString());
        list.add(ble.toString());
        return list;
    }
}