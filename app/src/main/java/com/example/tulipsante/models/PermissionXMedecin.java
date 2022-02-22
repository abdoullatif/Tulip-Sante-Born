package com.example.tulipsante.models;

import androidx.annotation.NonNull;

public class PermissionXMedecin extends Permission {
    private String nomMedecin;
    private String prenomMedecin;

    public PermissionXMedecin(
            @NonNull String idPermission,
            String idPatient,
            String idMedecin,
            String dateDemande,
            String dateExpiration,
            String type,
            String chemin,
            String flagTransmis,
            String nomMedecin,
            String prenomMedecin) {
        super(idPermission, idPatient, idMedecin, dateDemande,dateExpiration, type, chemin,flagTransmis);
        this.nomMedecin = nomMedecin;
        this.prenomMedecin = prenomMedecin;
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
