package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.models.TypeVaccination;
import com.example.tulipsante.models.uIModels.VaccinationData;
import com.example.tulipsante.models.VaccinationPatient;
import com.example.tulipsante.repository.ConsultationRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VaccinationFragmentViewModel extends AndroidViewModel {
    private String idPatient;
    private String idMedecin;
    private String idConsultation;
    private SharedPreferences sharedPreferences;
    private ConsultationRepository consultationRepository;

    // Current date
    private final String myFormat = "yyyy/MM/dd hh:MM:ss";
    Calendar calendar1 = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    public VaccinationFragmentViewModel(@NonNull Application application) {
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

    // **
    // Get list of vaccines
    public List<TypeVaccination> getVaccines() {
        return consultationRepository.getVaccines();
    }

    // **
    // Insert new balues
    public void insertValues(List<VaccinationPatient> vaccinationPatients, String date) {
        Date dateD = calendar1.getTime();
        date = sdf.format(dateD);
        if(!consultationRepository.consultationRecordExist(idConsultation)) {
            consultationRepository.insertConsultation(new Consultation(
                    idConsultation,
                    date,
                    idPatient,
                    idMedecin,
                    "",
                    ""
            ));
            for (int i = 0; i < vaccinationPatients.size(); i++) {
                vaccinationPatients.get(i).setIdConsultation(idConsultation);
                vaccinationPatients.get(i).setDateVaccination(date);
                consultationRepository.insertVaccinationPatient(vaccinationPatients.get(i));
            }
        } else {
            for (int i = 0; i < vaccinationPatients.size(); i++) {
                vaccinationPatients.get(i).setIdConsultation(idConsultation);
                vaccinationPatients.get(i).setDateVaccination(date);
                consultationRepository.insertVaccinationPatient(vaccinationPatients.get(i));
            }
        }
    }

    // **
    // Get vaccination table from db
    public List<VaccinationData> vaccinationList(String idConsultation) {
        return consultationRepository.getConsultationVaccination(idConsultation);
    }
}
