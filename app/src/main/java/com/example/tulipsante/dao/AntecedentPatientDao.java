package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.AntecedentPatient;

import java.util.List;

@Dao
public interface AntecedentPatientDao {

    @Insert
    void insertAntecedentPatient(AntecedentPatient antecedentPatient);

    @Delete
    void deleteAntecedentPatient(AntecedentPatient antecedentPatient);

    @Query("SELECT * FROM antecedentPatient WHERE antecedentPatient.idPatient LIKE :idPatient")
    List<AntecedentPatient> getAntecedentPatient(String idPatient);

    @Query("SELECT idMaladie FROM antecedentPatient WHERE antecedentPatient.idPatient LIKE :idPatient")
    List<String> getPatientIdMaladie(String idPatient);


}
