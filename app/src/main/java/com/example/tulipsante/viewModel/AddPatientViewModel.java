package com.example.tulipsante.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tulipsante.models.Addresse;
import com.example.tulipsante.models.Commune;
import com.example.tulipsante.models.Contact;
import com.example.tulipsante.models.ContactUrgence;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.Region;
import com.example.tulipsante.repository.PatientRepository;

import java.util.List;

public class AddPatientViewModel extends AndroidViewModel {
    private PatientRepository patientRepository;

    public AddPatientViewModel(@NonNull Application application) {
        super(application);
        patientRepository = new PatientRepository(application);
    }

    public void insertNewPatient(
            Patient patient,
            Addresse addresse,
            Contact contact,
            ContactUrgence contactUrgence
    ) {
        // insert addresse and get Id
        patientRepository.insertPatientAddresse(addresse);
        patient.setIdAddresse(addresse.getIdAddresse());
        contact.setIdPersonne(patient.getIdPatient());
        contactUrgence.setIdPersonne(patient.getIdPatient());
        //insert patient and get Id
        patientRepository.insert(patient);
        //insert contact
        patientRepository.insertPatientContact(contact);
        //insert contact urgence
        patientRepository.insertContactUrgenceP(contactUrgence);
    }

    public boolean checkUidExist(String uid) {
        return patientRepository.checkUidExist(uid);
    }


    public List<Region> regionList() {
        return patientRepository.regionList();
    }

    public List<Commune> getCommuneFromRegion(String idRegion) {
        return patientRepository.getCommuneFromRegion(idRegion);
    }

}
