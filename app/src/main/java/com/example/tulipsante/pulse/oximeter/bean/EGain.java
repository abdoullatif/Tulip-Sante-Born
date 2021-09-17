package com.example.tulipsante.pulse.oximeter.bean;

import java.util.ArrayList;
import java.util.List;

// 增益
public enum EGain {
    x0_25(0.25f), x0_5(0.5f), x1(1f), x2(2f);

    private float value = 0;

    EGain(float value) {
        this.value = value;
    }

    public float value() {
        return this.value;
    }

    public static EGain getEnum(String str) {
        if ("X0.25".equals(str)) {
            return x0_25;
        } else if ("X0.5".equals(str)) {
            return x0_5;
        } else if ("X1".equals(str)) {
            return x1;
        } else if ("X2".equals(str)) {
            return x2;
        }
        return x1;
    }

    public static List<String> getList() {
        List<String> list = new ArrayList<>();
        list.add(x0_25.toString());
        list.add(x0_5.toString());
        list.add(x1.toString());
        list.add(x2.toString());
        return list;
    }

    @Override
    public String toString() {
        String name = name();
        name = name.replace("_", ".");
        return name.toUpperCase();
    }
}