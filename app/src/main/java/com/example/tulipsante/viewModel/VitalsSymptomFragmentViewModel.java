package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.CategorieSigneVitaux;
import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.models.SignesVitaux;
import com.example.tulipsante.models.uIModels.SignesVitauxXCatSignesVitaux;
import com.example.tulipsante.repository.ConsultationRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VitalsSymptomFragmentViewModel extends AndroidViewModel {
    private String idPatient;
    private String idMedecin;
    private String idConsultation;
    private SharedPreferences sharedPreferences;
    private ConsultationRepository consultationRepository;
    // current date
    private final String myFormat = "yyyy/MM/dd hh:MM:ss";
    Calendar calendar1 = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


    public VitalsSymptomFragmentViewModel(@NonNull Application application) {
        super(application);
        consultationRepository = new ConsultationRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idMedecin = sharedPreferences.getString("IDMEDECIN",null);
        idConsultation = sharedPreferences.getString("IDCONSULTATION",null);
        idPatient = sharedPreferences.getString("IDPATIENT",null);

    }

    public List<CategorieSigneVitaux> getSignesVitaux() {
        return consultationRepository.getSignesVitaux();
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    // **
    // INSERT NEW Values
    public void insertValues(List<SignesVitaux> signesVitaux) {
        Date dateD = calendar1.getTime();
        String date = sdf.format(dateD);
        if(!consultationRepository.consultationRecordExist(idConsultation)) {
            consultationRepository.insertConsultation(new Consultation(
                    idConsultation,
                    date,
                    idPatient,
                    idMedecin,
                    "",
                    ""
            ));
            for (int i = 0; i < signesVitaux.size(); i++) {
                signesVitaux.get(i).setIdConsultation(idConsultation);
                consultationRepository.insertSignesVitaux(signesVitaux.get(i));
            }

        } else {
            for (int i = 0; i < signesVitaux.size(); i++) {
                signesVitaux.get(i).setIdConsultation(idConsultation);
                consultationRepository.insertSignesVitaux(signesVitaux.get(i));
            }
        }
    }

    // **
    // Get vital table from db
    public List<SignesVitauxXCatSignesVitaux> vitalsSymptomList(String idConsultation) {
        return consultationRepository.getConsultationVitals(idConsultation);
    }

}
