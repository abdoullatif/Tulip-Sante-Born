package com.example.tulipsante.pulse.oximeter.repository.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SpoRecord {
    @Id(autoincrement = true)
    Long id;            // 自增id
    String uid;         // 用户id
    String deviceId;    // 设备id
    Long time;          // 时间戳，单位ms
    Integer spo2;       // 血氧
    Integer pr;         // 脉率
    Float pi;           // 灌注指数

    @Generated(hash = 1626343837)
    public SpoRecord(Long id, String uid, String deviceId, Long time, Integer spo2,
                     Integer pr, Float pi) {
        this.id = id;
        this.uid = uid;
        this.deviceId = deviceId;
        this.time = time;
        this.spo2 = spo2;
        this.pr = pr;
        this.pi = pi;
    }

    @Generated(hash = 1813722805)
    public SpoRecord() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getSpo2() {
        return this.spo2;
    }

    public void setSpo2(Integer spo2) {
        this.spo2 = spo2;
    }

    public Integer getPr() {
        return this.pr;
    }

    public void setPr(Integer pr) {
        this.pr = pr;
    }

    public Float getPi() {
        return this.pi;
    }

    public void setPi(Float pi) {
        this.pi = pi;
    }

    @Override
    public String toString() {
        return "SpoRecord{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", time=" + time +
                ", spo2=" + spo2 +
                ", pr=" + pr +
                ", pi=" + pi +
                '}';
    }
}
