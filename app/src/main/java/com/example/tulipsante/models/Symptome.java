package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "symptome",
        foreignKeys = {
                @ForeignKey(entity = Departement.class,
                        parentColumns = "idDepartement",
                        childColumns = "idDepartement",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Symptome {
    @NonNull
    @PrimaryKey
    private String idSymptome;
    private String idDepartement;
    private String description;
    private String flagTransmis;

    public Symptome(@NonNull String idSymptome, String idDepartement, String description, String flagTransmis) {
        this.idSymptome = idSymptome;
        this.idDepartement = idDepartement;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdSymptome() {
        return idSymptome;
    }

    public void setIdSymptome(@NonNull String idSymptome) {
        this.idSymptome = idSymptome;
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public String getDescription() {
        return description;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    @Override
    public String toString() {
        return "(" + idDepartement + ") " + description;
    }
}
