package com.example.tulipsante.views.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tulipsante.views.fragments.AdviceFragment;
import com.example.tulipsante.views.fragments.DiagnosticFragment;
import com.example.tulipsante.views.fragments.FirstTimeConsultationFragment;
import com.example.tulipsante.views.fragments.InvestigationFragment;
import com.example.tulipsante.views.fragments.MedicalStatusFragment;
import com.example.tulipsante.views.fragments.PrescriptionFragment;
import com.example.tulipsante.views.fragments.VaccinationFragment;
import com.example.tulipsante.views.fragments.VitalsSymptomFragment;

public class ConsultationFragmentAdapter extends FragmentStateAdapter {
    private String idPatient;
    private String idConsultation;
    private String clickedFrom;
    private Boolean hasConsulted;

    public ConsultationFragmentAdapter(
            @NonNull FragmentManager fragmentManager,
            @NonNull Lifecycle lifecycle,
            String idPatient,
            String clickedFrom,
            String idConsultation,
            Boolean hasConsulted) {
        super(fragmentManager, lifecycle);
        this.idPatient = idPatient;
        this.clickedFrom = clickedFrom;
        this.idConsultation = idConsultation;
        this.hasConsulted = hasConsulted;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(hasConsulted) {
            switch (position) {
                case 1:
                    return new VitalsSymptomFragment(idPatient, clickedFrom, idConsultation); // done
                case 2:
                    return new InvestigationFragment(idPatient, clickedFrom, idConsultation); // done
                case 3:
                    return new DiagnosticFragment(idPatient, clickedFrom, idConsultation); // done
                case 4:
                    return new PrescriptionFragment(idPatient, clickedFrom, idConsultation); // done
                case 5:
                    return new AdviceFragment(idPatient, clickedFrom,idConsultation); // done
                case 6:
                    return new VaccinationFragment(idPatient, clickedFrom,idConsultation); // done
            }
        }
        else {
            switch (position) {
                case 1:
                    return new MedicalStatusFragment(clickedFrom, idConsultation);
                case 2:
                    return new VitalsSymptomFragment(idPatient, clickedFrom, idConsultation);
                case 3:
                    return new InvestigationFragment(idPatient, clickedFrom, idConsultation);
                case 4:
                    return new DiagnosticFragment(idPatient, clickedFrom, idConsultation);
                case 5:
                    return new PrescriptionFragment(idPatient, clickedFrom, idConsultation);
                case 6:
                    return new AdviceFragment(idPatient, clickedFrom,idConsultation);
                case 7:
                    return new VaccinationFragment(idPatient, clickedFrom,idConsultation);
            }
        }
        return hasConsulted
                ? new MedicalStatusFragment(clickedFrom,idConsultation)
                : new FirstTimeConsultationFragment(idPatient,"");
    }

    @Override
    public int getItemCount() {
        return hasConsulted ? 7 : 8;
    }
}
