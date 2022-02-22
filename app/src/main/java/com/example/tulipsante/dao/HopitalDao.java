package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Hopital;

@Dao
public interface HopitalDao {

    @Insert
    void insert(Hopital hopital);

    @Query("SELECT * FROM hopital WHERE hopital.idHopital LIKE :idHopital")
    Hopital getHopital(String idHopital);
}
