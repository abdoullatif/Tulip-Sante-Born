package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "allergie")
public class Allergie {
    @NonNull
    @PrimaryKey
    private String idAllergie;
    private String type;
    private String description;
    private String flagTransmis;

    public Allergie(@NonNull String idAllergie, String type, String description, String flagTransmis) {
        this.idAllergie = idAllergie;
        this.type = type;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdAllergie() {
        return idAllergie;
    }

    public void setIdAllergie(@NonNull String idAllergie) {
        this.idAllergie = idAllergie;
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
