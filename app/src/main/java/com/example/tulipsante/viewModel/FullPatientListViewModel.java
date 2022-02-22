package com.example.tulipsante.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.tulipsante.models.Patient;
import com.example.tulipsante.repository.PatientRepository;

import java.util.List;

public class FullPatientListViewModel extends AndroidViewModel {
    private PatientRepository patientRepository;

    private LiveData<List<Patient>> patientsList;
    private MutableLiveData<List<String>> filterLiveData = new MutableLiveData<>();

    public FullPatientListViewModel(@NonNull Application application) {
        super(application);
        patientRepository = new PatientRepository(application);

        patientsList = Transformations.switchMap(filterLiveData,
                v -> patientRepository.getAllPatientsLive(v));
    }

    public LiveData<List<Patient>> getAllPatients() { return patientsList; }
    public void setFilter(List<String> filter) { filterLiveData.setValue(filter); }

    public String getNumberOfRecord() {
        return patientRepository.numberOfPatient();
    }
}
