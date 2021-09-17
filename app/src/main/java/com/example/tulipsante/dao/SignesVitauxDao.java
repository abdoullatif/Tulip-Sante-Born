package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.SignesVitaux;
import com.example.tulipsante.models.uIModels.SignesVitauxXCatSignesVitaux;

import java.util.List;

@Dao
public interface SignesVitauxDao {

    @Insert
    void insertSignesVitaux(SignesVitaux signesVitaux);

    @Query("SELECT * FROM signesVitaux WHERE signesVitaux.idConsultation = :idConsultation")
    SignesVitaux signesVitauxConsultation(String idConsultation );

    @Query("SELECT valeur FROM signesVitaux inner join categorieSigneVitaux on signesVitaux.idCatSV like categorieSigneVitaux.idCatSV WHERE signesVitaux.idConsultation = :idConsultation AND categorieSigneVitaux.description LIKE :description")
    String getValeurSV(String idConsultation, String description);
//
//    @Query("SELECT valeur FROM signesVitaux WHERE signesVitaux.idConsultation = :idConsultation AND signesVitaux.idCatSV LIKE :idCategorySignesVitaux")
//    String getValeurSV(String idConsultation, String idCategorySignesVitaux);

    @Query("SELECT description, valeur from categorieSigneVitaux inner join signesVitaux on categorieSigneVitaux.idCatSV like signesVitaux.idCatSV WHERE signesVitaux.idConsultation = :idConsultation")
    List<SignesVitauxXCatSignesVitaux> vitalsDescription(String idConsultation);
}
