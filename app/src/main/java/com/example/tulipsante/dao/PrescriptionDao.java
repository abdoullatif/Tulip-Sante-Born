package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Prescription;

import java.util.List;

@Dao
public interface PrescriptionDao {
    @Insert
    void insertPrescription(Prescription prescription);

    @Query("SELECT * FROM prescription where idConsultation LIKE :idConsultation")
    List<Prescription> prescription(String idConsultation);
}
