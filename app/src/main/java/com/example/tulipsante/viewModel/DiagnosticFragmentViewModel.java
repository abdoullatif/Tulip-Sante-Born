package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.models.Diagnostic;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.repository.ConsultationRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiagnosticFragmentViewModel extends AndroidViewModel {
    private String idPatient;
    private String idMedecin;
    private String idConsultation;
    private SharedPreferences sharedPreferences;
    private ConsultationRepository consultationRepository;

    // Current date
    private final String myFormat = "yyyy/MM/dd hh:MM:ss";
    Calendar calendar1 = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    public DiagnosticFragmentViewModel(@NonNull Application application) {
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
    // Get list of patologies
    public List<Maladie> getMaladies() {
        return consultationRepository.getMaladies();
    }

    // **
    // Insert new values
    public void insertValues(List<Diagnostic> diagnostics, String description) {
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
            for (int i = 0; i < diagnostics.size(); i++) {
                diagnostics.get(i).setIdConsultation(idConsultation);
                diagnostics.get(i).setDescription(description);
                consultationRepository.insertDiagnosticPatient(diagnostics.get(i));
                System.out.println("Inserting data!");
            }
        } else {
            for (int i = 0; i < diagnostics.size(); i++) {
                diagnostics.get(i).setIdConsultation(idConsultation);
                diagnostics.get(i).setDescription(description);
                consultationRepository.insertDiagnosticPatient(diagnostics.get(i));
                System.out.println("Inserting data!");
            }
        }
    }

    // **
    // Get diagnostic table from db
    public List<String> diagnosticList(String idConsultation) {
        return consultationRepository.getConsultationDiagnostic(idConsultation);
    }

}
