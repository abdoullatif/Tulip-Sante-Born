package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.models.Examen;
import com.example.tulipsante.models.TypeExamens;
import com.example.tulipsante.models.uIModels.ExamenXTypeExamen;
import com.example.tulipsante.repository.ConsultationRepository;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvestigationFragmentViewModel extends AndroidViewModel {
    private String idPatient;
    private String idMedecin;
    private String idConsultation;
    private SharedPreferences sharedPreferences;
    private ConsultationRepository consultationRepository;

    // Current date
    private final String myFormat = "yyyy/MM/dd hh:MM:ss";
    Calendar calendar1 = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    public InvestigationFragmentViewModel(@NonNull Application application) {
        super(application);
        consultationRepository = new ConsultationRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idMedecin = sharedPreferences.getString("IDMEDECIN",null);
        idConsultation = sharedPreferences.getString("IDCONSULTATION",null);
        idPatient = sharedPreferences.getString("IDPATIENT",null);
    }
    // **
    // Get list of patologies
    public List<TypeExamens> getTypeExams() {
        return consultationRepository.typeExamens();
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    // **
    // Insert new balues
    public void insertValues(List<Examen> examenList) {
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
            for (int i = 0; i < examenList.size(); i++) {
                examenList.get(i).setIdConsultation(idConsultation);
                File parentDirectory = new File(
                        Environment.getExternalStorageDirectory() +
                                File.separator+
                                "Tulip_sante"+
                                File.separator+
                                "Patients");
                if(parentDirectory.exists()) {
                    File childDirectory = new File(parentDirectory + File.separator + idPatient + File.separator + "Diagnostic");
                    File childDirectory2 = new File(idPatient + File.separator + "Diagnostic");
                    File file = new File(examenList.get(i).getValeur());
                    GeneralPurposeFunctions.moveFile(examenList.get(i).getValeur(),childDirectory.toString()+File.separator+file.getName());
                    examenList.get(i).setValeur(childDirectory2.toString()+File.separator+file.getName());
                }
                consultationRepository.insertExamens(examenList.get(i));
            }
        } else {
            for (int i = 0; i < examenList.size(); i++) {
                examenList.get(i).setIdConsultation(idConsultation);
                File parentDirectory = new File(
                        Environment.getExternalStorageDirectory() +
                                File.separator+
                                "Tulip_sante"+
                                File.separator+
                                "Patients");
                if(parentDirectory.exists()) {
                    File childDirectory = new File(parentDirectory + File.separator + idPatient + File.separator + "Diagnostic");
                    File childDirectory2 = new File(idPatient + File.separator + "Diagnostic");
                    File file = new File(examenList.get(i).getValeur());
                    GeneralPurposeFunctions.moveFile(examenList.get(i).getValeur(),childDirectory.toString()+File.separator+file.getName());
                    examenList.get(i).setValeur(childDirectory2.toString()+File.separator+file.getName());
                }
                consultationRepository.insertExamens(examenList.get(i));
            }
        }
    }

    // **
    // Get investigation exam table from db
    public List<ExamenXTypeExamen> investigationList(String idConsultation) {
        return consultationRepository.getConsultationInvestigation(idConsultation);
    }


}
