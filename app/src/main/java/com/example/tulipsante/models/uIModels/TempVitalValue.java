package com.example.tulipsante.models.uIModels;

public class TempVitalValue {
    private String id;
    private String vital;
    private String value;

    public TempVitalValue() {
    }

    public TempVitalValue(String id, String vital, String value) {
        this.id = id;
        this.vital = vital;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVital() {
        return vital;
    }

    public String getValue() {
        return value;
    }
}
