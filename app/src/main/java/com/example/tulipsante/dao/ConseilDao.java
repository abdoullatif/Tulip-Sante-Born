package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Conseil;

import java.util.List;

@Dao
public interface ConseilDao {
    @Insert
    void insertConseil(Conseil conseil);

    @Query("SELECT * FROM conseil WHERE conseil.idConsultation LIKE :idConsultation")
    List<Conseil> conseil(String idConsultation);
}
