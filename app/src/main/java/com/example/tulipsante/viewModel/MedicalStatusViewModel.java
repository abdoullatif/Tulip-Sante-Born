package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.models.Symptome;
import com.example.tulipsante.models.SymptomesPatient;
import com.example.tulipsante.repository.ConsultationRepository;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MedicalStatusViewModel extends AndroidViewModel {
    private String idPatient;
    private String idMedecin;
    private String idConsultation;
    private SharedPreferences sharedPreferences;
    private ConsultationRepository consultationRepository;
    // current date
    private final String myFormat = "yyyy/MM/dd hh:MM:ss";
    Calendar calendar1 = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


    public MedicalStatusViewModel(@NonNull Application application) {
        super(application);
        consultationRepository = new ConsultationRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idMedecin = sharedPreferences.getString("IDMEDECIN",null);
        idConsultation = sharedPreferences.getString("IDCONSULTATION",null);
        idPatient = sharedPreferences.getString("IDPATIENT",null);

    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public LiveData<List<Symptome>> getAllSymptomes() {
        return consultationRepository.getAllSymptomes();
    }

    // **
    // INSERT NEW Values
    public void insertValues(List<Symptome> symptomeList) {
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
            for(int i = 0; i < symptomeList.size(); i++) {
                consultationRepository.insertSymptomesPatient(new SymptomesPatient(
                        GeneralPurposeFunctions.idTable(),
                        symptomeList.get(i).getIdSymptome(),
                        idConsultation,
                        ""
                ));
            }

        } else {
            for(int i = 0; i < symptomeList.size(); i++) {
                consultationRepository.insertSymptomesPatient(new SymptomesPatient(
                        GeneralPurposeFunctions.idTable(),
                        symptomeList.get(i).getIdSymptome(),
                        idConsultation,
                        ""
                ));
            }
        }
    }

    // **
    // Get symptom table from db
    public List<String> symptomList(String idConsultation) {
        return consultationRepository.getConsultationSymptom(idConsultation);
    }

}
