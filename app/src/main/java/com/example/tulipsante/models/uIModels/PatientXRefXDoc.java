package com.example.tulipsante.models.uIModels;

import androidx.annotation.NonNull;

import com.example.tulipsante.models.Patient;

public class PatientXRefXDoc extends Patient {
    private String raison;
    private String typeVisite;
    private String date;
    private String nomMedecin;
    private String prenomMedecin;
    private String photo;

    public PatientXRefXDoc(@NonNull String idPatient, String nomPatient, String prenomPatient, String genrePatient, String dateNaissancePatient, String groupeSanguinPatient, String photoPatient, String numeroIdentitePatient, String uidPatient, String nationalitePatient, String statusMatrimonialPatient, String idAddresse, String dateRegistration, String flagTransmis, String raison, String typeVisite, String date, String nomMedecin, String prenomMedecin, String photo) {
        super(idPatient, nomPatient, prenomPatient, genrePatient, dateNaissancePatient, groupeSanguinPatient, photoPatient, numeroIdentitePatient, uidPatient, nationalitePatient, statusMatrimonialPatient, idAddresse, dateRegistration, flagTransmis);
        this.raison = raison;
        this.typeVisite = typeVisite;
        this.date = date;
        this.nomMedecin = nomMedecin;
        this.prenomMedecin = prenomMedecin;
        this.photo = photo;
    }

    public String getRaison() {
        return raison;
    }

    public String getTypeVisite() {
        return typeVisite;
    }

    public String getDate() {
        return date;
    }

    public String getNomMedecin() {
        return nomMedecin;
    }

    public String getPrenomMedecin() {
        return prenomMedecin;
    }

    public String getPhoto() {
        return photo;
    }
}
