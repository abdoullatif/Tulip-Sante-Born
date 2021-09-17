package com.example.tulipsante.models.uIModels;

public class DashPatientProfileData {
    private String dateConsultation;
    private String valeur;

    public DashPatientProfileData(String dateConsultation, String valeur) {
        this.dateConsultation = dateConsultation;
        this.valeur = valeur;
    }

    public String getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(String dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
}
