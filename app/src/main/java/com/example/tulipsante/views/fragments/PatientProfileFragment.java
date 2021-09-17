package com.example.tulipsante.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tulipsante.models.Addresse;
import com.example.tulipsante.models.Commune;
import com.example.tulipsante.models.Contact;
import com.example.tulipsante.models.ContactUrgence;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.Region;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.PatientProfileViewModel;

public class PatientProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Views
    private TextView tvSurname,
            tvFirstName,
            tvGender,
            tvDate,
            tvSituation,
            tvNationality,
            tvIdNumber,
            tvBloodGroup,
            tvPrimaryPhone,
            tvSecondaryPhone,
            tvEmail,
            tvStreet,
            tvRegion,
            tvCity,
            tvRelation,
            tvFullRelation,
            tvNumberRelation;

    // View Model
    private PatientProfileViewModel patientProfileViewModel;

    // Models
    private Patient patient;
    private ContactUrgence contactUrgence;
    private Contact contact;
    private Addresse addresse;
    private Commune commune;
    private Region region;

    public PatientProfileFragment() {
        // Required empty public constructor
    }

    public static PatientProfileFragment newInstance(String param1, String param2) {
        PatientProfileFragment fragment = new PatientProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_profile, container, false);
    }

    private void initViews(View view) {
        tvSurname = view.findViewById(R.id.textViewSurname);
        tvFirstName = view.findViewById(R.id.textViewFirst);
        tvGender = view.findViewById(R.id.textViewGender);
        tvDate = view.findViewById(R.id.textViewDOB);
        tvSituation = view.findViewById(R.id.matrimoninalStatus);
        tvNationality = view.findViewById(R.id.textViewNationality);
        tvIdNumber = view.findViewById(R.id.textViewIdNumber);
        tvBloodGroup = view.findViewById(R.id.textViewBloodGroup);
        tvPrimaryPhone = view.findViewById(R.id.textViewPrimaryPhone);
        tvSecondaryPhone = view.findViewById(R.id.textViewSecondaryPhone);
        tvEmail = view.findViewById(R.id.textViewEmail);
        tvStreet = view.findViewById(R.id.textViewStreet);
        tvCity = view.findViewById(R.id.textViewCity);
        tvRegion = view.findViewById(R.id.textViewRegion);
        tvRelation = view.findViewById(R.id.textViewRelation);
        tvFullRelation = view.findViewById(R.id.textViewFullRelation);
        tvNumberRelation = view.findViewById(R.id.textViewNumRelation);
    }

    private void initialisation() {
        patientProfileViewModel = ViewModelProviders
                .of(this)
                .get(PatientProfileViewModel.class);
        patient = patientProfileViewModel.getPatient();
        contactUrgence = patientProfileViewModel.getPatientEmergency();
        contact = patientProfileViewModel.getPatientContact();
        try {
            addresse = patientProfileViewModel.getPatientAddress(patient.getIdAddresse());
            commune = patientProfileViewModel.getPatientCommune(addresse.getIdCommune());
            region = patientProfileViewModel.getPatientRegion(commune.getIdRegion());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setBasicInformation();
        setEmergencyContact();
        setOtherDetails();
    }

    private void setBasicInformation() {
        try {
            tvSurname.setText(patient.getNomPatient());
            tvFirstName.setText(patient.getPrenomPatient());
            tvGender.setText(patient.getGenrePatient());
            tvDate.setText(patient.getDateNaissancePatient());
            tvSituation.setText(patient.getStatusMatrimonialPatient());
            tvNationality.setText(patient.getNationalitePatient());
            tvIdNumber.setText(patient.getNumeroIdentitePatient());
            tvBloodGroup.setText(patient.getGroupeSanguinPatient());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setEmergencyContact() {
        try {
            tvRelation.setText(contactUrgence.getRelation());
            tvFullRelation.setText(contactUrgence.getNomRelation());
            tvNumberRelation.setText(contactUrgence.getTelephoneRelation());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setOtherDetails() {
        try {
            tvPrimaryPhone.setText(contact.getTelephoneContact());
            tvSecondaryPhone.setText(contact.getTelephoneUrgence());
            tvEmail.setText(contact.getEmail());
            tvStreet.setText(addresse.getPremiereAddresse());
            tvCity.setText(commune.getNomCommune());
            tvRegion.setText(region.getNomRegion());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
