package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "relation",
        foreignKeys = {
                @ForeignKey(entity = Patient.class,
                        parentColumns = "idPatient",
                        childColumns = "idPatient",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Patient.class,
                        parentColumns = "idPatient",
                        childColumns = "idRelationPatient",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Relation {
    @NonNull
    @PrimaryKey
    private String idRelation;
    private String idPatient;
    private String idRelationPatient;
    private String typeRelation;
    private String flagTransmis;

    public Relation(
            @NonNull String idRelation,
            String idPatient,
            String idRelationPatient,
            String typeRelation,
            String flagTransmis) {
        this.idRelation = idRelation;
        this.idPatient = idPatient;
        this.idRelationPatient = idRelationPatient;
        this.typeRelation = typeRelation;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdRelation() {
        return idRelation;
    }

    public void setIdRelation(@NonNull String idRelation) {
        this.idRelation = idRelation;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public String getIdRelationPatient() {
        return idRelationPatient;
    }

    public String getTypeRelation() {
        return typeRelation;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public void setIdRelationPatient(String idRelationPatient) {
        this.idRelationPatient = idRelationPatient;
    }

    public void setTypeRelation(String typeRelation) {
        this.typeRelation = typeRelation;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }
}
