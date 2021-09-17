package com.example.tulipsante.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.AllergiePatient;

import java.util.List;

@Dao
public interface AllergiePatientDao {
    @Insert
    void insertAllergiePatient(AllergiePatient allergiePatient);

    @Query("SELECT * FROM allergiePatient WHERE allergiePatient.idPatient LIKE :typeAllergie ORDER BY allergiePatient.idPatient ASC")
    LiveData<List<AllergiePatient>> getAllAllergies(String typeAllergie);

    @Query("SELECT idAllergie FROM allergiePatient WHERE allergiePatient.idPatient LIKE :idPatient")
    List<String> getAllergiesPatient(String idPatient);
}
