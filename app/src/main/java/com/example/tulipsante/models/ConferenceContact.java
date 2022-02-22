package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "conferenceContact")
public class ConferenceContact {
    @NonNull
    @PrimaryKey
    private String idConferenceContact;
    private String idMedecin;
    private String nomComplet;
    private String description;
    private String flagTransmis;

    public ConferenceContact(
            @NonNull String idConferenceContact,
            String idMedecin,
            String nomComplet,
            String description,
            String flagTransmis) {
        this.idConferenceContact = idConferenceContact;
        this.idMedecin = idMedecin;
        this.nomComplet = nomComplet;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdConferenceContact() {
        return idConferenceContact;
    }

    public void setIdConferenceContact(@NonNull String idConferenceContact) {
        this.idConferenceContact = idConferenceContact;
    }

    public String getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(String idMedecin) {
        this.idMedecin = idMedecin;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
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
