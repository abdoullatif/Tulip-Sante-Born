package com.example.tulipsante.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Vice;

import java.util.List;

@Dao
public interface ViceDao {
    @Insert
    void insertVice(Vice vice);

    @Query("SELECT * FROM vice ORDER BY vice.description ASC")
    LiveData<List<Vice>> getAllVices();

    @Query("SELECT * FROM vice WHERE vice.idVice IN (:allIds)")
    List<Vice> viceList(List<String> allIds);
}
