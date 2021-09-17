package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.uIModels.ConsultationUMedecin;
import com.example.tulipsante.repository.PatientRepository;

import java.util.List;

public class ConsultationHistoryViewModel extends AndroidViewModel {
    private PatientRepository patientRepository;
    private SharedPreferences sharedPreferences;
    private String idPatient;

    private LiveData<List<ConsultationUMedecin>> numberOfConsRecord;
    private MutableLiveData<String> filterLiveData = new MutableLiveData<>();

    public ConsultationHistoryViewModel(@NonNull Application application) {
        super(application);
        patientRepository = new PatientRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idPatient = sharedPreferences.getString("IDPATIENT", null);

        numberOfConsRecord = Transformations.switchMap(filterLiveData,
                v -> patientRepository.getPatientConsultation(idPatient,v));
    }

    public LiveData<List<ConsultationUMedecin>> getNumberOfConsRecord() {
        return numberOfConsRecord;
    }
    public void setFilter(String filter) { filterLiveData.setValue(filter); }

    public String getNumberOfRecord() {
        return patientRepository.numberOfConsRecord(idPatient);
    }
}
