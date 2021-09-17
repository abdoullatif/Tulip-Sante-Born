package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorieSigneVitaux")
public class CategorieSigneVitaux {
    @NonNull
    @PrimaryKey
    private String idCatSV;
    private String description;
    private String flagTransmis;

    public CategorieSigneVitaux(@NonNull String idCatSV, String description, String flagTransmis) {
        this.idCatSV = idCatSV;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdCatSV() {
        return idCatSV;
    }

    public void setIdCatSV(@NonNull String idCatSV) {
        this.idCatSV = idCatSV;
    }

    public String getDescription() {
        return description;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
