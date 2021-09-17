package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Region;

import java.util.List;

@Dao
public interface RegionDao {

    @Insert
    void insert(Region region);

    @Query("SELECT * FROM region WHERE region.idRegion LIKE :idRegion")
    Region getRegions(String idRegion);

    @Query("SELECT * FROM region")
    List<Region> getAllRegions();

}
