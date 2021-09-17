package com.example.tulipsante.pulse.oximeter.device.bluetooth.bean;

import androidx.annotation.NonNull;

public class BleDevice implements Comparable<BleDevice> {

    private String name;
    private String mac;
    private int rssi;

    public BleDevice(String name, String mac) {
        this.name = name;
        this.mac = mac;
    }

    public BleDevice(String name, String mac, int rssi) {
        this.name = name;
        this.mac = mac;
        this.rssi = rssi;
    }

    public String getName() {
        return name;
    }

    public String getMac() {
        return mac;
    }

    public int getRssi() {
        return rssi;
    }

    @Override
    public String toString() {
        return "BleDevice{" +
                "name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", rssi=" + rssi +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BleDevice bleDevice = (BleDevice) o;

        if (name != null ? !name.equals(bleDevice.name) : bleDevice.name != null) return false;
        return mac != null ? mac.equals(bleDevice.mac) : bleDevice.mac == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (mac != null ? mac.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(@NonNull BleDevice o) {
        return o.getRssi() - this.getRssi();
    }
}
