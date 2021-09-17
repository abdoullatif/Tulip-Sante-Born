package com.example.tulipsante.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Allergie;

import java.util.List;

@Dao
public interface AllergieDao {

    @Insert
    void insertAllergie(Allergie allergie);

    @Query("SELECT * FROM allergie WHERE allergie.type LIKE :typeAllergie ORDER BY allergie.description ASC")
    LiveData<List<Allergie>> getAllAllergies(String typeAllergie);

    @Query("SELECT * FROM allergie WHERE allergie.idAllergie IN (:allIds) AND allergie.type LIKE :type")
    List<Allergie> getPatientAllergies(List<String> allIds, String type);

}
