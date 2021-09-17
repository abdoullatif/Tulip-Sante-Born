package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Permission;
import com.example.tulipsante.repository.PermissionRepository;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

public class PermissionDialogViewModel extends AndroidViewModel {
    private PermissionRepository permissionRepository;
    private SharedPreferences sharedPreferences;
    private String idPatient;
    private String idMedecin;

    public PermissionDialogViewModel(@NonNull Application application) {
        super(application);
        permissionRepository = new PermissionRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idPatient = sharedPreferences.getString("IDPATIENT", null);
        idMedecin = sharedPreferences.getString("IDMEDECIN", null);

    }

    public void insertPermission(String dateRequested,String dateExpiration, String type, String path) {
        permissionRepository.insertPermission(new Permission(
                GeneralPurposeFunctions.idTable(),
                idPatient,
                idMedecin,
                dateRequested,
                dateExpiration,
                type,
                path,
                ""
        ));
    }

}
