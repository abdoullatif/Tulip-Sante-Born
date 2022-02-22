package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "signesVitaux",
        foreignKeys = {
                @ForeignKey(entity = CategorieSigneVitaux.class,
                        parentColumns = "idCatSV",
                        childColumns = "idCatSV",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Consultation.class,
                        parentColumns = "idConsultation",
                        childColumns = "idConsultation",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class SignesVitaux {
    @NonNull
    @PrimaryKey
    private String idSignesVitaux;
    private String idCatSV;
    private String idConsultation;
    private String valeur;
    private String flagTransmis;

    public SignesVitaux(
            @NonNull String idSignesVitaux,
            String idCatSV,
            String idConsultation,
            String valeur,
            String flagTransmis) {
        this.idSignesVitaux = idSignesVitaux;
        this.idCatSV = idCatSV;
        this.idConsultation = idConsultation;
        this.valeur = valeur;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdSignesVitaux() {
        return idSignesVitaux;
    }

    public void setIdSignesVitaux(@NonNull String idSignesVitaux) {
        this.idSignesVitaux = idSignesVitaux;
    }
    public String getIdCatSV() {
        return idCatSV;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public String getValeur() {
        return valeur;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
