package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "permission",
        foreignKeys = {
                @ForeignKey(entity = Patient.class,
                        parentColumns = "idPatient",
                        childColumns = "idPatient",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Medecin.class,
                        parentColumns = "idMedecin",
                        childColumns = "idMedecin",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Permission {
    @NonNull
    @PrimaryKey
    private String idPermission;
    private String idPatient;
    private String idMedecin;
    private String dateDemande;
    private String dateExpiration;
    private String type;
    private String chemin;
    private String flagTransmis;

    public Permission(
            @NonNull String idPermission,
            String idPatient,
            String idMedecin,
            String dateDemande,
            String dateExpiration,
            String type,
            String chemin,
            String flagTransmis) {
        this.idPermission = idPermission;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
        this.dateDemande = dateDemande;
        this.dateExpiration = dateExpiration;
        this.type = type;
        this.chemin = chemin;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdPermission() {
        return idPermission;
    }

    public void setIdPermission(@NonNull String idPermission) {
        this.idPermission = idPermission;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public String getIdMedecin() {
        return idMedecin;
    }

    public String getDateDemande() {
        return dateDemande;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public String getType() {
        return type;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getChemin() {
        return chemin;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
