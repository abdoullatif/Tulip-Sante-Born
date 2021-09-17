package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "vaccinationPatient",
        foreignKeys = {
                @ForeignKey(entity = Consultation.class,
                        parentColumns = "idConsultation",
                        childColumns = "idConsultation",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = TypeVaccination.class,
                        parentColumns = "idTypeVaccination",
                        childColumns = "idTypeVaccination",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class VaccinationPatient {
    @NonNull
    @PrimaryKey
    private String idVaccinationPatient;
    private String idConsultation;
    private String idTypeVaccination;
    private String dateVaccination;
    private String flagTransmis;

    public VaccinationPatient(
            @NonNull String idVaccinationPatient,
            String idConsultation,
            String idTypeVaccination,
            String dateVaccination,
            String flagTransmis) {
        this.idVaccinationPatient = idVaccinationPatient;
        this.idConsultation = idConsultation;
        this.idTypeVaccination = idTypeVaccination;
        this.dateVaccination = dateVaccination;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdVaccinationPatient() {
        return idVaccinationPatient;
    }

    public void setIdVaccinationPatient(@NonNull String idVaccinationPatient) {
        this.idVaccinationPatient = idVaccinationPatient;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
    }

    public String getIdTypeVaccination() {
        return idTypeVaccination;
    }

    public void setIdTypeVaccination(String idTypeVaccination) {
        this.idTypeVaccination = idTypeVaccination;
    }

    public String getDateVaccination() {
        return dateVaccination;
    }

    public void setDateVaccination(String dateVaccination) {
        this.dateVaccination = dateVaccination;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }
}
