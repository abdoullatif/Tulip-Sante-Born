package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Allergie;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.models.Vice;
import com.example.tulipsante.repository.PatientRepository;

import java.util.List;

public class MedicalHistoryPatientViewModel extends AndroidViewModel {
    private PatientRepository patientRepository;
    private SharedPreferences sharedPreferences;
    private String idPatient;
    
    
    public MedicalHistoryPatientViewModel(@NonNull Application application) {
        super(application);
        patientRepository = new PatientRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idPatient = sharedPreferences.getString("IDPATIENT", null);
    }
    
    public List<Allergie> getPatientAllergiesFood() {
        return patientRepository.getPatientAllergies(idPatient,"food");
    }
    
    public List<Allergie> getPatientAllergiesDrugs() {
        return patientRepository.getPatientAllergies(idPatient,"drug");
    }

    public List<Vice> getVicePatient() {
        return patientRepository.getPatientVice(idPatient);
    }

    public List<Maladie> getAntecedentPatient() {
        return patientRepository.getPatientAntecedent(idPatient);
    }
    
}
