package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.repository.ConsultationRepository;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.models.Allergie;
import com.example.tulipsante.models.AllergiePatient;
import com.example.tulipsante.models.AntecedentPatient;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.models.Vice;
import com.example.tulipsante.models.VicePatient;
import com.example.tulipsante.repository.PatientRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FirstTimeConsultationViewModel extends AndroidViewModel {
    private PatientRepository patientRepository;
    private ConsultationRepository consultationRepository;
    private SharedPreferences sharedPreferences;
    private String idMedecin;
    private String idConsultation;

    // current date
    private final String myFormat = "yyyy/MM/dd hh:MM:ss";
    Calendar calendar1 = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    public FirstTimeConsultationViewModel(@NonNull Application application) {
        super(application);
        patientRepository = new PatientRepository(application);
        consultationRepository = new ConsultationRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idMedecin = sharedPreferences.getString("IDMEDECIN", null);
        idConsultation = sharedPreferences.getString("IDCONSULTATION", null);

    }

    public LiveData<List<Vice>> getAllVices() {
        return patientRepository.getAllVices();
    }

    public LiveData<List<Allergie>> getFoodAllergies() {
        return patientRepository.getAllergies("food");
    }

    public LiveData<List<Allergie>> getDrugAllergies() {
        return patientRepository.getAllergies("drug");
    }

    public LiveData<List<Maladie>> getAllMaladies() {
        return patientRepository.getMaladies();
    }

    public void insertValues(String idPatient,List<Vice> vice,List<Allergie> allergieD,List<Allergie> allergieF,List<Maladie> maladies) {
        if(!consultationRepository.consultationRecordExist(idConsultation)) {
            Date dateD = calendar1.getTime();
            String date = sdf.format(dateD);
            consultationRepository.insertConsultation(new Consultation(
                    idConsultation,
                    date,
                    idPatient,
                    idMedecin,
                    "",
                    ""
            ));
            insertAllergies(allergieD, idPatient);
            insertAllergies(allergieF, idPatient);
            insertAntecedent(maladies, idPatient);
            insertPatientVices(vice, idPatient);
        }
    }


    private void insertPatientVices(List<Vice> vice,String idPatient) {
        for(int i = 0; i < vice.size(); i++) {
            patientRepository.insertVicePatient(new VicePatient(
                    GeneralPurposeFunctions.idTable(),
                    vice.get(i).getIdVice(),
                    idPatient,
                    ""
            ));
        }
    }

    private void insertAllergies(List<Allergie> allergies,String idPatient) {
        for(int i = 0; i < allergies.size(); i++) {
            patientRepository.insertAllergiePatient(new AllergiePatient(
                    GeneralPurposeFunctions.idTable(),
                    allergies.get(i).getIdAllergie(),
                    idPatient,
                    ""
            ));
        }
    }

    private void insertAntecedent(List<Maladie> maladies,String idPatient) {
        for(int i = 0; i < maladies.size(); i++) {
            patientRepository.insertAntecedentPatient(new AntecedentPatient(
                    GeneralPurposeFunctions.idTable(),
                    maladies.get(i).getIdMaladie(),
                    idPatient,
                    ""
            ));
        }
    }

}
