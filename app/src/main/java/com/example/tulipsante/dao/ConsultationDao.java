package com.example.tulipsante.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.models.uIModels.ConsultationUMedecin;
import com.example.tulipsante.models.uIModels.DashPatientProfileData;
import com.example.tulipsante.models.uIModels.DashProfileData;
import com.example.tulipsante.models.uIModels.HistoriqueTacheMedecin;

import java.util.List;

@Dao
public interface ConsultationDao {
    @Insert
    void insertNewConsultation(Consultation consultation);

    @Query("Delete from consultation where consultation.idConsultation = :idConsultation")
    void deleteConsultation(String idConsultation);

    @Query("SELECT * FROM consultation WHERE consultation.idPatient LIKE :idPatient ORDER BY consultation.description ASC")
    LiveData<List<Consultation>> getConsultationPatient(String idPatient);

    @Query("SELECT * FROM consultation WHERE consultation.idPatient LIKE :idPatient ORDER BY consultation.dateConsultation DESC")
    List<Consultation> getConsultationRecord(String idPatient);

//    @Query("SELECT *, nomMedecin, prenomMedecin FROM consultation inner join medecin on medecin.idMedecin like consultation.idMedecin WHERE consultation.idPatient LIKE :idPatient ORDER BY consultation.dateConsultation DESC limit :index,10")
//    List<ConsultationUMedecin> getConsultationRecordSortLatest(String idPatient, String index);
//
//    @Query("SELECT *, nomMedecin, prenomMedecin FROM consultation inner join medecin on medecin.idMedecin like consultation.idMedecin WHERE consultation.idPatient LIKE :idPatient ORDER BY consultation.dateConsultation ASC limit :index,10")
//    List<ConsultationUMedecin> getConsultationRecordSortOldest(String idPatient, String index);

    @Query("SELECT *, nomMedecin, prenomMedecin FROM consultation inner join medecin on medecin.idMedecin like consultation.idMedecin WHERE consultation.idPatient LIKE :idPatient AND consultation.dateConsultation LIKE :dateConsultation ORDER BY consultation.dateConsultation ASC")
    LiveData<List<ConsultationUMedecin>> getConsultationRecordSearch(String idPatient, String dateConsultation);

    @Query("SELECT *, nomMedecin, prenomMedecin FROM consultation inner join medecin on medecin.idMedecin like consultation.idMedecin WHERE consultation.idPatient LIKE :idPatient ORDER BY consultation.dateConsultation DESC limit :index,15")
    LiveData<List<ConsultationUMedecin>> getConsultationRecordLive(String idPatient, String index);

    @Query("SELECT count(*) FROM consultation WHERE consultation.idPatient LIKE :idPatient")
    String numberOfConsultationRecord(String idPatient);

    @Query("SELECT * FROM consultation WHERE consultation.idConsultation LIKE :idConsultation")
    List<Consultation> getConsultationWithId(String idConsultation);

    @Query("SELECT COUNT(*) FROM consultation WHERE consultation.idConsultation LIKE :idConsultation")
    String consultationExist(String idConsultation);

    @Query("SELECT COUNT(*),dateConsultation FROM consultation WHERE consultation.idMedecin LIKE :idMedecin AND consultation.dateConsultation LIKE :dateConsultation  GROUP BY substr(dateConsultation,1,10)")
    List<DashProfileData> getConsultationGraph(String idMedecin, String dateConsultation);

    @Query("SELECT COUNT(*),dateConsultation FROM consultation WHERE consultation.idMedecin LIKE :idMedecin AND consultation.dateConsultation LIKE :dateConsultation  GROUP BY dateConsultation")
    LiveData<List<DashProfileData>> getConsultGraph(String idMedecin, String dateConsultation);

    @Query("SELECT idConsultation FROM consultation WHERE consultation.idPatient LIKE :idPatient ORDER BY consultation.dateConsultation DESC LIMIT 1")
    String getConsultationId(String idPatient);

    @Query("SELECT consultation.dateConsultation, valeur FROM consultation INNER JOIN signesVitaux ON consultation.idConsultation LIKE signesVitaux.idConsultation INNER JOIN categorieSigneVitaux on signesVitaux.idCatSV like categorieSigneVitaux.idCatSV AND consultation.idPatient LIKE :idPatient AND categorieSigneVitaux.description LIKE :description")
    List<DashPatientProfileData> getWeightData(String idPatient,String description);

    // **
    // Historique tache consultation medecin
    @Query("SELECT count(*) FROM consultation WHERE consultation.idMedecin LIKE :idMedecin")
    String numberOfRecord(String idMedecin);

    @Query("SELECT *,nomPatient,prenomPatient from consultation inner join patient on consultation.idPatient LIKE patient.idPatient AND consultation.idMedecin LIKE :idMedecin ORDER BY dateConsultation Desc limit :index,15")
    LiveData<List<HistoriqueTacheMedecin>> getHistoriqueTache(String idMedecin, String index);

    @Query("SELECT *,nomPatient,prenomPatient from consultation inner join patient on consultation.idPatient LIKE patient.idPatient AND consultation.idMedecin LIKE :idMedecin AND consultation.dateConsultation LIKE :dateHistorique ORDER BY dateConsultation ASC")
    LiveData<List<HistoriqueTacheMedecin>> getHistoriqueTacheSearch(String idMedecin,String dateHistorique);

}
