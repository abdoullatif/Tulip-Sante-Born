package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Examen;
import com.example.tulipsante.models.uIModels.ExamenXTypeExamen;

import java.util.List;

@Dao
public interface ExamensDao {
    @Insert
    void insertExamen(Examen examen);

    @Query("SELECT * from examen WHERE examen.idExamen LIKE :idExamens")
    Examen getExamen(String idExamens);

    @Query("SELECT typeExamens, valeur from typeExamens inner join examen on typeExamens.idTypeExamens like examen.idTypeExamens WHERE examen.idConsultation LIKE :idConsultation")
    List<ExamenXTypeExamen> investigationDescription(String idConsultation);
}
