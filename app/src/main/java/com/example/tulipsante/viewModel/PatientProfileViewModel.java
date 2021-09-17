package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Addresse;
import com.example.tulipsante.models.Commune;
import com.example.tulipsante.models.Contact;
import com.example.tulipsante.models.ContactUrgence;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.Region;
import com.example.tulipsante.repository.PatientRepository;

public class PatientProfileViewModel extends AndroidViewModel {
    private SharedPreferences sharedPreferences;
    private PatientRepository patientRepository;

    private String idPatient;

    public PatientProfileViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        patientRepository = new PatientRepository(application);
        idPatient = sharedPreferences.getString("IDPATIENT", null);
    }

    public Patient getPatient() {
        return patientRepository.getPatient(idPatient);
    }

    public ContactUrgence getPatientEmergency() {
        return patientRepository.getPatientEmergency(idPatient);
    }

    public Contact getPatientContact() {
        return patientRepository.getPatientContact(idPatient);
    }

    public Addresse getPatientAddress(String idAddresse) {
        return patientRepository.getPatientAddresse(idAddresse);
    }

    public Commune getPatientCommune(String idCommune) {
        return patientRepository.getPatientCommune(idCommune);
    }

    public Region getPatientRegion(String idRegion) {
        return  patientRepository.getPatientRegion(idRegion);
    }
}
