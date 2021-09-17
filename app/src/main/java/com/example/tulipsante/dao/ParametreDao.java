package com.example.tulipsante.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tulipsante.models.Parametre;

import java.util.List;

@Dao
public interface ParametreDao {
    @Insert
    void insertParametre(Parametre parametre);

    @Update
    void updateParametre(Parametre parametre);

    @Query("Select * from parametre")
    LiveData<List<Parametre>> parametreList();
}
