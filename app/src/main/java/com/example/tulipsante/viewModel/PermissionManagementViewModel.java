package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.PermissionXMedecin;
import com.example.tulipsante.repository.PermissionRepository;

import java.util.List;

public class PermissionManagementViewModel extends AndroidViewModel {
    private PermissionRepository permissionRepository;
    private SharedPreferences sharedPreferences;
    private String idPatient;
    private String idMedecin;

    public PermissionManagementViewModel(@NonNull Application application) {
        super(application);
        permissionRepository = new PermissionRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idPatient = sharedPreferences.getString("IDPATIENT", null);
        idMedecin = sharedPreferences.getString("IDMEDECIN", null);
    }

    public List<PermissionXMedecin> getPermissions() {
        return permissionRepository.getPermissions(idMedecin, idPatient);
    }

    public void cancelPermission(String param) {
        permissionRepository.cancelPermission("","private",param);
    }
}
