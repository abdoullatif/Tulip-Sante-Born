package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "allergiePatient",
        foreignKeys = {
                @ForeignKey(entity = Patient.class,
                        parentColumns = "idPatient",
                        childColumns = "idPatient",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Allergie.class,
                        parentColumns = "idAllergie",
                        childColumns = "idAllergie",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class AllergiePatient {
    @NonNull
    @PrimaryKey
    private String idAllergiePatient;
    private String idAllergie;
    private String idPatient;
    private String flagTransmis;

    public AllergiePatient(@NonNull String idAllergiePatient, String idAllergie, String idPatient, String flagTransmis) {
        this.idAllergiePatient = idAllergiePatient;
        this.idAllergie = idAllergie;
        this.idPatient = idPatient;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdAllergiePatient() {
        return idAllergiePatient;
    }

    public void setIdAllergiePatient(@NonNull String idAllergiePatient) {
        this.idAllergiePatient = idAllergiePatient;
    }

    public String getIdAllergie() {
        return idAllergie;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
