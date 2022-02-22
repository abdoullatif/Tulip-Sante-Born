package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "typeExamens")
public class TypeExamens {
    @NonNull
    @PrimaryKey
    private String idTypeExamens;
    private String typeExamens;
    private String description;
    private String flagTransmis;

    public TypeExamens() {
    }

    public TypeExamens(
            @NonNull String idTypeExamens,
            String typeExamens,
            String description,
            String flagTransmis) {
        this.idTypeExamens = idTypeExamens;
        this.typeExamens = typeExamens;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdTypeExamens() {
        return idTypeExamens;
    }

    public void setIdTypeExamens(@NonNull String idTypeExamens) {
        this.idTypeExamens = idTypeExamens;
    }

    public String getTypeExamens() {
        return typeExamens;
    }

    public void setTypeExamens(String typeExamens) {
        this.typeExamens = typeExamens;
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
