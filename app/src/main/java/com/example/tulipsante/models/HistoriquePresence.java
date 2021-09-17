package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "historiquePresence",
        foreignKeys = {
                @ForeignKey(entity = Medecin.class,
                        parentColumns = "idMedecin",
                        childColumns = "idMedecin",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class HistoriquePresence {
    @NonNull
    @PrimaryKey
    private String idPresence;
    private String idMedecin;
    private String dateHistorique;
    private String description;
    private String flagTransmis;

    public HistoriquePresence(@NonNull String idPresence, String idMedecin, String dateHistorique, String description, String flagTransmis) {
        this.idPresence = idPresence;
        this.idMedecin = idMedecin;
        this.dateHistorique = dateHistorique;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    public HistoriquePresence() {
    }

    @NonNull
    public String getIdPresence() {
        return idPresence;
    }

    public void setIdPresence(@NonNull String idPresence) {
        this.idPresence = idPresence;
    }

    public String getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(String idMedecin) {
        this.idMedecin = idMedecin;
    }

    public String getDateHistorique() {
        return dateHistorique;
    }

    public void setDateHistorique(String dateHistorique) {
        this.dateHistorique = dateHistorique;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }
}
