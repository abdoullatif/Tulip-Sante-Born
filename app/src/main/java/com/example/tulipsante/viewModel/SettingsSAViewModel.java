package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Parametre;
import com.example.tulipsante.repository.SuperAdminRepository;

import java.util.List;

public class SettingsSAViewModel extends AndroidViewModel {
    private SuperAdminRepository superAdminRepository;
    private SharedPreferences sharedPreferences;

    LiveData<List<Parametre>> parametreList;

    public SettingsSAViewModel(@NonNull Application application) {
        super(application);

        superAdminRepository = new SuperAdminRepository(application);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);

        parametreList = superAdminRepository.getParametreList();
    }

    public void insertParametre(Parametre parametre) {
        superAdminRepository.insertParametre(parametre);
    }

    public void updateParametre(Parametre parametre) {
        superAdminRepository.updateParametre(parametre);
    }

    public LiveData<List<Parametre>> getParametreList() {
        return parametreList;
    }

    public boolean isPasswordValid(String password) {
        String oldPassword = superAdminRepository
                .getPassword(sharedPreferences.getString("SAUsername", null));
        return oldPassword.equals(password);
    }
}
