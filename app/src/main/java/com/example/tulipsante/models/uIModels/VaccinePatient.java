package com.example.tulipsante.models.uIModels;

public class VaccinePatient {
    private String type;
    private String dateVaccination;
    private String duree;

    public VaccinePatient(String type, String dateVaccination, String duree) {
        this.type = type;
        this.dateVaccination = dateVaccination;
        this.duree = duree;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateVaccination() {
        return dateVaccination;
    }

    public void setDateVaccination(String dateVaccination) {
        this.dateVaccination = dateVaccination;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }
}
