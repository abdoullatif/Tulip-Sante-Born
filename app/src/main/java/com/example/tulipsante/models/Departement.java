package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "departement",
        foreignKeys = {
                @ForeignKey(entity = Hopital.class,
                        parentColumns = "idHopital",
                        childColumns = "idHopital",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Departement {
    @NonNull
    @PrimaryKey
    private String idDepartement;
    private String description;
    private String idHopital;
    private String flagTransmis;

    public Departement(@NonNull String idDepartement, String description, String idHopital, String flagTransmis) {
        this.idDepartement = idDepartement;
        this.description = description;
        this.idHopital = idHopital;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(@NonNull String idDepartement) {
        this.idDepartement = idDepartement;
    }

    public String getDescription() {
        return description;
    }

    public String getIdHopital() {
        return idHopital;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
