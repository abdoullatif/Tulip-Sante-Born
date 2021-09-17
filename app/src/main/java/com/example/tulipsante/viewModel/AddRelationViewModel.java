package com.example.tulipsante.viewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.Relation;
import com.example.tulipsante.repository.PatientRepository;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import java.util.List;

public class AddRelationViewModel extends AndroidViewModel {
    private PatientRepository patientRepository;
    private SharedPreferences sharedPreferences;
    private String idPatient;

    public AddRelationViewModel(@NonNull Application application) {
        super(application);
        patientRepository = new PatientRepository(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        idPatient = sharedPreferences.getString("IDPATIENT", null);
    }

    public LiveData<List<Patient>> getPatientSearch(String searchAttr) {
        return patientRepository.getPatientFromSearchAttr("%" + searchAttr + "%");
    }

    public boolean insertRelation(Relation relation,String genre) {
        boolean res;
        if(Integer.parseInt(patientRepository.checkRelation(idPatient, relation.getIdRelationPatient())) > 0) {
            res = false;
        }
        else {
            // First insertion
            relation.setIdPatient(idPatient);
            patientRepository.insertPatientRelation(relation);
            // Second insertion
            String rel;
            if (relation.getTypeRelation().equals("Mère") || relation.getTypeRelation().equals("Père")) {
                rel = "Enfant";
            } else {
                if (genre.equals("Male")) {
                    rel = "Père";
                } else {
                    rel = "Mère";
                }
            }
            patientRepository.insertPatientRelation(new Relation(
                    GeneralPurposeFunctions.idTable(),
                    relation.getIdRelationPatient(),
                    idPatient,
                    rel,
                    ""

            ));
            res = true;
        }
        return res;
    }

}
