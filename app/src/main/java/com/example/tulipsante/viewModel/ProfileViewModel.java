package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tulipsante.models.Addresse;
import com.example.tulipsante.models.Commune;
import com.example.tulipsante.models.Contact;
import com.example.tulipsante.models.ContactUrgence;
import com.example.tulipsante.models.Departement;
import com.example.tulipsante.models.HistoriquePresence;
import com.example.tulipsante.models.Hopital;
import com.example.tulipsante.models.Medecin;
import com.example.tulipsante.models.Pays;
import com.example.tulipsante.models.Region;
import com.example.tulipsante.models.Specialite;
import com.example.tulipsante.repository.MedecinRepository;


public class ProfileViewModel extends AndroidViewModel {
    private MedecinRepository medecinRepository;
    private SharedPreferences sharedPreferences;

    private String idMedecin;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        medecinRepository = new MedecinRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idMedecin = sharedPreferences.getString("IDMEDECIN", null);
    }

    public Medecin getMedecin() {
        return medecinRepository.getMedecin(idMedecin);
    }

    public Specialite getSpecialite(String idSpecialite) {
        return medecinRepository.getSpecialite(idSpecialite);
    }

    public Departement getDepartement(String idDepartement) {
        return medecinRepository.getDepartement(idDepartement);
    }

    public Hopital getHopital(String idHopital) {
        return medecinRepository.getHopital(idHopital);
    }

    public Addresse getAddress(String idAddresse) {
        return medecinRepository.getAddresse(idAddresse);
    }

    public Contact getContact(String idPersonne) {
        return medecinRepository.getContact(idPersonne);
    }

    public ContactUrgence getContactUrgence(String idPersonne) {
        return medecinRepository.getContactUrgence(idPersonne);
    }

    public Region getRegion(String idRegion) {
        return medecinRepository.getRegion(idRegion);
    }

    public Commune getCommune(String idCommune) {
        return medecinRepository.getCommune(idCommune);
    }

    public Pays getPaysMedecin(String idPays) {
        return medecinRepository.getPaysMedecin(idPays);
    }


    public void insert(HistoriquePresence historiquePresence) {
        medecinRepository.insert(historiquePresence);
    }
}
