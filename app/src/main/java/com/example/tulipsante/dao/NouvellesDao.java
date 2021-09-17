package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Nouvelles;

import java.util.List;

@Dao
public interface NouvellesDao {

    @Insert
    void insertNouvelles(Nouvelles nouvelles);

    @Query("SELECT * FROM nouvelles ORDER BY dateAdded DESC LIMIT 10")
    List<Nouvelles> news();

}
