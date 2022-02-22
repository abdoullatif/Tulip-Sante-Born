package com.example.tulipsante.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.tulipsante.dao.MedecinDao;
import com.example.tulipsante.dao.PatientDao;
import com.example.tulipsante.database.TulipSanteDatabase;
import com.example.tulipsante.models.Patient;

import java.util.List;

public class MedecinPatientRepository {
    private MedecinDao medecinDao;
    private PatientDao patientDao;

    public MedecinPatientRepository(Application application) {
        TulipSanteDatabase tulipSanteDatabase = TulipSanteDatabase.getInstance(application);
        medecinDao = tulipSanteDatabase.medecinDao();
        patientDao = tulipSanteDatabase.patientDao();
    }

    // **
    // Get Doc Patients Number
    public String getDocPatientNumber(String idMedecin,String actualDate) {
        return patientDao.getDocPatientsNumber(idMedecin,actualDate);
    }


    public LiveData<List<Patient>> getDocP(List<String> data) {
        LiveData<List<Patient>> patients = null;
        if(Character.isDigit(data.get(2).charAt(0)) && data.get(2).length() != 8) {
            patients = patientDao.getDocPatientLive(data.get(0),data.get(1),data.get(2));
        }
        else if(data.get(2).length() == 8 && (Character.isUpperCase(data.get(2).charAt(0)) || Character.isDigit(data.get(2).charAt(0)))) {
            patients = patientDao.getDocPatientsWithTag(data.get(0),data.get(1),data.get(2));
        }
        else {
            patients = patientDao.getDocPatientsWithSearch(data.get(0),data.get(1),data.get(2));
        }
        return patients;
    }

    // **
    // Get Doc Patients
    public List<Patient> getDocPatient(String idMedecin,String actualDate,String index) {
        List<Patient> patientList = null;
        try {
            patientList = new GetPatienAsync(patientDao).execute(idMedecin, actualDate, index).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return patientList;
    }

    public class GetPatienAsync extends AsyncTask<String, Void, List<Patient>> {
        PatientDao patientDao;
        List<Patient> patientList;
        public GetPatienAsync(PatientDao patientDao) {
            this.patientDao = patientDao;
        }
        @Override
        protected List<Patient> doInBackground(String... strings) {
            patientList = patientDao.getDocPatients(strings[0],strings[1],strings[2]);
            return patientList;
        }
    }

    // **
    // Get Doc Patients With Tag
    public List<Patient> getDocPatientsWithTag(String idMedecin, String actualDate,String idTag) {
        List<Patient> patientList = null;
        try {
            patientList = new GetPatientWithTagAsync(patientDao).execute(idMedecin, actualDate, idTag).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return patientList;
    }

    public class GetPatientWithTagAsync extends AsyncTask<String, Void, List<Patient>> {
        PatientDao patientDao;
        List<Patient> patientList;
        public GetPatientWithTagAsync(PatientDao patientDao) {
            this.patientDao = patientDao;
        }
        @Override
        protected List<Patient> doInBackground(String... strings) {
//            patientList = patientDao.getDocPatientsWithTag(strings[0],strings[1],strings[2]);
            return patientList;
        }
    }

    // **
    // Get Doc Patients With Search
    public LiveData<List<Patient>> getDocPatientWithSearch(String idMedecin,String actualDate,String searchAttr) {
        return patientDao.getDocPatientsWithSearch(idMedecin,actualDate,searchAttr);
    }

    public String numberOfRecord(String idMedecin,String actualDate) {
        String res = null;
        try {
            res = new GetNumberOfRecordAsync(patientDao).execute(idMedecin,actualDate).get();
        } catch (Exception e){
        }
        return res;
    }

    private static class GetNumberOfRecordAsync extends AsyncTask<String, Void, String> {
        private PatientDao patientDao;
        private String res;
        private GetNumberOfRecordAsync(PatientDao patientDao) {
            this.patientDao = patientDao;
        }
        @Override
        protected String doInBackground(String... data) {
            res = patientDao.getDocPatientsNumber(data[0],data[1]);
            return res;
        }
    }
}
