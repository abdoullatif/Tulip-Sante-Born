package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "commune",
        foreignKeys = {
                @ForeignKey(entity = Region.class,
                        parentColumns = "idRegion",
                        childColumns = "idRegion",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Commune {
    @NonNull
    @PrimaryKey
    private String idCommune;
    private String nomCommune;
    private String idRegion;
    private String flagTransmis;

    public Commune(@NonNull String idCommune, String nomCommune, String idRegion, String flagTransmis) {
        this.idCommune = idCommune;
        this.nomCommune = nomCommune;
        this.idRegion = idRegion;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdCommune() {
        return idCommune;
    }

    public void setIdCommune(@NonNull String idCommune) {
        this.idCommune = idCommune;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public String getIdRegion() {
        return idRegion;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}

