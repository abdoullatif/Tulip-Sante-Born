package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "specialite")
public class Specialite {
    @NonNull
    @PrimaryKey
    private String idSpecialite;
    private String description;
    private String flagTransmis;

    public Specialite(@NonNull String idSpecialite, String description, String flagTransmis) {
        this.idSpecialite = idSpecialite;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdSpecialite() {
        return idSpecialite;
    }

    public void setIdSpecialite(@NonNull String idSpecialite) {
        this.idSpecialite = idSpecialite;
    }

    public String getDescription() {
        return description;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
