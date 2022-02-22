package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "diagnostic",
        foreignKeys = {
                @ForeignKey(entity = Diagnostic.class,
                        parentColumns = "idDiagnostic",
                        childColumns = "idDiagnostic",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Diagnostic {
    @NonNull
    @PrimaryKey
    private String idDiagnostic;
    private String idMaladie;
    private String idConsultation;
    private String description;
    private String flagTransmis;

    public Diagnostic(
            @NonNull String idDiagnostic,
            String idMaladie,
            String idConsultation,
            String description,
            String flagTransmis) {
        this.idDiagnostic = idDiagnostic;
        this.idMaladie = idMaladie;
        this.idConsultation = idConsultation;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdDiagnostic() {
        return idDiagnostic;
    }

    public void setIdDiagnostic(@NonNull String idDiagnostic) {
        this.idDiagnostic = idDiagnostic;
    }

    public String getIdMaladie() {
        return idMaladie;
    }

    public void setIdMaladie(String idMaladie) {
        this.idMaladie = idMaladie;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
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
