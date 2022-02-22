package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "region",
        foreignKeys = {
                @ForeignKey(entity = Pays.class,
                        parentColumns = "idPays",
                        childColumns = "idPays",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Region {
    @NonNull
    @PrimaryKey
    private String idRegion;
    private String nomRegion;
    private String idPays;
    private String flagTransmis;

    public Region(@NonNull String idRegion, String nomRegion, String idPays, String flagTransmis) {
        this.idRegion = idRegion;
        this.nomRegion = nomRegion;
        this.idPays = idPays;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(@NonNull String idRegion) {
        this.idRegion = idRegion;
    }

    public String getNomRegion() {
        return nomRegion;
    }

    public String getIdPays() {
        return idPays;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
