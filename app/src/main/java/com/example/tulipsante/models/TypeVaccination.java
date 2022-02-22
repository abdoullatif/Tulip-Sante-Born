package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "typeVaccination")
public class TypeVaccination {
    @NonNull
    @PrimaryKey
    private String idTypeVaccination;
    private String type;
    private String duree;
    private String flagTransmis;

    public TypeVaccination(@NonNull String idTypeVaccination, String type, String duree, String flagTransmis) {
        this.idTypeVaccination = idTypeVaccination;
        this.type = type;
        this.duree = duree;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdTypeVaccination() {
        return idTypeVaccination;
    }

    public void setIdTypeVaccination(@NonNull String idTypeVaccination) {
        this.idTypeVaccination = idTypeVaccination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }
}
