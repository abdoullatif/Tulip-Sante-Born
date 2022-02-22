package com.example.tulipsante.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Symptome;

import java.util.List;

@Dao
public interface SymptomeDao {
    @Insert
    void insertSymptome(Symptome symptome);

    @Query("SELECT * FROM symptome ORDER BY symptome.description ASC")
    LiveData<List<Symptome>> getAllSymptomes();
}
