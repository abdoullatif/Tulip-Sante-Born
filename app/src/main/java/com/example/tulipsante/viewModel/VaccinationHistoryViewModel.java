package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.uIModels.VaccinePatient;
import com.example.tulipsante.models.uIModels.VaccineTableDialog;
import com.example.tulipsante.repository.PatientRepository;

import java.util.List;

public class VaccinationHistoryViewModel extends AndroidViewModel {

    private PatientRepository patientRepository;
    private SharedPreferences sharedPreferences;
    private String idPatient;

    public VaccinationHistoryViewModel(@NonNull Application application) {
        super(application);
        patientRepository = new PatientRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idPatient = sharedPreferences.getString("IDPATIENT", null);
    }

    public List<VaccinePatient> getVaccinationHistory(String sort) {
        return patientRepository.getPatientVaccination(idPatient, sort);
    }

    public List<VaccineTableDialog> getVaccineMore(String type) {
        return patientRepository.getVaccineMore(idPatient, type);
    }


    
}
