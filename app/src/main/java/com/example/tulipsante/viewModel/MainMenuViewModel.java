package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.models.HistoriquePresence;
import com.example.tulipsante.repository.MedecinRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainMenuViewModel extends AndroidViewModel {
    private MedecinRepository medecinRepository;
    private SharedPreferences sharedPreferences;
    // Current date
    private final String myFormat = "yyyy/MM/dd hh:mm:ss";
    private Calendar calendar1 = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    public MainMenuViewModel(@NonNull Application application) {
        super(application);
        medecinRepository = new MedecinRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public void logout(String idMedecin) {
        Date dateD = calendar1.getTime();
        String date = sdf.format(dateD);
        unSetSharedPreferences();
        medecinRepository.insert(
                new HistoriquePresence(
                        GeneralPurposeFunctions.idTable(),
                        idMedecin,
                        date,
                        "Logout",
                        ""
                )
        );
    }

    private void unSetSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("IDMEDECIN");
        editor.apply();
    }
}
