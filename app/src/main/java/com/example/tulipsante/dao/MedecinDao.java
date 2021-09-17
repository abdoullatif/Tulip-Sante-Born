package com.example.tulipsante.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Medecin;

import java.util.List;

@Dao
public interface MedecinDao {

    @Insert
    void insert(Medecin medecin);

    @Query("SELECT * FROM medecin WHERE medecin.idMedecin LIKE :idMedecin ")
    Medecin getMedecin(String idMedecin);

    @Query("SELECT COUNT(uidMedecin) FROM medecin WHERE medecin.uidMedecin LIKE :uidMedecin ")
    String checkTag(String uidMedecin);

    @Query("SELECT idMedecin FROM medecin WHERE medecin.uidMedecin LIKE :uidMedecin ")
    String idMedecin(String uidMedecin);

    @Query("SELECT * FROM medecin WHERE medecin.nomMedecin LIKE :nomMedecin OR medecin.prenomMedecin LIKE :nomMedecin")
    LiveData<List<Medecin>> getMedecinList(String nomMedecin);
}
