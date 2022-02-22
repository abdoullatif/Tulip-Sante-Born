package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "symptome")
public class Symptome {
    @NonNull
    @PrimaryKey
    private String idSymptome;
    private String type;
    private String description;
    private String flagTransmis;

    public Symptome(@NonNull String idSymptome, String type, String description, String flagTransmis) {
        this.idSymptome = idSymptome;
        this.type = type;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdSymptome() {
        return idSymptome;
    }

    public void setIdSymptome(@NonNull String idSymptome) {
        this.idSymptome = idSymptome;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
