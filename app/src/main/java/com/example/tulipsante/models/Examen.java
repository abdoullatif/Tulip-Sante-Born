package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "examen",
        foreignKeys = {
                @ForeignKey(entity = Consultation.class,
                        parentColumns = "idConsultation",
                        childColumns = "idConsultation",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = TypeExamens.class,
                        parentColumns = "idTypeExamens",
                        childColumns = "idTypeExamens",
                        onDelete = ForeignKey.CASCADE),
        }
)
public class Examen {
    @NonNull
    @PrimaryKey
    private String idExamen;
    private String idConsultation;
    private String idTypeExamens;
    private String valeur;
    private String flagTransmis;

    public Examen(
            @NonNull String idExamen,
            String idConsultation,
            String idTypeExamens,
            String valeur,
            String flagTransmis) {
        this.idExamen = idExamen;
        this.idConsultation = idConsultation;
        this.idTypeExamens = idTypeExamens;
        this.valeur = valeur;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(@NonNull String idExamen) {
        this.idExamen = idExamen;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
    }

    public String getIdTypeExamens() {
        return idTypeExamens;
    }

    public void setIdTypeExamens(String idTypeExamens) {
        this.idTypeExamens = idTypeExamens;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }
}
