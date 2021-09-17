package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "conseil")
public class Conseil {
    @NonNull
    @PrimaryKey
    private String idConseil;
    private String idConsultation;
    private String description;
    private String flagTransmis;

    public Conseil() {
    }

    public Conseil(@NonNull String idConseil, String idConsultation, String description, String flagTransmis) {
        this.idConseil = idConseil;
        this.idConsultation = idConsultation;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdConseil() {
        return idConseil;
    }

    public void setIdConseil(@NonNull String idConseil) {
        this.idConseil = idConseil;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
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
