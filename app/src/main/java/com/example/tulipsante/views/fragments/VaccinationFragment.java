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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.models.TypeVaccination;
import com.example.tulipsante.models.uIModels.VaccinationData;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.VaccinationFragmentViewModel;
import com.example.tulipsante.views.adapters.VaccinationStatusTableAdapter;
import com.example.tulipsante.views.adapters.VaccineSpinnerAdapter;
import com.example.tulipsante.views.adapters.VaccineTableAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VaccinationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2, id, description, duree, idConsultation;
    private List<TypeVaccination> rowItems = new ArrayList<>();
    // Current date
    private final String myFormat = "yyyy/MM/dd hh:MM:ss";
    private Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private Date date;

    private LinearLayout linearLayout;
    private TableLayout tableLayout;
    private EditText editTextDate;
    private CardView cardViewSave, buttonAdd;
    private Spinner vaccineSpinner;
    private RecyclerView recViewVac, recyclerView;
    private TextView textViewNoData;

    // Adapters
    private VaccineTableAdapter vaccineTableAdapter;
    private VaccineSpinnerAdapter vaccineSpinnerAdapter;
    private VaccinationStatusTableAdapter vaccinationStatusTableAdapter;

    // View Model
    private VaccinationFragmentViewModel vaccinationFragmentViewModel;

    public VaccinationFragment(String mParam1, String mParam2, String idConsultation) {
        this.mParam1 = mParam1;
        this.mParam2 = mParam2;
        this.idConsultation = idConsultation;
    }

    public static VaccinationFragment newInstance(String param1, String param2) {
        VaccinationFragment fragment = new VaccinationFragment(
                "","", "");
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
        return inflater.inflate(R.layout.fragment_vaccination, container, false);
    }

    private void initViews(View view) {
        cardViewSave = view.findViewById(R.id.buttonSave);
        linearLayout = view.findViewById(R.id.linear1);
        tableLayout = view.findViewById(R.id.tabLayout1);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        recyclerView = view.findViewById(R.id.recyclerView);
        editTextDate = view.findViewById(R.id.editTextDate);
        vaccineSpinner = view.findViewById(R.id.vaccineSpinner);
        recViewVac = view.findViewById(R.id.recViewVac);
        textViewNoData = view.findViewById(R.id.textViewNoData);
    }

    private void init() {
        if(mParam2.equals("Patient List")) {
            linearLayout.setVisibility(View.VISIBLE);
            tableLayout.setVisibility(View.INVISIBLE);
        }
        else {
            linearLayout.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.VISIBLE);
            cardViewSave.setVisibility(View.INVISIBLE);
        }
    }

    private void initialisation() {
        vaccineSpinnerAdapter = new VaccineSpinnerAdapter(
                getContext(),
                R.layout.spinner_region_list_item
                ,R.id.textViewRegionListItem,rowItems);
        vaccineSpinnerAdapter.clear();
        vaccinationFragmentViewModel = ViewModelProviders.of(this)
                .get(VaccinationFragmentViewModel.class);
        vaccineTableAdapter = new VaccineTableAdapter();
        vaccinationStatusTableAdapter = new VaccinationStatusTableAdapter();
        recViewVac.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recViewVac.setAdapter(vaccinationStatusTableAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        init();
        initialisation();

        setDate();
        setTableVaccine();
        setVaccineTable();
        setVaccineSpinner();
        onButtonSavePressed();
        onButtonAddPressed();
    }

    private void setVaccineTable() {
        if(tableLayout.getVisibility() == View.VISIBLE) {
            if(idConsultation == null) {
                vaccinationStatusTableAdapter
                        .setVaccinationList(
                                vaccinationFragmentViewModel
                                        .vaccinationList(vaccinationFragmentViewModel.
                                                getIdConsultation()));
            }
            else {
                List<VaccinationData> vaccinationDataList =
                        vaccinationFragmentViewModel
                                .vaccinationList(idConsultation);
                if(!vaccinationDataList.isEmpty()) {
                    vaccinationStatusTableAdapter
                            .setVaccinationList(vaccinationDataList);
                }
                else {
                    tableLayout.setVisibility(View.INVISIBLE);
                    textViewNoData.setVisibility(View.VISIBLE);
                }
            }
            vaccinationStatusTableAdapter.notifyDataSetChanged();
        }
    }

    private void setVaccineSpinner() {
        rowItems.addAll(vaccinationFragmentViewModel.getVaccines());
        vaccineSpinner.setPopupBackgroundResource(R.color.colorAccent);
        vaccineSpinner.setAdapter(vaccineSpinnerAdapter);
        vaccineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj= adapterView.getItemAtPosition(i);
                TypeVaccination typeVaccination = (TypeVaccination) obj;
                id = typeVaccination.getIdTypeVaccination();
                description = typeVaccination.getType();
                duree = typeVaccination.getDuree();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setDate() {
        date = calendar.getTime();
        editTextDate.setText(sdf.format(date));
    }

    private void setTableVaccine() {
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(vaccineTableAdapter);
    }

    private void onButtonAddPressed() {
        buttonAdd.setOnClickListener(view -> {
            hideKeyboard(getActivity());
            vaccineTableAdapter.setTypeVaccinations(new TypeVaccination(
                    id,
                    description,
                    duree,
                    ""
            ));
            vaccineTableAdapter.notifyDataSetChanged();
        });
    }

    private void onButtonSavePressed() {
        cardViewSave.setOnClickListener(view -> {
            hideKeyboard(getActivity());
            if(!vaccineTableAdapter.vaccinationPatients.isEmpty()) {
                showAlertDialogButtonClicked();
            }
            else {
                Toast.makeText(
                        getContext(),
                        "Enter data before saving",
                        Toast.LENGTH_SHORT).show();
            }        });
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
            vaccinationFragmentViewModel
                    .insertValues(vaccineTableAdapter.vaccinationPatients,date.toString());
            vaccineTableAdapter.notifyDataSetChanged();
            linearLayout.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.VISIBLE);
            cardViewSave.setVisibility(View.INVISIBLE);
            setVaccineTable();
            Toast.makeText(getContext(), "Enregistrer!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}