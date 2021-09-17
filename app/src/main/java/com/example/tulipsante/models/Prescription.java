package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "prescription",
        foreignKeys = {
                @ForeignKey(entity = Consultation.class,
                        parentColumns = "idConsultation",
                        childColumns = "idConsultation",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Prescription {
    @NonNull
    @PrimaryKey
    private String idPrescription;
    private String idConsultation;
    private String datePrescription;
    private String description;
    private String flagTransmis;

    public Prescription() {
    }

    public Prescription(
            @NonNull String idPrescription,
            String idConsultation,
            String datePrescription,
            String description,
            String flagTransmis) {
        this.idPrescription = idPrescription;
        this.idConsultation = idConsultation;
        this.datePrescription = datePrescription;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdPrescription() {
        return idPrescription;
    }

    public void setIdPrescription(@NonNull String idPrescription) {
        this.idPrescription = idPrescription;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
    }

    public String getDatePrescription() {
        return datePrescription;
    }

    public void setDatePrescription(String datePrescription) {
        this.datePrescription = datePrescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }
}
