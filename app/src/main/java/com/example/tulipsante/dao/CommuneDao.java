package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Commune;

import java.util.List;

@Dao
public interface CommuneDao {

    @Insert
    void insert(Commune commune);

    @Query("SELECT * FROM commune WHERE commune.idCommune LIKE :idCommune")
    Commune getCommuneFomPK(String idCommune);

    @Query("SELECT * FROM commune WHERE commune.idRegion LIKE :idRegion")
    List<Commune> getCommuneFromRegion(String idRegion);
}
