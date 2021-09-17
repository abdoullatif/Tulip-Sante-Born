package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nouvelles")
public class Nouvelles {
    @NonNull
    @PrimaryKey
    private String idNouvelles;
    private String titre;
    private String contenu;
    private String dateAdded;
    private String flagTransmis;

    public Nouvelles(@NonNull String idNouvelles, String titre, String contenu, String dateAdded, String flagTransmis) {
        this.idNouvelles = idNouvelles;
        this.titre = titre;
        this.contenu = contenu;
        this.dateAdded = dateAdded;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdNouvelles() {
        return idNouvelles;
    }

    public void setIdNouvelles(@NonNull String idNouvelles) {
        this.idNouvelles = idNouvelles;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }
}
