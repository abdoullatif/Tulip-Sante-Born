package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "addresse",
        foreignKeys = {
                @ForeignKey(entity = Commune.class,
                        parentColumns = "idCommune",
                        childColumns = "idCommune",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Addresse {
    @NonNull
    @PrimaryKey
    private String idAddresse;
    private String premiereAddresse;
    private String idCommune;
    private String flagTransmis;

    public Addresse(@NonNull String idAddresse, String premiereAddresse, String idCommune, String flagTransmis) {
        this.idAddresse = idAddresse;
        this.premiereAddresse = premiereAddresse;
        this.idCommune = idCommune;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdAddresse() {
        return idAddresse;
    }

    public void setIdAddresse(@NonNull String idAddresse) {
        this.idAddresse = idAddresse;
    }

    public String getPremiereAddresse() {
        return premiereAddresse;
    }

    public void setIdCommune(String idCommune) {
        this.idCommune = idCommune;
    }

    public String getIdCommune() {
        return idCommune;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
