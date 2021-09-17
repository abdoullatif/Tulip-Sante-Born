package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.uIModels.HistoriquePresenceMedecin;
import com.example.tulipsante.repository.MedecinRepository;

import java.util.List;

public class HistoriquePresenceViewModel extends AndroidViewModel {
    private String idMedecin;
    private SharedPreferences sharedPreferences;
    private MedecinRepository medecinRepository;

    private LiveData<List<HistoriquePresenceMedecin>> historiquePresence;
    private MutableLiveData<String> filterLiveData = new MutableLiveData<>();

    public HistoriquePresenceViewModel(@NonNull Application application) {
        super(application);
        medecinRepository = new MedecinRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idMedecin = sharedPreferences.getString("IDMEDECIN", null);

        historiquePresence = Transformations.switchMap(filterLiveData,
                v -> medecinRepository.getPresenceHistory(idMedecin,v));
    }

    public LiveData<List<HistoriquePresenceMedecin>> getHisPresenceMedecin() { return historiquePresence; }
    public void setFilter(String filter) { filterLiveData.setValue(filter); }

    public String getNumberOfRecord() {
        return medecinRepository.numberOfRecord(idMedecin);
    }
}
