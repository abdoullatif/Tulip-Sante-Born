package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact")
public class Contact {
    @NonNull
    @PrimaryKey
    private String idContact;
    private String idPersonne;
    private String telephoneContact;
    private String telephoneUrgence;
    private String email;
    private String flagTransmis;

    public Contact(
            @NonNull String idContact,
            String idPersonne,
            String telephoneContact,
            String telephoneUrgence,
            String email,
            String flagTransmis) {
        this.idContact = idContact;
        this.idPersonne = idPersonne;
        this.telephoneContact = telephoneContact;
        this.telephoneUrgence = telephoneUrgence;
        this.email = email;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdContact() {
        return idContact;
    }

    public void setIdContact(@NonNull String idContact) {
        this.idContact = idContact;
    }

    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    public String getIdPersonne() {
        return idPersonne;
    }

    public String getTelephoneContact() {
        return telephoneContact;
    }

    public String getTelephoneUrgence() {
        return telephoneUrgence;
    }

    public String getEmail() {
        return email;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

}
