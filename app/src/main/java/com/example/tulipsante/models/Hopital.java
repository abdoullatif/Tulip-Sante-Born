package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hopital")
public class Hopital {
    @NonNull
    @PrimaryKey
    private String idHopital;
    private String nomHopital;
    private String idAddresse;
    private String flagTransmis;

    public Hopital(@NonNull String idHopital, String nomHopital, String idAddresse, String flagTransmis) {
        this.idHopital = idHopital;
        this.nomHopital = nomHopital;
        this.idAddresse = idAddresse;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdHopital() {
        return idHopital;
    }

    public void setIdHopital(@NonNull String idHopital) {
        this.idHopital = idHopital;
    }

    public String getNomHopital() {
        return nomHopital;
    }

    public String getIdAddresse() {
        return idAddresse;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
