package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "reference",
        foreignKeys = {
                @ForeignKey(entity = Consultation.class,
                        parentColumns = "idConsultation",
                        childColumns = "idConsultation",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Medecin.class,
                        parentColumns = "idMedecin",
                        childColumns = "idMedecin1",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Medecin.class,
                        parentColumns = "idMedecin",
                        childColumns = "idMedecin2",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Reference {
    @NonNull
    @PrimaryKey
    private String idReference;
    private String idConsultation;
    private String idMedecin1;
    private String idMedecin2;
    private String raison;
    private String typeVisite;
    private String date;
    private String flagTransmis;

    public Reference() {
    }

    public Reference(@NonNull String idReference,
                     String idConsultation,
                     String idMedecin1,
                     String idMedecin2,
                     String raison,
                     String typeVisite,
                     String date,
                     String flagTransmis) {
        this.idReference = idReference;
        this.idConsultation = idConsultation;
        this.idMedecin1 = idMedecin1;
        this.idMedecin2 = idMedecin2;
        this.raison = raison;
        this.typeVisite = typeVisite;
        this.date = date;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdReference() {
        return idReference;
    }

    public void setIdReference(@NonNull String idReference) {
        this.idReference = idReference;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
    }

    public String getIdMedecin1() {
        return idMedecin1;
    }

    public void setIdMedecin1(String idMedecin1) {
        this.idMedecin1 = idMedecin1;
    }

    public String getIdMedecin2() {
        return idMedecin2;
    }

    public void setIdMedecin2(String idMedecin2) {
        this.idMedecin2 = idMedecin2;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getTypeVisite() {
        return typeVisite;
    }

    public void setTypeVisite(String typeVisite) {
        this.typeVisite = typeVisite;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }
}
