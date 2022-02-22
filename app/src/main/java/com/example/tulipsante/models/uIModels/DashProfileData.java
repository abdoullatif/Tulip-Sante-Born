package com.example.tulipsante.models.uIModels;

import androidx.room.ColumnInfo;

public class DashProfileData {
    @ColumnInfo(name = "COUNT(*)")
    private String numPatient;
    private String dateConsultation;

    public DashProfileData(String numPatient, String dateConsultation) {
        this.numPatient = numPatient;
        this.dateConsultation = dateConsultation;
    }

    public String getNumPatient() {
        return numPatient;
    }

    public void setNumPatient(String numPatient) {
        this.numPatient = numPatient;
    }

    public String getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(String dateConsultation) {
        this.dateConsultation = dateConsultation;
    }
}
