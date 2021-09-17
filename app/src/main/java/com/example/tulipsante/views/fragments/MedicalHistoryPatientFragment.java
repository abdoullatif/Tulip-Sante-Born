package com.example.tulipsante.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tulipsante.models.Allergie;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.models.Vice;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.MedicalHistoryPatientViewModel;
import com.example.tulipsante.views.adapters.AllRecViewAdapter;
import com.example.tulipsante.views.adapters.AntecedentRecViewAdapter;
import com.example.tulipsante.views.adapters.ViceRecViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MedicalHistoryPatientFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private List<Vice> viceList = new ArrayList<>();
    private List<Maladie> maladieList = new ArrayList<>();
    private List<Allergie> allergieListFood = new ArrayList<>();
    private List<Allergie> allergieListDrugs = new ArrayList<>();

    // View
    private RecyclerView recViewVices,
            recViewAntecedents,
            recViewAllFood,
            recViewAllDrugs;
    private TextView textViewVice,
            textViewAntecendent,
            textViewAlFood,
            textViewAlDrugs;

    // View Model
    private MedicalHistoryPatientViewModel medicalHistoryPatientViewModel;

    // Adapters
    private ViceRecViewAdapter viceRecViewAdapter;
    private AntecedentRecViewAdapter antecedentRecViewAdapter;
    private AllRecViewAdapter allRecViewAdapterFood;
    private AllRecViewAdapter allRecViewAdapterDrugs;

    public MedicalHistoryPatientFragment() {
        // Required empty public constructor
    }

    public static MedicalHistoryPatientFragment newInstance(String param1, String param2) {
        MedicalHistoryPatientFragment fragment = new MedicalHistoryPatientFragment();
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
        return inflater.inflate(R.layout.fragment_medical_history_patient, container, false);
    }

    private void initViews(View view) {
        recViewVices = view.findViewById(R.id.recViewVices);
        recViewAntecedents = view.findViewById(R.id.recViewAntecedents);
        recViewAllDrugs = view.findViewById(R.id.recViewAllDrugs);
        recViewAllFood = view.findViewById(R.id.recViewAllFood);

        textViewVice = view.findViewById(R.id.textViewVice);
        textViewAntecendent = view.findViewById(R.id.textViewAntecedent);
        textViewAlDrugs = view.findViewById(R.id.textViewAllDrugs);
        textViewAlFood = view.findViewById(R.id.textViewAlFood);
    }

    private void initialisation() {
        medicalHistoryPatientViewModel = ViewModelProviders
                .of(this)
                .get(MedicalHistoryPatientViewModel.class);

        viceRecViewAdapter = new ViceRecViewAdapter();
        antecedentRecViewAdapter = new AntecedentRecViewAdapter();
        allRecViewAdapterDrugs = new AllRecViewAdapter();
        allRecViewAdapterFood = new AllRecViewAdapter();

        maladieList.clear();
        viceList.clear();
        allergieListDrugs.clear();
        allergieListFood.clear();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setData();
    }

    private void setData() {
        new Thread(() -> {
            List<Maladie> maladieList = medicalHistoryPatientViewModel.getAntecedentPatient();
            List<Allergie> allergieListDrugs = medicalHistoryPatientViewModel.getPatientAllergiesDrugs();
            List<Allergie> allergieListFood = medicalHistoryPatientViewModel.getPatientAllergiesFood();
            List<Vice> viceList = medicalHistoryPatientViewModel.getVicePatient();
            getActivity().runOnUiThread(() -> {
                setAntecedent(maladieList);
                setVices(viceList);
                setAllergiesDrugs(allergieListDrugs);
                setAllergiesFood(allergieListFood);
            });
        }).start();
    }


    private void setAntecedent(List<Maladie> maladies) {
        recViewAntecedents.setHasFixedSize(true);
        recViewAntecedents.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recViewAntecedents.setAdapter(antecedentRecViewAdapter);
        if(maladies.size() <= 0) {
            String data = getText(R.string.antecedents) + " (0)" ;
            textViewAntecendent.setText(data);
            maladieList.add(
                    new Maladie("1001","No data","No data","No data"));
        } else {
            String data = getText(R.string.antecedents) + " (" + maladies.size() + ")" ;
            textViewAntecendent.setText(data);
            maladieList.addAll(maladies);
        }
        antecedentRecViewAdapter.setMaladieList(maladieList);
        antecedentRecViewAdapter.notifyDataSetChanged();
    }

    private void setVices(List<Vice> vices) {
        recViewVices.setHasFixedSize(true);
        recViewVices.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recViewVices.setAdapter(viceRecViewAdapter);
        if(vices.size() <= 0) {
            String data = getText(R.string.vices) + " (0)" ;
            textViewVice.setText(data);
            viceList.add(new Vice("1001","No Data", ""));
        } else {
            String data = getText(R.string.vices) + " (" + vices.size() + ")" ;
            textViewVice.setText(data);
            viceList.addAll(vices);
        }
        viceRecViewAdapter.setViceList(viceList);
        viceRecViewAdapter.notifyDataSetChanged();
    }

    private void setAllergiesFood(List<Allergie> allergies) {
        recViewAllFood.setHasFixedSize(true);
        recViewAllFood.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recViewAllFood.setAdapter(allRecViewAdapterFood);
        if(allergies.size() <= 0) {
            String data = getText(R.string.allergies_food) + " (0)" ;
            textViewAlFood.setText(data);
            allergieListFood.add(
                    new Allergie("1001","food","No Data", ""));
        } else {
            String data = getText(R.string.allergies_food) + " (" + allergies.size() + ")" ;
            textViewAlFood.setText(data);
            allergieListFood.addAll(allergies);
        }
        allRecViewAdapterFood.setAllergieList(allergieListFood);
        allRecViewAdapterFood.notifyDataSetChanged();
    }

    private void setAllergiesDrugs(List<Allergie> allergies) {
        recViewAllDrugs.setHasFixedSize(true);
        recViewAllDrugs.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recViewAllDrugs.setAdapter(allRecViewAdapterDrugs);
        if(allergies.size() <= 0) {
            String data = getText(R.string.allergies_drugs) + " (0)" ;
            textViewAlDrugs.setText(data);
            allergieListDrugs.add(
                    new Allergie("1001","drugs","No Data", ""));
        } else {
            String data = getText(R.string.allergies_drugs) + " (" + allergies.size() + ")" ;
            textViewAlDrugs.setText(data);
            allergieListDrugs.addAll(allergies);
        }
        allRecViewAdapterDrugs.setAllergieList(allergieListDrugs);
        allRecViewAdapterDrugs.notifyDataSetChanged();
    }
}
