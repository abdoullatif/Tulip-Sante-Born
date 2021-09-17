package com.example.tulipsante.views.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tulipsante.views.fragments.ConsultationHistoryFragment;
import com.example.tulipsante.views.fragments.DashPatientProfileFragment;
import com.example.tulipsante.views.fragments.MedicalHistoryPatientFragment;
import com.example.tulipsante.views.fragments.PatientProfileFragment;
import com.example.tulipsante.views.fragments.VaccinationHistoryFragment;

import java.util.List;

public class PatientRecordFragmentAdapter extends FragmentStateAdapter {
    private String patientAge;
    private String patientGender;
    private List<String> basicData;

    public PatientRecordFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String patientAge, String patientGender, List<String> basicData) {
        super(fragmentManager, lifecycle);
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.basicData = basicData;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new PatientProfileFragment();
            case 2:
                return new MedicalHistoryPatientFragment();
            case 3:
                return new ConsultationHistoryFragment(basicData);
            case 4:
                return new VaccinationHistoryFragment();
        }
        return new DashPatientProfileFragment(patientAge,patientGender);
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
