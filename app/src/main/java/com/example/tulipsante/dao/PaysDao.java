package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Pays;

@Dao
public interface PaysDao {

    @Insert
    void insert(Pays pays);

    @Query("SELECT * FROM pays WHERE pays.idPays LIKE :idPays")
    Pays getPays(String idPays);
}
