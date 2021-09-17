package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.uIModels.VaccinationData;
import com.example.tulipsante.models.uIModels.VaccinePatient;
import com.example.tulipsante.models.uIModels.VaccineTableDialog;
import com.example.tulipsante.models.VaccinationPatient;

import java.util.List;

@Dao
public interface VaccinationPatientDao {
    @Insert
    void insertVaccinationPatient(VaccinationPatient vaccinationPatient);

    @Query("SELECT * FROM vaccinationPatient WHERE vaccinationPatient.idConsultation LIKE :idConsultation")
    List<VaccinationPatient> vaccinationList(String idConsultation);

    @Query("SELECT typeVaccination.type, dateVaccination From typeVaccination INNER JOIN vaccinationPatient ON vaccinationPatient.idTypeVaccination LIKE typeVaccination.idTypeVaccination AND vaccinationPatient.idConsultation LIKE :idConsultation")
    List<VaccinationData> vaccineType(String idConsultation);

    @Query("select type, dateVaccination, duree from typeVaccination inner join vaccinationPatient on vaccinationPatient.idTypeVaccination like typeVaccination.idTypeVaccination inner join consultation on vaccinationPatient.idConsultation like consultation.idConsultation and consultation.idPatient like :idPatient group by typeVaccination.type")
    List<VaccinePatient> getVaccinePatient(String idPatient);

    @Query("select vaccinationPatient.dateVaccination, vaccinationPatient.idConsultation, typeVaccination.type from vaccinationPatient inner join typeVaccination on vaccinationPatient.idTypeVaccination like typeVaccination.idTypeVaccination inner join consultation on consultation.idConsultation like vaccinationPatient.idConsultation and consultation.idPatient like :idPatient and typeVaccination.type like :type order by dateVaccination DESC")
    List<VaccineTableDialog> getVaccineMore(String idPatient, String type);

    @Query("select type, dateVaccination, duree from typeVaccination inner join vaccinationPatient on vaccinationPatient.idTypeVaccination like typeVaccination.idTypeVaccination inner join consultation on vaccinationPatient.idConsultation like consultation.idConsultation and consultation.idPatient like :idPatient and typeVaccination.type like :search group by typeVaccination.type")
    List<VaccinePatient> getVaccinePatientSearch(String idPatient, String search);
}
