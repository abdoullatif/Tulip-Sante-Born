package com.example.tulipsante.models.uIModels;

import androidx.annotation.NonNull;

import com.example.tulipsante.models.Consultation;

public class ConsultationUMedecin extends Consultation {
    private String nomMedecin;
    private String prenomMedecin;

    public ConsultationUMedecin(@NonNull String idConsultation, String dateConsultation, String idPatient, String idMedecin, String description, String flagTransmis, String nomMedecin, String prenomMedecin) {
        super(idConsultation, dateConsultation, idPatient, idMedecin, description, flagTransmis);
        this.nomMedecin = nomMedecin;
        this.prenomMedecin = prenomMedecin;
    }

    public ConsultationUMedecin() {
        super();
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
