package com.example.tulipsante.models.uIModels;

public class SignesVitauxXCatSignesVitaux {
    private String description;
    private String valeur;

    public SignesVitauxXCatSignesVitaux(String description, String valeur) {
        this.description = description;
        this.valeur = valeur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
}
