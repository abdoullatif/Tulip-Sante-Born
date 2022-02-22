package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.tulipsante.models.Patient;
import com.example.tulipsante.repository.MedecinPatientRepository;
import com.example.tulipsante.repository.PatientRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientFragmentViewModel extends AndroidViewModel {
    private PatientRepository patientRepository;
    private MedecinPatientRepository medecinPatientRepository;
    private LiveData<List<Patient>> myPatients;
    private SharedPreferences sharedPreferences;
    private String idMedecin;
    // Current date
    private final String myFormat = "yyyy-MM-dd";
    Calendar calendar1 = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    String date;

    private LiveData<List<Patient>> patients;
    private MutableLiveData<List<String>> filterLiveData = new MutableLiveData<>();


    public PatientFragmentViewModel(@NonNull Application application) {
        super(application);
        patientRepository = new PatientRepository(application);
        medecinPatientRepository = new MedecinPatientRepository(application);
        myPatients = patientRepository.getAllPatients();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idMedecin = sharedPreferences.getString("IDMEDECIN",null);
        Date dateD = calendar1.getTime();
        date = sdf.format(dateD);

        patients = Transformations.switchMap(filterLiveData,
                v -> {
                    List<String> data = new ArrayList<>();
                    data.add(idMedecin);
                    data.add(date);
                    data.add(v.get(0));
                    return medecinPatientRepository.getDocP(data);
                });

    }

    public LiveData<List<Patient>> getSearchBy() { return patients; }
    public void setFilter(List<String> filter) { filterLiveData.setValue(filter); }

    public void insert(Patient patient){
        patientRepository.insert(patient);
    }

    public void update(Patient patient) {
        patientRepository.update(patient);
    }


    public List<Patient> getDocPatientWithTag(String idTag) {
        return medecinPatientRepository.getDocPatientsWithTag(idMedecin,date,idTag);
    }

    public LiveData<List<Patient>> getDocPatientFromSearchAttr(String searchAttr) {
        return medecinPatientRepository.getDocPatientWithSearch(idMedecin,date,searchAttr);
    }

    public String getNumberOfRecord() {
        return medecinPatientRepository.numberOfRecord(idMedecin,date);
    }
}
