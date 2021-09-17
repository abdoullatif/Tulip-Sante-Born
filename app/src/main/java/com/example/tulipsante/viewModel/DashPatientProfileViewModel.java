package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.uIModels.DashPatientProfileData;
import com.example.tulipsante.repository.PatientRepository;

import java.util.List;

public class DashPatientProfileViewModel extends AndroidViewModel {
    private SharedPreferences sharedPreferences;
    private PatientRepository patientRepository;

    private String idPatient;

    public DashPatientProfileViewModel(@NonNull Application application) {
        super(application);
        patientRepository = new PatientRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idPatient = sharedPreferences.getString("IDPATIENT", null);
    }

    public String getWeight() {
        return patientRepository.getBasicVitals(idPatient,"weight");
    }

    public String getHeight() {
        return patientRepository.getBasicVitals(idPatient,"height");
    }

    public List<DashPatientProfileData> getDashPatientProfile() {
        return patientRepository.getDashPatient(idPatient);
    }
}
