package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "consultation",
        foreignKeys ={
                @ForeignKey(entity = Patient.class,
                        parentColumns = "idPatient",
                        childColumns = "idPatient",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Medecin.class,
                        parentColumns = "idMedecin",
                        childColumns = "idMedecin",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Consultation {
    @NonNull
    @PrimaryKey
    private String idConsultation;
    private String dateConsultation;
    private String idPatient;
    private String idMedecin;
    private String description;
    private String flagTransmis;

    public Consultation(
            @NonNull String idConsultation,
            String dateConsultation,
            String idPatient,
            String idMedecin,
            String description,
            String flagTransmis) {
        this.idConsultation = idConsultation;
        this.dateConsultation = dateConsultation;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    public Consultation() {

    }

    @NonNull
    public String getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(@NonNull String idConsultation) {
        this.idConsultation = idConsultation;
    }

    public String getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(String dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(String idMedecin) {
        this.idMedecin = idMedecin;
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
