package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "vicePatient",
        foreignKeys = {
                @ForeignKey(entity = Patient.class,
                        parentColumns = "idPatient",
                        childColumns = "idPatient",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Vice.class,
                        parentColumns = "idVice",
                        childColumns = "idVice",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class VicePatient {
    @NonNull
    @PrimaryKey
    private String idVicePatient;
    private String idVice;
    private String idPatient;
    private String flagTransmis;

    public VicePatient(@NonNull String idVicePatient, String idVice, String idPatient, String flagTransmis) {
        this.idVicePatient = idVicePatient;
        this.idVice = idVice;
        this.idPatient = idPatient;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdVicePatient() {
        return idVicePatient;
    }

    public void setIdVicePatient(@NonNull String idVicePatient) {
        this.idVicePatient = idVicePatient;
    }

    public String getIdVice() {
        return idVice;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
