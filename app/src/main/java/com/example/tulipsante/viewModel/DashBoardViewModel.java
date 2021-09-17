package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tulipsante.models.Medecin;
import com.example.tulipsante.models.Nouvelles;
import com.example.tulipsante.models.uIModels.DashProfileData;
import com.example.tulipsante.repository.ConsultationRepository;
import com.example.tulipsante.repository.MedecinPatientRepository;
import com.example.tulipsante.repository.MedecinRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashBoardViewModel extends AndroidViewModel {
    private SharedPreferences sharedPreferences;
    private MedecinPatientRepository medecinPatientRepository;
    private MedecinRepository medecinRepository;
    private ConsultationRepository consultationRepository;
    private String idMedecin;

    // Current date
    private final String myFormat = "yyyy/MM";
    Calendar calendar1 = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private String date;

    public DashBoardViewModel(@NonNull Application application) {
        super(application);
        medecinRepository = new MedecinRepository(application);
        medecinPatientRepository = new MedecinPatientRepository(application);
        consultationRepository = new ConsultationRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idMedecin = sharedPreferences.getString("IDMEDECIN", null);
    }

    public String getDate() {
        Date dateD = calendar1.getTime();
        return sdf.format(dateD);
    }

    public Medecin getMedecin() {
        return medecinRepository.getMedecin(idMedecin);
    }

    public String numberOfPatients() {
        Date dateD = calendar1.getTime();
        date = format.format(dateD);
        return medecinPatientRepository.getDocPatientNumber(idMedecin,date);
    }

    public List<DashProfileData> getConsultationGraph() {
        Date dateD = calendar1.getTime();
        date = sdf.format(dateD);
        return consultationRepository.getConsultationGraph(idMedecin, "%" + date + "%");
    }

    public List<Nouvelles> getNouvelles() {
        return medecinRepository.getNouvelles();
    }
}
