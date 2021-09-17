package com.example.tulipsante.models.uIModels;

import androidx.annotation.NonNull;

import com.example.tulipsante.models.Consultation;

public class HistoriqueTacheMedecin extends Consultation {
    private String nomPatient;
    private String prenomPatient;

    private String genrePatient;
    private String dateNaissancePatient;
    private String groupeSanguinPatient;
    private String photoPatient;

    public HistoriqueTacheMedecin(@NonNull String idConsultation, String dateConsultation, String idPatient, String idMedecin, String description, String flagTransmis, String nomPatient, String prenomPatient, String genrePatient, String dateNaissancePatient, String groupeSanguinPatient, String photoPatient) {
        super(idConsultation, dateConsultation, idPatient, idMedecin, description, flagTransmis);
        this.nomPatient = nomPatient;
        this.prenomPatient = prenomPatient;
        this.genrePatient = genrePatient;
        this.dateNaissancePatient = dateNaissancePatient;
        this.groupeSanguinPatient = groupeSanguinPatient;
        this.photoPatient = photoPatient;
    }

    public HistoriqueTacheMedecin() {
    }

    public String getNomPatient() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient = nomPatient;
    }

    public String getPrenomPatient() {
        return prenomPatient;
    }

    public void setPrenomPatient(String prenomPatient) {
        this.prenomPatient = prenomPatient;
    }

    public String getGenrePatient() {
        return genrePatient;
    }

    public void setGenrePatient(String genrePatient) {
        this.genrePatient = genrePatient;
    }

    public String getDateNaissancePatient() {
        return dateNaissancePatient;
    }

    public void setDateNaissancePatient(String dateNaissancePatient) {
        this.dateNaissancePatient = dateNaissancePatient;
    }

    public String getGroupeSanguinPatient() {
        return groupeSanguinPatient;
    }

    public void setGroupeSanguinPatient(String groupeSanguinPatient) {
        this.groupeSanguinPatient = groupeSanguinPatient;
    }

    public String getPhotoPatient() {
        return photoPatient;
    }

    public void setPhotoPatient(String photoPatient) {
        this.photoPatient = photoPatient;
    }
}
