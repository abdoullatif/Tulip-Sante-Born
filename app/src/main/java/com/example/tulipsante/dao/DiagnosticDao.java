package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Diagnostic;

import java.util.List;

@Dao
public interface DiagnosticDao {
    @Insert
    void insertDiagnostic(Diagnostic diagnostic);

    @Query("SELECT * from diagnostic WHERE diagnostic.idDiagnostic LIKE :idDiagnostic")
    Diagnostic getDiagnostic(String idDiagnostic);

    @Query("SELECT maladie.description From maladie INNER JOIN diagnostic ON diagnostic.idMaladie LIKE maladie.idMaladie AND diagnostic.idConsultation LIKE :idConsultation")
    List<String> diagnosticDescription(String idConsultation);
}
