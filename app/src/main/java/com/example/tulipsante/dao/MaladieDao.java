package com.example.tulipsante.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Maladie;

import java.util.List;

@Dao
public interface MaladieDao {
    @Insert
    void insertMaladie(Maladie maladie);

    @Query("SELECT * FROM maladie ORDER BY maladie.description ASC")
    LiveData<List<Maladie>> getAllMaladies();

    @Query("SELECT * FROM maladie ORDER BY maladie.description ASC")
    List<Maladie> getMaladies();

    @Query("SELECT * FROM maladie WHERE maladie.idMaladie IN (:allIds)")
    List<Maladie> maladieList(List<String> allIds);
}
