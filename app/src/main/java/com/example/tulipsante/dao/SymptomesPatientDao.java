package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.SymptomesPatient;

import java.util.List;

@Dao
public interface SymptomesPatientDao {
    @Insert
    void insertSymptomePatient(SymptomesPatient symptomesPatient);

    @Query("SELECT * FROM symptomesPatient WHERE symptomesPatient.idConsultation LIKE :idConsultation")
    SymptomesPatient symptomesPatient(String idConsultation);

    @Query("SELECT symptome.description From symptome INNER JOIN symptomesPatient ON symptomesPatient.idSymptome LIKE symptome.idSymptome AND symptomesPatient.idConsultation LIKE :idConsultation")
    List<String> symptomDescription(String idConsultation);
}
