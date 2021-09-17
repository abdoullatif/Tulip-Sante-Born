package com.example.tulipsante.models.uIModels;

public class VaccineTableDialog {
    private String dateVaccination;
    private String idConsultation;
    private String type;

    public VaccineTableDialog(String dateVaccination, String idConsultation, String type) {
        this.dateVaccination = dateVaccination;
        this.idConsultation = idConsultation;
        this.type = type;
    }

    public String getDateVaccination() {
        return dateVaccination;
    }

    public void setDateVaccination(String dateVaccination) {
        this.dateVaccination = dateVaccination;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
