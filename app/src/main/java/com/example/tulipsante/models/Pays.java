package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pays")
public class Pays {
    @NonNull
    @PrimaryKey
    private String idPays;
    private String nomPays;
    private String flagTransmis;

    public Pays(@NonNull String idPays, String nomPays, String flagTransmis) {
        this.idPays = idPays;
        this.nomPays = nomPays;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdPays() {
        return idPays;
    }

    public void setIdPays(@NonNull String idPays) {
        this.idPays = idPays;
    }

    public String getNomPays() {
        return nomPays;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
