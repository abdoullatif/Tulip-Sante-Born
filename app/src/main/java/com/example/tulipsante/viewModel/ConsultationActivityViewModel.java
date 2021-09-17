package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.repository.ConsultationRepository;
import com.example.tulipsante.repository.PermissionRepository;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import java.util.List;

public class ConsultationActivityViewModel extends AndroidViewModel {
    private ConsultationRepository consultationRepository;
    private PermissionRepository permissionRepository;
    private SharedPreferences sharedPreferences;
    private String consultationId;
    private String idPatient;
    private String idMedecin;

    public ConsultationActivityViewModel(@NonNull Application application) {
        super(application);
        consultationRepository = new ConsultationRepository(application);
        permissionRepository = new PermissionRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        consultationId = GeneralPurposeFunctions.idTable();
        idPatient = sharedPreferences.getString("IDPATIENT",null);
        idMedecin = sharedPreferences.getString("IDMEDECIN", null);
        setSharedPreferencesConsultation();
    }

    // TODO: 01/03/21 unset shared preferences when page is left

    private void setSharedPreferencesConsultation() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("IDCONSULTATION", consultationId);
        editor.apply();
    }

    public void setSharedPreferencesPatient(String idPatient) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("IDPATIENT", idPatient);
        editor.apply();
    }

    public void unSetSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("IDPATIENT");
        editor.remove("IDCONSULTATION");
        editor.apply();
    }

    public String getConsultationId() {
        return consultationId;
    }

    public boolean hasPatientConsulted(String idPatient) {
        boolean result = false;
        List<Consultation> consultationList =
                consultationRepository.getConsultationRecord(idPatient);
        if(consultationList.size() != 0) {
            result = true;
        }
        return result;
    }

    public boolean consultationExist() {
        boolean result;
        String consultation =
                consultationRepository.consultationExist(consultationId);
        result = !consultation.equals("0");
        return result;
    }

    public void deleteDataEntered() {
        consultationRepository.deleteConsultation(consultationId);
    }

    public boolean hasValidPermission(String idPatient) {
        return permissionRepository.hasPermission(idMedecin, idPatient);
    }

    public boolean checkTagAndPatientMatch(byte[] tag,String uid) {
        boolean res;
        res = GeneralPurposeFunctions.bytesToHexString(tag).equals(uid);
        return res;
    }
}