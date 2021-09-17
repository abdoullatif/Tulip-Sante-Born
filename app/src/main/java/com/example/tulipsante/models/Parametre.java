package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parametre")
public class Parametre {
    @NonNull
    @PrimaryKey
    private String id;
    private String deviceName;
    private String dbName;
    private String dbPass;
    private String ftpName;
    private String ftpPass;
    private String webSiteName;

    public Parametre(
            @NonNull String id,
            String deviceName,
            String dbName,
            String dbPass,
            String ftpName,
            String ftpPass,
            String webSiteName) {
        this.id = id;
        this.deviceName = deviceName;
        this.dbName = dbName;
        this.dbPass = dbPass;
        this.ftpName = ftpName;
        this.ftpPass = ftpPass;
        this.webSiteName = webSiteName;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public String getFtpName() {
        return ftpName;
    }

    public void setFtpName(String ftpName) {
        this.ftpName = ftpName;
    }

    public String getFtpPass() {
        return ftpPass;
    }

    public void setFtpPass(String ftpPass) {
        this.ftpPass = ftpPass;
    }

    public String getWebSiteName() {
        return webSiteName;
    }

    public void setWebSiteName(String webSiteName) {
        this.webSiteName = webSiteName;
    }
}
