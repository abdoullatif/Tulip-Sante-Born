package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.CategorieSigneVitaux;

import java.util.List;

@Dao
public interface CategorieSigneVitauxDao {
    @Insert
    void insertCatSignesVitaux(CategorieSigneVitaux caterorieSigneVitaux);

    @Query("SELECT * FROM categorieSigneVitaux ORDER BY description ASC")
    List<CategorieSigneVitaux> catSignesVitaux();
}
