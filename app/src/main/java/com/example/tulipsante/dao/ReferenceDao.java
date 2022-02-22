package com.example.tulipsante.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tulipsante.models.Reference;
import com.example.tulipsante.models.uIModels.PatientXRefXDoc;

import java.util.List;

@Dao
public interface ReferenceDao {

    @Insert
    void insertReference(Reference reference);

    @Update
    void updateReference(Reference reference);

    @Delete
    void deleteReference(Reference reference);

    @Query("SELECT * FROM reference WHERE idMedecin1 LIKE :idMedecin1")
    LiveData<List<Reference>> referenceMedecin1(String idMedecin1);

    @Query("SELECT * FROM reference WHERE idMedecin2 LIKE :idMedecin2")
    LiveData<List<Reference>> referenceMedecin2(String idMedecin2);

    @Query("SELECT * from patient " +
            "inner join consultation on consultation.idPatient = patient.idPatient " +
            "inner join reference on reference.idConsultation = consultation.idConsultation " +
            "inner join medecin on medecin.idMedecin = reference.idMedecin2 " +
            "where reference.idMedecin1 like :idMedecin")
    LiveData<List<PatientXRefXDoc>> patientReferredByMe(String idMedecin);

    @Query("SELECT * from patient " +
            "inner join consultation on consultation.idPatient = patient.idPatient " +
            "inner join reference on reference.idConsultation = consultation.idConsultation " +
            "inner join medecin on medecin.idMedecin = reference.idMedecin1 " +
            "where reference.idMedecin2 like :idMedecin")
    LiveData<List<PatientXRefXDoc>> patientReferredByOthers(String idMedecin);

}
