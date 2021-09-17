package com.example.tulipsante.pulse.oximeter.bean;

public enum ELang {
     en(1 );

    private int value = 1;

    ELang(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ELang getEnum(String str) {

            return en;

    }
}