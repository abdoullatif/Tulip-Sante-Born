package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.tulipsante.repository.SuperAdminRepository;

public class LoginSAViewModel extends AndroidViewModel {
    private SuperAdminRepository superAdminRepository;
    private SharedPreferences sharedPreferences;

    public LoginSAViewModel(@NonNull Application application) {
        super(application);

        superAdminRepository = new  SuperAdminRepository(application);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public boolean checkValidLogin(String username, String password) {
        String oldPassword = superAdminRepository.getPassword(username);
        boolean res = oldPassword.equals(password);
        // GeneralPurposeFunctions.decrypePassword(password, oldPassword);
        if(res) {
            String id = superAdminRepository.getId(username);
            setSharedPreferences(id,username);
        }
        return res;
    }

    public String getIdSA(String username) {
        return superAdminRepository.getId(username);
    }

    public void setSharedPreferences(String id, String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("IDSA",id);
        editor.putString("SAUsername",username);
        editor.apply();
    }
}
