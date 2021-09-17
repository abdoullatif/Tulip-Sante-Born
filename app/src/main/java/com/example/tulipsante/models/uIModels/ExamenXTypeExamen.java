package com.example.tulipsante.models.uIModels;

public class ExamenXTypeExamen {
    private String typeExamens;
    private String valeur;

    public ExamenXTypeExamen(String typeExamens, String valeur) {
        this.typeExamens = typeExamens;
        this.valeur = valeur;
    }

    public String getTypeExamens() {
        return typeExamens;
    }

    public void setTypeExamens(String typeExamens) {
        this.typeExamens = typeExamens;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
}
