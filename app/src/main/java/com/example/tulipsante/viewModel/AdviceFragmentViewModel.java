package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Conseil;
import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.repository.ConsultationRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdviceFragmentViewModel extends AndroidViewModel {
    private String idPatient;
    private String idMedecin;
    private String idConsultation;
    private SharedPreferences sharedPreferences;
    private ConsultationRepository consultationRepository;

    // Current date
    private final String myFormat = "yyyy/MM/dd hh:mm:ss";
    Calendar calendar1 = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    public AdviceFragmentViewModel(@NonNull Application application) {
        super(application);
        consultationRepository = new ConsultationRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idMedecin = sharedPreferences.getString("IDMEDECIN",null);
        idConsultation = sharedPreferences.getString("IDCONSULTATION",null);
        idPatient = sharedPreferences.getString("IDPATIENT",null);
    }

    // Get consultation id
    public String getIdConsultation() {
        return idConsultation;
    }

    // **
    // Insert new balues
    public void insertValues(List<Conseil> conseils) {
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
            for (int i = 0; i < conseils.size(); i++) {
                conseils.get(i).setIdConsultation(idConsultation);
                consultationRepository.insertConseilPatient(conseils.get(i));
            }
        } else {
            for (int i = 0; i < conseils.size(); i++) {
                conseils.get(i).setIdConsultation(idConsultation);
                consultationRepository.insertConseilPatient(conseils.get(i));
            }
        }
    }

    // **
    // Get conseil table form db
    public List<Conseil> conseilList(String idConsultation) {
        return consultationRepository.getConsultationConseil(idConsultation);
    }

}
