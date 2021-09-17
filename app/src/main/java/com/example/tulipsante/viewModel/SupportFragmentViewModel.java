package com.example.tulipsante.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tulipsante.models.Parametre;
import com.example.tulipsante.repository.SuperAdminRepository;

import java.util.List;

public class SupportFragmentViewModel extends AndroidViewModel {
    private SuperAdminRepository superAdminRepository;

    private LiveData<List<Parametre>> parametreList;

    public SupportFragmentViewModel(@NonNull Application application) {
        super(application);
        superAdminRepository = new SuperAdminRepository(application);

        parametreList = superAdminRepository.getParametreList();
    }

    public LiveData<List<Parametre>> getParametreList() {
        return parametreList;
    }
}
