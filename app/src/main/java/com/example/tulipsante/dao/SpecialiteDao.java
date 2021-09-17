package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Specialite;

@Dao
public interface SpecialiteDao {

    @Insert
    void insert(Specialite specialite);

    @Query("SELECT * FROM specialite WHERE specialite.idSpecialite LIKE :idSpecialite")
    Specialite getSpecialite(String idSpecialite);

}
