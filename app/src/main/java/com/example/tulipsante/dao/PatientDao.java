package com.example.tulipsante.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tulipsante.models.Patient;

import java.util.List;

@Dao
public interface PatientDao {

    @Insert
    void insert(Patient patient);
    @Update
    void update(Patient patient);
    @Delete
    void delete(Patient patient);

    // **
    // Get unique patient
    @Query("SELECT * FROM patient WHERE patient.idPatient LIKE :idPatient")
    Patient patient(String idPatient);

    @Query("SELECT count(*) FROM patient")
    String patientSize();

    // **
    // All Patients
    @Query("SELECT * FROM patient ORDER BY patient.dateRegistration Desc limit :index,10")
    LiveData<List<Patient>> getAllPatientsLive(String index);

    @Query("SELECT * FROM patient ORDER BY patient.dateRegistration ASC limit :index,10")
    LiveData<List<Patient>> getAllPatientsListOldest(String index);

    @Query("SELECT * FROM patient WHERE patient.prenomPatient LIKE :searchAttr OR patient.idPatient LIKE :searchAttr")
    LiveData<List<Patient>> getPatientWithFirstNameAndId(String searchAttr);

    @Query("SELECT * FROM patient WHERE patient.uidPatient LIKE :tagId")
    LiveData<List<Patient>> getPatientWithTag(String tagId);

    // **
    // Doctor's Patients
    @Query("SELECT * FROM patient inner join permission on permission.idPatient = patient.idPatient WHERE permission.idMedecin = :idMedecin and permission.dateExpiration > :actualDate and permission.type = 'public' ORDER BY patient.dateRegistration Desc limit :index,10")
    List<Patient> getDocPatients(String idMedecin, String actualDate, String index);
    // **
    // Doctor's Patients
    @Query("SELECT * FROM patient inner join permission on permission.idPatient = patient.idPatient WHERE permission.idMedecin = :idMedecin and permission.dateExpiration > :actualDate and permission.type = 'public' ORDER BY patient.dateRegistration Desc limit :index,10")
    LiveData<List<Patient>> getDocPatientLive(String idMedecin, String actualDate, String index);

    @Query("SELECT * FROM patient inner join permission on permission.idPatient = patient.idPatient WHERE permission.idMedecin = :idMedecin and permission.dateExpiration > :actualDate and permission.type = 'public' and patient.uidPatient = :tagId")
    LiveData<List<Patient>> getDocPatientsWithTag(String idMedecin, String actualDate,String tagId);

    @Query("SELECT * FROM patient inner join permission on permission.idPatient = patient.idPatient WHERE permission.idMedecin = :idMedecin and permission.dateExpiration > :actualDate and permission.type = 'public' and patient.prenomPatient like :searchAttr OR patient.idPatient LIKE :searchAttr")
    LiveData<List<Patient>> getDocPatientsWithSearch(String idMedecin, String actualDate,String searchAttr);

    @Query("SELECT COUNT(*) FROM patient inner join permission on permission.idPatient = patient.idPatient WHERE permission.idMedecin = :idMedecin and permission.dateExpiration > :actualDate and permission.type = 'public'")
    String getDocPatientsNumber(String idMedecin, String actualDate);

    @Query("SELECT count(*) FROM patient, medecin WHERE patient.uidPatient AND medecin.uidMedecin LIKE :tagId")
    String checkUidExist(String tagId);

}
