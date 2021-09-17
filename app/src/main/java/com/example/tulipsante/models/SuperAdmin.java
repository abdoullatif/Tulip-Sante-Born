package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "superAdmin")
public class SuperAdmin {
    @NonNull
    @PrimaryKey
    String idSuperAdmin;
    String username;
    String password;
    String flagTransmis;

    public SuperAdmin(@NonNull String idSuperAdmin, String username, String password, String flagTransmis) {
        this.idSuperAdmin = idSuperAdmin;
        this.username = username;
        this.password = password;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdSuperAdmin() {
        return idSuperAdmin;
    }

    public void setIdSuperAdmin(@NonNull String idSuperAdmin) {
        this.idSuperAdmin = idSuperAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }
}
