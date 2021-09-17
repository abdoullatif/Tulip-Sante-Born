package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "utilisateur",
        foreignKeys = @ForeignKey(entity = Medecin.class,
                parentColumns = "idMedecin",
                childColumns = "idMedecin",
                onDelete = ForeignKey.CASCADE)
)
public class Utilisateur {
    @NonNull
    @PrimaryKey
    private String idUser;
    private String idMedecin;
    private String username;
    private String password;
    private String email;
    private String flagTransmis;

    public Utilisateur(@NonNull String idUser, String idMedecin, String username, String password, String email, String flagTransmis) {
        this.idUser = idUser;
        this.idMedecin = idMedecin;
        this.username = username;
        this.password = password;
        this.email = email;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(@NonNull String idUser) {
        this.idUser = idUser;
    }

    public String getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(String idMedecin) {
        this.idMedecin = idMedecin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }
}
