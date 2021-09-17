package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "maladie")
public class Maladie {
    @NonNull
    @PrimaryKey
    private String idMaladie;
    private String description;
    private String type;
    private String flagTransmis;

    public Maladie() {
    }

    public Maladie(@NonNull String idMaladie, String description, String type, String flagTransmis) {
        this.idMaladie = idMaladie;
        this.description = description;
        this.type = type;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdMaladie() {
        return idMaladie;
    }

    public void setIdMaladie(@NonNull String idMaladie) {
        this.idMaladie = idMaladie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }

    @Override
    public String toString() {
        return "(" + idMaladie + ") " + description;
    }
}
