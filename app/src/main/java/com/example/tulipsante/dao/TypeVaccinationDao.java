package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.TypeVaccination;

import java.util.List;

@Dao
public interface TypeVaccinationDao {
    @Insert
    void insertTypeVaccination(TypeVaccination typeVaccination);

    @Query("SELECT * FROM typeVaccination")
    List<TypeVaccination> typeVaccinationList();
}
