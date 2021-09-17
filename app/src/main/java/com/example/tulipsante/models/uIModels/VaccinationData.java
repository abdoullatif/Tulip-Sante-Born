package com.example.tulipsante.models.uIModels;

public class VaccinationData {
    private String type;
    private String dateVaccination;

    public VaccinationData(String type, String dateVaccination) {
        this.type = type;
        this.dateVaccination = dateVaccination;
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
}
