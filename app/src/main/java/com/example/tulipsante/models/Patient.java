package com.example.tulipsante.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "patient",
        foreignKeys = {
                @ForeignKey(entity = Addresse.class,
                        parentColumns = "idAddresse",
                        childColumns = "idAddresse",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Patient implements Parcelable {
    @NonNull
    @PrimaryKey
    private String idPatient;
    private String nomPatient;
    private String prenomPatient;
    private String genrePatient;
    private String dateNaissancePatient;
    private String groupeSanguinPatient;
    private String photoPatient;
    private String numeroIdentitePatient;
    private String uidPatient;
    private String nationalitePatient;
    private String statusMatrimonialPatient;
    private String idAddresse;
    private String dateRegistration;
    private String flagTransmis;


    public Patient(
            @NonNull String idPatient,
            String nomPatient,
            String prenomPatient,
            String genrePatient,
            String dateNaissancePatient,
            String groupeSanguinPatient,
            String photoPatient,
            String numeroIdentitePatient,
            String uidPatient,
            String nationalitePatient,
            String statusMatrimonialPatient,
            String idAddresse,
            String dateRegistration,
            String flagTransmis) {
        this.idPatient = idPatient;
        this.nomPatient = nomPatient;
        this.prenomPatient = prenomPatient;
        this.genrePatient = genrePatient;
        this.dateNaissancePatient = dateNaissancePatient;
        this.groupeSanguinPatient = groupeSanguinPatient;
        this.photoPatient = photoPatient;
        this.numeroIdentitePatient = numeroIdentitePatient;
        this.uidPatient = uidPatient;
        this.nationalitePatient = nationalitePatient;
        this.statusMatrimonialPatient = statusMatrimonialPatient;
        this.idAddresse = idAddresse;
        this.dateRegistration = dateRegistration;
        this.flagTransmis = flagTransmis;
    }

    public Patient() {
    }

    @NonNull
    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(@NonNull String idPatient) {
        this.idPatient = idPatient;
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

    public String getNumeroIdentitePatient() {
        return numeroIdentitePatient;
    }

    public void setNumeroIdentitePatient(String numeroIdentitePatient) {
        this.numeroIdentitePatient = numeroIdentitePatient;
    }

    public String getUidPatient() {
        return uidPatient;
    }

    public void setUidPatient(String uidPatient) {
        this.uidPatient = uidPatient;
    }

    public String getNationalitePatient() {
        return nationalitePatient;
    }

    public void setNationalitePatient(String nationalitePatient) {
        this.nationalitePatient = nationalitePatient;
    }

    public String getStatusMatrimonialPatient() {
        return statusMatrimonialPatient;
    }

    public void setStatusMatrimonialPatient(String statusMatrimonialPatient) {
        this.statusMatrimonialPatient = statusMatrimonialPatient;
    }

    public String getIdAddresse() {
        return idAddresse;
    }

    public void setIdAddresse(String idAddresse) {
        this.idAddresse = idAddresse;
    }

    public String getDateRegistration() {
        return dateRegistration;
    }

    public void setDateRegistration(String dateRegistration) {
        this.dateRegistration = dateRegistration;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }

    public void setFlagTransmis(String flagTransmis) {
        this.flagTransmis = flagTransmis;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idPatient);
        parcel.writeString(nomPatient);
        parcel.writeString(prenomPatient);
        parcel.writeString(genrePatient);
        parcel.writeString(dateNaissancePatient);
        parcel.writeString(groupeSanguinPatient);
        parcel.writeString(photoPatient);
        parcel.writeString(numeroIdentitePatient);
        parcel.writeString(uidPatient);
        parcel.writeString(nationalitePatient);
        parcel.writeString(statusMatrimonialPatient);
        parcel.writeString(idAddresse);
        parcel.writeString(dateRegistration);
        parcel.writeString(flagTransmis);
    }

    public Patient(Parcel parcel) {
        idPatient = parcel.readString();
        nomPatient = parcel.readString();
        prenomPatient = parcel.readString();
        genrePatient = parcel.readString();
        dateNaissancePatient = parcel.readString();
        groupeSanguinPatient = parcel.readString();
        photoPatient = parcel.readString();
        numeroIdentitePatient = parcel.readString();
        uidPatient = parcel.readString();
        nationalitePatient = parcel.readString();
        statusMatrimonialPatient = parcel.readString();
        idAddresse = parcel.readString();
        dateRegistration = parcel.readString();
        flagTransmis = parcel.readString();

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Patient createFromParcel(Parcel parcel) {
            return new Patient(parcel);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };
}



//    insert into patient(
//    idPatient,
//    nomPatient,
//    prenomPatient,
//    genrePatient,
//    dateNaissancePatient,
//    groupeSanguinPatient,
//    photoPatient,
//
//                       numeroIdentitePatient,uidPatient, nationalitePatient, statusMatrimonialPatient, idAddresse, dateRegistration, flagTransmis) values (
//        '1001', 'fode','oussou','Male','1999/04/09','B+','photo.jpg',' ',' ',' ', ' ','idAddresse','2021/01/01',' '
//        );