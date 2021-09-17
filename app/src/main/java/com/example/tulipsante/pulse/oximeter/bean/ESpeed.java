package com.example.tulipsante.pulse.oximeter.bean;

import java.util.ArrayList;
import java.util.List;

// 速度5mm/s，10mm/s，12.5mm/s，25mm/s，50mm/s
public enum ESpeed {
    v5mms(5f), v10mms(10f), v12_5mms(12.5f), v25mms(25f), v50mms(50f);

    private float value = 0;

    ESpeed(float value) {
        this.value = value;
    }

    public float value() {
        return this.value;
    }

    public static ESpeed getEnum(String str) {
        if ("5mm/s".equals(str)) {
            return v5mms;
        } else if ("10mm/s".equals(str)) {
            return v10mms;
        } else if ("12.5mm/s".equals(str)) {
            return v12_5mms;
        } else if ("25mm/s".equals(str)) {
            return v25mms;
        } else if ("50mm/s".equals(str)) {
            return v50mms;
        }
        return v25mms;
    }

    public static List<String> getList() {
        List<String> list = new ArrayList<>();
        list.add(v5mms.toString());
        list.add(v10mms.toString());
        list.add(v12_5mms.toString());
        list.add(v25mms.toString());
        list.add(v50mms.toString());
        return list;
    }

    @Override
    public String toString() {
        String name = name();
        name = name.replace("v", "");
        name = name.replace("_", ".");
        name = name.replace("mms", "mm/s");
        return name;
    }
}