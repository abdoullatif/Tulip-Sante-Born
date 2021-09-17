package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contactUrgence")
public class ContactUrgence {
    @NonNull
    @PrimaryKey
    private String idEmergencyContact;
    private String idPersonne;
    private String relation;
    private String nomRelation;
    private String telephoneRelation;
    private String flagTransmis;

    public ContactUrgence(
            @NonNull String idEmergencyContact,
            String idPersonne,
            String relation,
            String nomRelation,
            String telephoneRelation,
            String flagTransmis) {
        this.idEmergencyContact = idEmergencyContact;
        this.idPersonne = idPersonne;
        this.relation = relation;
        this.nomRelation = nomRelation;
        this.telephoneRelation = telephoneRelation;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdEmergencyContact() {
        return idEmergencyContact;
    }

    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    public String getIdPersonne() {
        return idPersonne;
    }

    public String getRelation() {
        return relation;
    }

    public String getNomRelation() {
        return nomRelation;
    }

    public String getTelephoneRelation() {
        return telephoneRelation;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
