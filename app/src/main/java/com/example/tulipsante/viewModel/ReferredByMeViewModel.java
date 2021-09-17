package com.example.tulipsante.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.tulipsante.models.uIModels.PatientXRefXDoc;
import com.example.tulipsante.repository.ReferenceRepository;

import java.util.List;

public class ReferredByMeViewModel extends AndroidViewModel {
    private ReferenceRepository referenceRepository;

    private LiveData<List<PatientXRefXDoc>> patientList;
    private MutableLiveData<List<String>> filterLiveData = new MutableLiveData<>();

    public ReferredByMeViewModel(@NonNull Application application) {
        super(application);

        referenceRepository = new ReferenceRepository(application);

        patientList = Transformations.switchMap(filterLiveData,
                v -> referenceRepository.patientReferredByMe(v));
    }

    public LiveData<List<PatientXRefXDoc>> getPatientList() {
        return patientList;
    }
    public void setFilter(List<String> filter) { filterLiveData.setValue(filter); }
}
