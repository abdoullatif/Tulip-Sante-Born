package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.uIModels.HistoriqueTacheMedecin;
import com.example.tulipsante.repository.MedecinRepository;
import com.example.tulipsante.repository.PatientRepository;

import java.util.List;

public class HistoriqueTacheViewModel extends AndroidViewModel {
    private String idMedecin;
    private SharedPreferences sharedPreferences;
    private MedecinRepository medecinRepository;
    private PatientRepository patientRepository;

    private LiveData<List<HistoriqueTacheMedecin>> historiqueTache;
    private MutableLiveData<String> filterLiveData = new MutableLiveData<>();

    public HistoriqueTacheViewModel(@NonNull Application application) {
        super(application);
        medecinRepository = new MedecinRepository(application);
        patientRepository = new PatientRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idMedecin = sharedPreferences.getString("IDMEDECIN", null);

        historiqueTache = Transformations.switchMap(filterLiveData,
                v -> medecinRepository.getHistoriqueTache(idMedecin,v));
    }

    public LiveData<List<HistoriqueTacheMedecin>> getHistoriqueTache() { return historiqueTache; }
    public void setFilter(String filter) { filterLiveData.setValue(filter); }

    public String getNumberOfRecord() {
        return medecinRepository.numberOfConsRecord(idMedecin);
    }

    public Patient getPatientById(String idPatient) {
        return patientRepository.getPatient(idPatient);
    }
}
