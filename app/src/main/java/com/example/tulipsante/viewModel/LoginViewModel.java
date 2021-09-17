package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.models.HistoriquePresence;
import com.example.tulipsante.repository.MedecinRepository;
import com.example.tulipsante.repository.UtilisateurRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LoginViewModel extends AndroidViewModel {

    private UtilisateurRepository utilisateurRepository;
    private MedecinRepository medecinRepository;
    private SharedPreferences sharedPreferences;
    // Current date
    private final String myFormat = "yyyy/MM/dd hh:mm:ss";
    private Calendar calendar1 = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private String date;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        medecinRepository = new MedecinRepository(application);
        utilisateurRepository = new UtilisateurRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        Date dateD = calendar1.getTime();
        date = sdf.format(dateD);
    }

    public boolean checkValidLogin(String username, String password) {
        String oldPassword = utilisateurRepository.getPassword(username);
        boolean res = GeneralPurposeFunctions.decrypePassword(password, oldPassword);
        if(res) {
            String idMedecin = utilisateurRepository.getIdMedecin(username);
            System.out.println(idMedecin);
            setSharedPreferences(idMedecin);
            insert(new HistoriquePresence(
                    GeneralPurposeFunctions.idTable(),
                    idMedecin,
                    date,
                    "Login",
                    ""
            ));
        }
        return res;
    }

    public String getIdMedecin(String username) {
        return utilisateurRepository.getIdMedecin(username);
    }

    public boolean checkTagEqualsUid(String uidString) {
        boolean result;
        result = medecinRepository.checkIfTagExist(uidString); // added recently
        if(result) {
            String idMedecin = medecinRepository.getIdMed(uidString); // added recently
            setSharedPreferences(idMedecin);
            insert(new HistoriquePresence(
                    GeneralPurposeFunctions.idTable(),
                    idMedecin,
                    date,
                    "Login",
                    ""
            ));
        }
        return result;
    }

    private void insert(HistoriquePresence historiquePresence) {
        medecinRepository.insert(historiquePresence);
    }

    private void setSharedPreferences(String idMedecin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("IDMEDECIN",idMedecin);
        editor.apply();
    }

}
