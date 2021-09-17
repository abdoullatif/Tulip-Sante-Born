package com.example.tulipsante.models.uIModels;

import androidx.annotation.NonNull;

import com.example.tulipsante.models.HistoriquePresence;

public class HistoriquePresenceMedecin extends HistoriquePresence {

    private String nomMedecin;
    private String prenomMedecin;

    public HistoriquePresenceMedecin(@NonNull String idPresence, String idMedecin, String dateHistorique, String description, String flagTransmis, String nomMedecin, String prenomMedecin) {
        super(idPresence, idMedecin, dateHistorique, description, flagTransmis);
        this.nomMedecin = nomMedecin;
        this.prenomMedecin = prenomMedecin;
    }

    public HistoriquePresenceMedecin() {
    }

    public String getNomMedecin() {
        return nomMedecin;
    }

    public void setNomMedecin(String nomMedecin) {
        this.nomMedecin = nomMedecin;
    }

    public String getPrenomMedecin() {
        return prenomMedecin;
    }

    public void setPrenomMedecin(String prenomMedecin) {
        this.prenomMedecin = prenomMedecin;
    }
}
