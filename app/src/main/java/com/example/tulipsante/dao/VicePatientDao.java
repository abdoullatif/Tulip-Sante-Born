package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.VicePatient;

import java.util.List;

@Dao
public interface VicePatientDao {

    @Insert
    void insertVicePatient(VicePatient vicePatient);

    @Delete
    void deleteVicePatient(VicePatient vicePatient);

    @Query("SELECT * FROM vicePatient WHERE vicePatient.idPatient LIKE :idPatient")
    List<VicePatient> getVicePatient(String idPatient);

    @Query("SELECT idVice FROM vicePatient WHERE vicePatient.idPatient LIKE :idPatient")
    List<String> getPatientIdVice(String idPatient);

}
