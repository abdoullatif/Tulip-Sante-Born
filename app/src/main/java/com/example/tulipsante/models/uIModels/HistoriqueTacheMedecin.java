package com.example.tulipsante.models.uIModels;

import androidx.annotation.NonNull;

import com.example.tulipsante.models.Consultation;

public class HistoriqueTacheMedecin extends Consultation {
    private String nomPatient;
    private String prenomPatient;

    public HistoriqueTacheMedecin(@NonNull String idConsultation, String dateConsultation, String idPatient, String idMedecin, String description, String flagTransmis, String nomPatient, String prenomPatient) {
        super(idConsultation, dateConsultation, idPatient, idMedecin, description, flagTransmis);
        this.nomPatient = nomPatient;
        this.prenomPatient = prenomPatient;
    }

    public HistoriqueTacheMedecin() {
    }

    public String getNomPatient() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient = nomPatient;
    }

    public String getPrenomPatient() {
        return prenomPatient;
    }

    public void setPrenomPatient(String prenomPatient) {
        this.prenomPatient = prenomPatient;
    }
}
