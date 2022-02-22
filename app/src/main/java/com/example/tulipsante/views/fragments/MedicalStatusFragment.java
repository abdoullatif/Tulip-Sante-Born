package com.example.tulipsante.views.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.models.Symptome;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.MedicalStatusViewModel;
import com.example.tulipsante.views.adapters.MedicalStatusTableAdapter;
import com.example.tulipsante.views.adapters.SymptomesListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MedicalStatusFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private List<Symptome> symptomesList = new ArrayList<>();
    private List<String> symptomPatientList = new ArrayList<>();

    private ListView listViewSymptomes;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private TableLayout tableLayout;
    private CardView buttonSave;
    private TextView textViewNoData;

    // Adapter
    private SymptomesListViewAdapter symptomesListViewAdapter;
    private MedicalStatusTableAdapter medicalStatusTableAdapter;

    // View Model
    private MedicalStatusViewModel medicalStatusViewModel;

    public MedicalStatusFragment(String clickedFrom,String idConsultation) {
        this.mParam1 = clickedFrom;
        this.mParam2 = idConsultation;
    }

    public static MedicalStatusFragment newInstance(String param1, String param2) {
        MedicalStatusFragment fragment = new MedicalStatusFragment("", "");
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

    private void initViews(View view) {
        listViewSymptomes = view.findViewById(R.id.listViewSymptomes);
        buttonSave = view.findViewById(R.id.buttonSave);
        tableLayout = view.findViewById(R.id.tabLayout);
        linearLayout = view.findViewById(R.id.linear2);
        recyclerView = view.findViewById(R.id.recyclerView);
        textViewNoData = view.findViewById(R.id.textViewNoData);
    }

    private void init() {
        if(mParam1.equals("Patient List")) {
            linearLayout.setVisibility(View.VISIBLE);
            tableLayout.setVisibility(View.INVISIBLE);
        }
        else {
            linearLayout.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.INVISIBLE);
        }
    }

    private void initialisation() {
        medicalStatusViewModel = ViewModelProviders.of(this)
                .get(MedicalStatusViewModel.class);
        medicalStatusTableAdapter = new MedicalStatusTableAdapter();
        symptomesList.clear();
        symptomesListViewAdapter = new SymptomesListViewAdapter(
                getContext(),
                R.layout.checkbox_list_view_entry,
                R.id.textViewId,
                symptomesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(medicalStatusTableAdapter);
        listViewSymptomes.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listViewSymptomes.setAdapter(symptomesListViewAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medical_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        init();
        initialisation();

        setSymptomes();
        setSymptomTable();
        onButtonSavePressed();
    }

    private void setSymptomTable() {
        if(tableLayout.getVisibility() == View.VISIBLE) {
            if(mParam2 == null) {
                medicalStatusTableAdapter.setSymptomList(medicalStatusViewModel.symptomList(medicalStatusViewModel.getIdConsultation()));
            }
            else {
                List<String> symptomList = medicalStatusViewModel.symptomList(mParam2);
                if(!symptomList.isEmpty()) {
                    medicalStatusTableAdapter.setSymptomList(symptomList);
                }
                else {
                    tableLayout.setVisibility(View.INVISIBLE);
                    textViewNoData.setVisibility(View.VISIBLE);
                }
            }
            medicalStatusTableAdapter.notifyDataSetChanged();
        }
    }

    private void setSymptomes() {
        medicalStatusViewModel.getAllSymptomes().observe(this, symptomes -> {
            symptomesList.addAll(symptomes);
            symptomesListViewAdapter.notifyDataSetChanged();
        });
    }

    private void onButtonSavePressed() {
        buttonSave.setOnClickListener(view -> {
            if(symptomesListViewAdapter.selectedSymptome.isEmpty()) {
                Toast.makeText(
                        getContext(),
                        "Enter data before saving!",
                        Toast.LENGTH_LONG).show();
            }
            else {
                showAlertDialogButtonClicked();
            }
        });
    }

    // dialog alert
    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure that you want to save the data?");

        builder.setPositiveButton("Continue", (dialogInterface, i) -> {
            medicalStatusViewModel
                    .insertValues(symptomesListViewAdapter.selectedSymptome);
            linearLayout.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.INVISIBLE);
            setSymptomTable();
            Toast.makeText(getContext(), "Enregistrer!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}