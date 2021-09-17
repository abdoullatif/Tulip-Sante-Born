package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "symptomesPatient",
        foreignKeys = {
                @ForeignKey(entity = Symptome.class,
                        parentColumns = "idSymptome",
                        childColumns = "idSymptome",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Consultation.class,
                        parentColumns = "idConsultation",
                        childColumns = "idConsultation",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class SymptomesPatient {
    @NonNull
    @PrimaryKey
    private String idSymptomesPatient;
    private String idSymptome;
    private String idConsultation;
    private String flagTransmis;

    public SymptomesPatient(
            @NonNull String idSymptomesPatient,
            String idSymptome,
            String idConsultation,
            String flagTransmis) {
        this.idSymptomesPatient = idSymptomesPatient;
        this.idSymptome = idSymptome;
        this.idConsultation = idConsultation;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdSymptomesPatient() {
        return idSymptomesPatient;
    }

    public void setIdSymptomesPatient(@NonNull String idSymptomesPatient) {
        this.idSymptomesPatient = idSymptomesPatient;
    }

    public void setIdSymptome(String idSymptome) {
        this.idSymptome = idSymptome;
    }

    public String getIdSymptome() {
        return idSymptome;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
