package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.ConferenceContact;
import com.example.tulipsante.repository.MedecinRepository;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import java.util.List;

public class ConferencingViewModel extends AndroidViewModel {

    private MedecinRepository medecinRepository;
    private SharedPreferences sharedPreferences;
    private String idMedecin;

    public ConferencingViewModel(@NonNull Application application) {
        super(application);
        medecinRepository = new MedecinRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idMedecin = sharedPreferences.getString("IDMEDECIN", null);
    }

    public List<ConferenceContact> getConfContact() {
        return medecinRepository.getConfContacts(idMedecin);
    }

    public List<ConferenceContact> getConfByName(String nomComplet) {
        return medecinRepository.getConfByName(nomComplet);
    }

    public void deleteConfContact(String idConferenceContact) {
        medecinRepository.deleteConfContact(new ConferenceContact(
                idConferenceContact,
                "",
                "",
                "",
                ""
        ));
    }

    public void insertConfContact(String fullName, String skypeId) {
        medecinRepository.insertConfContact(new ConferenceContact(
                GeneralPurposeFunctions.idTable(),
                idMedecin,
                fullName,
                skypeId,
                ""
        ));
    }

}
