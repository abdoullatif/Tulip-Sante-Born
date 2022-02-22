package com.example.tulipsante.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tulipsante.models.Addresse;
import com.example.tulipsante.models.Commune;
import com.example.tulipsante.models.Contact;
import com.example.tulipsante.models.ContactUrgence;
import com.example.tulipsante.models.Departement;
import com.example.tulipsante.models.Hopital;
import com.example.tulipsante.models.Medecin;
import com.example.tulipsante.models.Pays;
import com.example.tulipsante.models.Region;
import com.example.tulipsante.models.Specialite;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.ProfileViewModel;

import java.io.File;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ImageView profileImage;
    public TextView textViewProfileName,
            textViewLast,
            textViewFirst,
            textViewGender,
            textViewDob,
            textViewSpeciality,
            textViewDepartment,
            textViewHopital,
            textViewId,
            textViewSpecialisation,
            textViewAddress1,
            textViewCommune,
            textViewCity,
            textViewCountry,
            textViewPrimary,
            textViewEmergency,
            textViewRelation,
            textViewEmail,
            textViewNameRelation,
            textViewPhoneRelation
    ;

    private ProfileViewModel profileViewModel;

    private void initViews(View view) {
        textViewLast = view.findViewById(R.id.textViewLast);
        textViewFirst = view.findViewById(R.id.textViewFirst);
        textViewGender = view.findViewById(R.id.textViewGender);
        textViewDob = view.findViewById(R.id.textViewDob);
        textViewProfileName = view.findViewById(R.id.textViewProfileName);
        profileImage = view.findViewById(R.id.profileImage);

        textViewSpeciality = view.findViewById(R.id.textViewSpeciality);
        textViewDepartment = view.findViewById(R.id.textViewDepartment);
        textViewHopital = view.findViewById(R.id.textViewHospital);
        textViewId = view.findViewById(R.id.textViewId);
        textViewSpecialisation = view.findViewById(R.id.textViewSpecialisation);

        textViewAddress1 = view.findViewById(R.id.textViewAddress1);
        textViewCommune = view.findViewById(R.id.textViewCommune);
        textViewCity = view.findViewById(R.id.textViewCity);
        textViewCountry = view.findViewById(R.id.textViewCountry);

        textViewPrimary = view.findViewById(R.id.textViewPrimaryPhone);
        textViewEmergency = view.findViewById(R.id.textViewEmergencyPhone);
        textViewRelation = view.findViewById(R.id.textViewRelation);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewNameRelation = view.findViewById(R.id.textViewNameRelation);
        textViewPhoneRelation = view.findViewById(R.id.textViewPhoneRelation);

    }

    private void initialisation() {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        try {
            Medecin medecin = getMedecin();
            Specialite specialite = profileViewModel.getSpecialite(medecin.getIdSpecialite());
            Departement departement = profileViewModel.getDepartement(medecin.getIdDepartement());
            Hopital hopital = profileViewModel.getHopital(departement.getIdHopital());
            Addresse addresse = profileViewModel.getAddress(medecin.getIdAddresse());
            Contact contact = profileViewModel.getContact(medecin.getIdMedecin());
            ContactUrgence contactUrgence = profileViewModel.getContactUrgence(medecin.getIdMedecin());
            Commune commune = profileViewModel.getCommune(addresse.getIdCommune());
            Region region = profileViewModel.getRegion(commune.getIdRegion());
            Pays pays = profileViewModel.getPaysMedecin(region.getIdPays());
            displayData(medecin,specialite,departement, hopital, addresse, contact, commune, region, pays, contactUrgence);
            setProfileImage(medecin);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProfileImage(Medecin medecin) {
        File myPath = new File(
                Environment
                        .getExternalStorageDirectory()+
                        File.separator+
                        "Tulip_sante/Medecin/"+medecin.getPhoto()
                );
        if(myPath.exists()) {
            Glide.with(this).load(myPath).into(profileImage);
        }
    }

    private void displayData(
            Medecin medecin,
            Specialite specialite,
            Departement departement,
            Hopital hopital,
            Addresse addresse,
            Contact contact,
            Commune commune,
            Region region,
            Pays pays,
            ContactUrgence contactUrgence
    ) {
        String name = medecin.getNomMedecin()+ " " + medecin.getPrenomMedecin();
        textViewProfileName.setText(name);
        textViewSpecialisation.setText(specialite.getDescription());
        textViewLast.setText(medecin.getNomMedecin());
        textViewFirst.setText(medecin.getPrenomMedecin());
        textViewGender.setText(medecin.getGenreMedecin());
        textViewDob.setText(medecin.getDateDeNaissance());

        textViewSpeciality.setText(specialite.getDescription());
        textViewDepartment.setText(departement.getDescription());
        textViewHopital.setText(hopital.getNomHopital());

        textViewId.setText(medecin.getIdMedecin());

        textViewAddress1.setText(addresse.getPremiereAddresse());
        textViewCommune.setText(commune.getNomCommune());
        textViewCity.setText(region.getNomRegion());
        textViewCountry.setText(pays.getNomPays());

        textViewPrimary.setText(contact.getTelephoneContact());
        textViewEmergency.setText(contact.getTelephoneUrgence());
        textViewEmail .setText(contact.getEmail());
        textViewRelation.setText(contactUrgence.getRelation());
        textViewPhoneRelation.setText(contactUrgence.getTelephoneRelation());
        textViewNameRelation.setText(contactUrgence.getNomRelation());
    }

    private Medecin getMedecin() {
        return profileViewModel.getMedecin();
    }
}
