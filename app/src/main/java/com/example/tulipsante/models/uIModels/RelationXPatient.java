package com.example.tulipsante.models.uIModels;

import androidx.annotation.NonNull;

import com.example.tulipsante.models.Patient;

public class RelationXPatient extends Patient {
    private String idRelation;
    private String typeRelation;

    public RelationXPatient(
            @NonNull String idPatient,
            String nomPatient,
            String prenomPatient,
            String genrePatient,
            String dateNaissancePatient,
            String groupeSanguinPatient,
            String photoPatient,
            String numeroIdentitePatient,
            String uidPatient,
            String nationalitePatient,
            String statusMatrimonialPatient,
            String idAddresse,
            String dateRegistration,
            String flagTransmis,
            String idRelation,
            String typeRelation) {
        super(
                idPatient,
                nomPatient,
                prenomPatient,
                genrePatient,
                dateNaissancePatient,
                groupeSanguinPatient,
                photoPatient,
                numeroIdentitePatient,
                uidPatient,
                nationalitePatient,
                statusMatrimonialPatient,
                idAddresse,
                dateRegistration,
                flagTransmis);
        this.idRelation = idRelation;
        this.typeRelation = typeRelation;
    }

    public String getIdRelation() {
        return idRelation;
    }

    public void setIdRelation(String idRelation) {
        this.idRelation = idRelation;
    }

    public String getTypeRelation() {
        return typeRelation;
    }

    public void setTypeRelation(String typeRelation) {
        this.typeRelation = typeRelation;
    }
}
