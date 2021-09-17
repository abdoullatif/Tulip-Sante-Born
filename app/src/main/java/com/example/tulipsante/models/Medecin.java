package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "medecin"
        , foreignKeys = {
        @ForeignKey(entity = Specialite.class,
                parentColumns = "idSpecialite",
                childColumns = "idSpecialite",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Departement.class,
                parentColumns = "idDepartement",
                childColumns = "idDepartement",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Addresse.class,
                parentColumns = "idAddresse",
                childColumns = "idAddresse",
                onDelete = ForeignKey.CASCADE)

}
)
public class Medecin {

    @NonNull
    @PrimaryKey
    private String idMedecin;
    private String idSpecialite;
    private String idDepartement;
    private String nomMedecin;
    private String prenomMedecin;
    private String genreMedecin;
    private String dateDeNaissance;
    private String uidMedecin;
    private String flagTransmis;
    private String photo;
    private String idAddresse;
    private String statusMatrimonialMedecin;

    public Medecin(@NonNull String idMedecin, String idSpecialite, String idDepartement, String nomMedecin, String prenomMedecin, String genreMedecin, String dateDeNaissance, String uidMedecin, String flagTransmis, String photo, String idAddresse, String statusMatrimonialMedecin) {
        this.idMedecin = idMedecin;
        this.idSpecialite = idSpecialite;
        this.idDepartement = idDepartement;
        this.nomMedecin = nomMedecin;
        this.prenomMedecin = prenomMedecin;
        this.genreMedecin = genreMedecin;
        this.dateDeNaissance = dateDeNaissance;
        this.uidMedecin = uidMedecin;
        this.flagTransmis = flagTransmis;
        this.photo = photo;
        this.idAddresse = idAddresse;
        this.statusMatrimonialMedecin = statusMatrimonialMedecin;
    }

    @NonNull
    public String getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(@NonNull String idMedecin) {
        this.idMedecin = idMedecin;
    }

    public String getIdSpecialite() {
        return idSpecialite;
    }

    public void setIdSpecialite(String idSpecialite) {
        this.idSpecialite = idSpecialite;
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }

    public String getNomMedecin() {
        return nomMedecin;
    }

    public void setNomMedecin(String nomMedecin) {
        this.nomMedecin = nomMedecin;
    }

    public String getPrenomMedecin() {
        return prenomMedecin;
    }

    public void setPrenomMedecin(String prenomMedecin) {
        this.prenomMedecin = prenomMedecin;
    }

    public String getGenreMedecin() {
        return genreMedecin;
    }

    public void setGenreMedecin(String genreMedecin) {
        this.genreMedecin = genreMedecin;
    }

    public String getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(String dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getUidMedecin() {
        return uidMedecin;
    }

    public void setUidMedecin(String uidMedecin) {
        this.uidMedecin = uidMedecin;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIdAddresse() {
        return idAddresse;
    }

    public void setIdAddresse(String idAddresse) {
        this.idAddresse = idAddresse;
    }

    public String getStatusMatrimonialMedecin() {
        return statusMatrimonialMedecin;
    }

    public void setStatusMatrimonialMedecin(String statusMatrimonialMedecin) {
        this.statusMatrimonialMedecin = statusMatrimonialMedecin;
    }
}
