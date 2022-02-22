package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "antecedentPatient",
        foreignKeys = {
                @ForeignKey(entity = Patient.class,
                        parentColumns = "idPatient",
                        childColumns = "idPatient",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Maladie.class,
                        parentColumns = "idMaladie",
                        childColumns = "idMaladie",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class AntecedentPatient {
    @NonNull
    @PrimaryKey
    private String idAntecedentPatient;
    private String idMaladie;
    private String idPatient;
    private String flagTransmis;

    public AntecedentPatient(@NonNull String idAntecedentPatient, String idMaladie, String idPatient, String flagTransmis) {
        this.idAntecedentPatient = idAntecedentPatient;
        this.idMaladie = idMaladie;
        this.idPatient = idPatient;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdAntecedentPatient() {
        return idAntecedentPatient;
    }

    public void setIdAntecedentPatient(@NonNull String idAntecedentPatient) {
        this.idAntecedentPatient = idAntecedentPatient;
    }

    public String getIdMaladie() {
        return idMaladie;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
