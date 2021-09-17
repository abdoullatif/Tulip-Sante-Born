package com.example.tulipsante.views.fragments;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.models.Symptome;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.MedicalStatusViewModel;
import com.example.tulipsante.views.adapters.DiagnosticSpinnerAdapter;
import com.example.tulipsante.views.adapters.MedicalStatusTableAdapter;
import com.example.tulipsante.views.adapters.MedicalTableAdapter;
import com.example.tulipsante.views.adapters.PatologyTableAdapter;
import com.example.tulipsante.views.adapters.SymptomesListViewAdapter;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class MedicalStatusFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private List<Symptome> rowItems = new ArrayList<>();
    private List<String> symptomPatientList = new ArrayList<>();
    private String symptomeId, symptomeDescription;

    //    private ListView listViewSymptomes;
    private RecyclerView recyclerView, recyclerViewSymptom;
    private LinearLayout linearLayout;
    private TableLayout tableLayout;
    private CardView buttonSave;
    private TextView textViewNoData;

    private CardView buttonAdd;

    // Adapter
    private SymptomesListViewAdapter symptomesListViewAdapter;
    private MedicalStatusTableAdapter medicalStatusTableAdapter;
    private MedicalTableAdapter medicalTableAdapter;

    // View Model
    private MedicalStatusViewModel medicalStatusViewModel;

    private SearchableSpinner medSpinner;

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
        buttonSave = view.findViewById(R.id.buttonSave);
        tableLayout = view.findViewById(R.id.tabLayout);
        linearLayout = view.findViewById(R.id.linear1);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewSymptom = view.findViewById(R.id.recyclerViewSymptom);
        textViewNoData = view.findViewById(R.id.textViewNoData);

        medSpinner = view.findViewById(R.id.medicalSpinner);
        buttonAdd = view.findViewById(R.id.buttonAdd);
    }

    private void init() {
        if(mParam1.equals("Patient List")) {
            linearLayout.setVisibility(View.VISIBLE);
            buttonAdd.setVisibility(View.VISIBLE);
            tableLayout.setVisibility(View.INVISIBLE);
        }
        else {
            linearLayout.setVisibility(View.INVISIBLE);
            buttonAdd.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.INVISIBLE);
        }
    }

    private void initialisation() {
        symptomesListViewAdapter = new SymptomesListViewAdapter(
                getContext(),
                R.layout.spinner_region_list_item,
                R.id.textViewRegionListItem,
                rowItems);

        medicalStatusViewModel = ViewModelProviders.of(this)
                .get(MedicalStatusViewModel.class);
        medicalStatusTableAdapter = new MedicalStatusTableAdapter();
        medicalTableAdapter = new MedicalTableAdapter();

        recyclerViewSymptom.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerViewSymptom.setAdapter(medicalStatusTableAdapter);

        medSpinner.setTitle("Select Symptom");
        medSpinner.setPositiveButton("OK");
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

        setSymptomTable();
        onButtonSavePressed();
        setPatologySpinner();
        setTableMedical();
        onButtonSavePressed();
        onButtonAddPressed();
    }

    private void setSymptomTable() {
        if(tableLayout.getVisibility() == View.VISIBLE) {
            if(mParam2 == null) {
                medicalStatusTableAdapter
                        .setSymptomList(
                                medicalStatusViewModel.symptomList(
                                        medicalStatusViewModel.getIdConsultation()));
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

    private void setPatologySpinner() {
        rowItems.addAll(medicalStatusViewModel.getAllSymptomes());
        medSpinner.setPopupBackgroundResource(R.color.colorAccent);
        medSpinner.setAdapter(symptomesListViewAdapter);
        medSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj= adapterView.getItemAtPosition(i);
                Symptome symptome = (Symptome) obj;
                symptomeId = symptome.getIdSymptome();
                symptomeDescription = symptome.getDescription();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setTableMedical() {
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(medicalTableAdapter);
    }

    private void onButtonAddPressed() {
        buttonAdd.setOnClickListener(view -> {
            hideKeyboard(getActivity());
            medicalTableAdapter.setSymptomeList(new Symptome(
                    symptomeId,
                    "",
                    symptomeDescription,
                    ""
            ));
            medicalTableAdapter.notifyDataSetChanged();
        });
    }

    private void onButtonSavePressed() {
        buttonSave.setOnClickListener(view -> {
            if(!medicalTableAdapter.symptomesPatients.isEmpty()) {
                showAlertDialogButtonClicked();
            }
            else {
                Toast.makeText(
                        getContext(),
                        "Enter all the fields!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // close keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if(view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    // dialog alert
    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure that you want to save the data?");

        builder.setPositiveButton("Continue", (dialogInterface, i) -> {
            medicalStatusViewModel
                    .insertValues(
                            medicalTableAdapter.symptomesPatients);
            linearLayout.setVisibility(View.INVISIBLE);
            buttonAdd.setVisibility(View.INVISIBLE);
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