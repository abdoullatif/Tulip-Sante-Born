package com.example.tulipsante.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.tulipsante.models.Medecin;
import com.example.tulipsante.models.Reference;
import com.example.tulipsante.repository.MedecinRepository;
import com.example.tulipsante.repository.ReferenceRepository;

import java.util.List;

public class AddReferenceViewModel extends AndroidViewModel {

    private MedecinRepository medecinRepository;
    private ReferenceRepository referenceRepository;

    private LiveData<List<Medecin>> doctorList;
    private MutableLiveData<String> filterLiveData = new MutableLiveData<>();

    public AddReferenceViewModel(@NonNull Application application) {
        super(application);

        referenceRepository = new ReferenceRepository(application);
        medecinRepository = new MedecinRepository(application);

        doctorList = Transformations.switchMap(filterLiveData,
                v -> medecinRepository.getMedecinList(v));

    }

    public void insertReference(Reference reference) {
        referenceRepository.insertReference(reference);
    }

    public LiveData<List<Medecin>> getDoctorList() {
        return doctorList;
    }
    public void setFilter(String filter) {
        filterLiveData.setValue(filter);
    }

}
