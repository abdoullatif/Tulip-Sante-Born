package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.uIModels.RelationXPatient;
import com.example.tulipsante.repository.PatientRepository;

import java.util.List;

public class RelationMViewModel extends AndroidViewModel {
    private SharedPreferences sharedPreferences;
    private String idPatient;
    private PatientRepository repository;

    public RelationMViewModel(@NonNull Application application) {
        super(application);
        repository = new PatientRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idPatient = sharedPreferences.getString("IDPATIENT", null);

    }

    public List<RelationXPatient> getRelations() {
        return repository.getRelationPatient(idPatient);
    }

    public void deleteRelation(String idRelation) {
        repository.deleteRelation(idRelation);
    }

}
