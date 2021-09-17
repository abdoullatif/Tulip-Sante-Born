package com.example.tulipsante.views.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
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

import com.example.tulipsante.models.CategorieSigneVitaux;
import com.example.tulipsante.models.uIModels.SignesVitauxXCatSignesVitaux;
import com.example.tulipsante.models.uIModels.TempVitalValue;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.VitalsSymptomFragmentViewModel;
import com.example.tulipsante.views.activities.ConsultationToolsActivity;
import com.example.tulipsante.views.adapters.VitalsSpinnerAdapter;
import com.example.tulipsante.views.adapters.VitalsStatusTableAdapter;
import com.example.tulipsante.views.adapters.VitalsTableAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VitalsSymptomFragment extends Fragment {
    private static final int LAUNCH_SECOND_ACTIVITY = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String idConsultation;
    private List<CategorieSigneVitaux> rowItemCSV = new ArrayList<>();
    private String catSVId;
    private String catDescription;
    private String catValue;

    private CardView buttonTools, buttonAddVital, buttonSave;
    private Spinner vitalSpinner;
    private RecyclerView recyclerViewValue, recViewIn;
    private TableLayout tableLayout;
    private LinearLayout linearLayout;
    private EditText editTextCatValue;
    private TextView textViewNoData;

    // Adapter
    private VitalsSpinnerAdapter vitalsSpinnerAdapter;
    private VitalsTableAdapter vitalsTableAdapter;
    private VitalsStatusTableAdapter vitalsStatusTableAdapter;

    // View Model
    private VitalsSymptomFragmentViewModel consultationFragmentViewModel;

    private View.OnClickListener listener;

    private SharedPreferences sharedPreferences;


    public VitalsSymptomFragment(String mParam1, String mParam2, String idConsultation) {
        this.mParam1 = mParam1;
        this.mParam2 = mParam2;
        this.idConsultation = idConsultation;
    }

    private void initViews(View view) {
        buttonTools = view.findViewById(R.id.buttonTools);
        buttonSave = view.findViewById(R.id.buttonSave);
        vitalSpinner = view.findViewById(R.id.vitalSpinner);
        recyclerViewValue = view.findViewById(R.id.recyclerViewValue);
        buttonAddVital = view.findViewById(R.id.buttonAddVital);
        editTextCatValue = view.findViewById(R.id.editTextValue);
        recViewIn = view.findViewById(R.id.recViewIn);
        tableLayout = view.findViewById(R.id.tabLayout1);
        linearLayout = view.findViewById(R.id.linear1);
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
            buttonSave.setVisibility(View.INVISIBLE);
            buttonTools.setVisibility(View.INVISIBLE);
        }
    }

    private void initialisation() {
        consultationFragmentViewModel = ViewModelProviders
                .of(this)
                .get(VitalsSymptomFragmentViewModel.class);
        vitalsSpinnerAdapter = new VitalsSpinnerAdapter(
                getContext(),
                R.layout.spinner_region_list_item
                ,R.id.textViewRegionListItem,rowItemCSV);
        vitalsTableAdapter = new VitalsTableAdapter();
        vitalsSpinnerAdapter.clear();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        vitalsStatusTableAdapter = new VitalsStatusTableAdapter();
        recViewIn.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recViewIn.setAdapter(vitalsStatusTableAdapter);

        // get vital signs
        rowItemCSV.addAll(consultationFragmentViewModel.getSignesVitaux());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (View.OnClickListener) context;

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
        return inflater.inflate(
                R.layout.fragment_vitals_symptom,
                container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        init();
        initialisation();

        setVitalSpinner();
        setTableVital();
        onButtonToolsPressed();
        onButtonAddVitalsPressed();
        onButtonSavePressed();
        setVitalsStatusTable();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                if(sharedPreferences.getString("temp", null) != null) {
                    catValue = sharedPreferences.getString("temp", null);
                    assert catValue != null;
                    if (!catValue.isEmpty()) {
                        List<CategorieSigneVitaux> sV =
                                rowItemCSV
                                        .stream()
                                        .filter(o -> o.getDescription()
                                                .contains("Temperature (°C)"))
                                        .collect(Collectors.toList());
                        try {
                            vitalsTableAdapter.setTempVitalValue(new TempVitalValue(
                                    sV.get(0).getIdCatSV(),
                                    sV.get(0).getDescription(),
                                    catValue.substring(0,2)
                            ));
                        }
                        catch (Exception e) {
                            Toast.makeText(
                                    getContext(),
                                    "Empty Temperature!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        vitalsTableAdapter.notifyDataSetChanged();
                        editTextCatValue.setText("");
                    }
                }
                else if(sharedPreferences.getString("spo2", null) != null) {
                    String spo2 = sharedPreferences.getString("spo2", null);
                    String pi = sharedPreferences.getString("pi", null);
                    String pr = sharedPreferences.getString("pr", null);
                    assert spo2 != null;
                    assert pi != null;
                    assert pr != null;
                    if (!spo2.isEmpty() && !pi.isEmpty() && !pr.isEmpty()) {
                        List<CategorieSigneVitaux> sV =
                                rowItemCSV
                                        .stream()
                                        .filter(o -> o.getDescription()
                                                .contains("Spo2"))
                                        .collect(Collectors.toList());
                        List<CategorieSigneVitaux> sV1 =
                                rowItemCSV
                                        .stream()
                                        .filter(o -> o.getDescription()
                                                .contains("Pi"))
                                        .collect(Collectors.toList());
                        List<CategorieSigneVitaux> sV2 =
                                rowItemCSV
                                        .stream()
                                        .filter(o -> o.getDescription()
                                                .contains("Pr"))
                                        .collect(Collectors.toList());
                        try {
                            vitalsTableAdapter.setTempVitalValue(new TempVitalValue(
                                    sV.get(0).getIdCatSV(),
                                    sV.get(0).getDescription(),
                                    spo2.substring(0,2)
                            ));
                            vitalsTableAdapter.setTempVitalValue(new TempVitalValue(
                                    sV1.get(0).getIdCatSV(),
                                    sV1.get(0).getDescription(),
                                    pi
                            ));
                            vitalsTableAdapter.setTempVitalValue(new TempVitalValue(
                                    sV2.get(0).getIdCatSV(),
                                    sV2.get(0).getDescription(),
                                    pr.substring(0,2)
                            ));
                        }
                        catch (Exception e) {
                            Toast.makeText(
                                    getContext(),
                                    e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        vitalsTableAdapter.notifyDataSetChanged();
                        editTextCatValue.setText("");
                    }
                }
            }
        }
    }

    private void setVitalsStatusTable() {
        if(tableLayout.getVisibility() == View.VISIBLE) {
            if(idConsultation == null) {
                vitalsStatusTableAdapter
                        .setSignesVitauxList(
                                consultationFragmentViewModel
                                        .vitalsSymptomList(consultationFragmentViewModel.
                                                getIdConsultation()));
            }
            else {
                List<SignesVitauxXCatSignesVitaux> signesVitaux = consultationFragmentViewModel
                        .vitalsSymptomList(idConsultation);
                if(!signesVitaux.isEmpty()) {
                    vitalsStatusTableAdapter
                            .setSignesVitauxList(signesVitaux);
                }
                else {
                    tableLayout.setVisibility(View.INVISIBLE);
                    textViewNoData.setVisibility(View.VISIBLE);
                }
            }
            vitalsStatusTableAdapter.notifyDataSetChanged();
        }
    }

    private void onButtonSavePressed() {
        buttonSave.setOnClickListener(view -> {
            if(vitalsTableAdapter.signesVitaux.isEmpty()) {
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

    private void onButtonAddVitalsPressed() {
        buttonAddVital.setOnClickListener(view -> {
            hideKeyboard(getActivity());
            catValue = editTextCatValue.getText().toString().trim();
            if(!catValue.isEmpty()) {
                vitalsTableAdapter.setTempVitalValue(new TempVitalValue(
                        catSVId,
                        catDescription,
                        catValue
                ));
                vitalsTableAdapter.notifyDataSetChanged();
                editTextCatValue.setText("");
            }
        });
    }

    private void setVitalSpinner() {
        vitalSpinner.setPopupBackgroundResource(R.color.colorAccent);
        vitalSpinner.setAdapter(vitalsSpinnerAdapter);
        vitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj= adapterView.getItemAtPosition(i);
                CategorieSigneVitaux categorieSigneVitaux = (CategorieSigneVitaux) obj;
                catSVId = categorieSigneVitaux.getIdCatSV();
                catDescription = categorieSigneVitaux.getDescription();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setTableVital() {
        recyclerViewValue.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,false));
        recyclerViewValue.setHasFixedSize(true);
        recyclerViewValue.setAdapter(vitalsTableAdapter);
    }

    private void onButtonToolsPressed() {
        buttonTools.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ConsultationToolsActivity.class);
            intent.putExtra("From", "Vitals");
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
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
            if(!vitalsTableAdapter.contains("Poids (Kg)") || !vitalsTableAdapter.contains("Taille (cm)")) {
                Toast.makeText(getContext(),"Enter Weight and height!",Toast.LENGTH_LONG).show();
            }
            else {
                consultationFragmentViewModel
                        .insertValues(vitalsTableAdapter.signesVitaux);
                linearLayout.setVisibility(View.INVISIBLE);
                tableLayout.setVisibility(View.VISIBLE);
                buttonSave.setVisibility(View.INVISIBLE);
                buttonTools.setVisibility(View.INVISIBLE);
                setVitalsStatusTable();
                Toast.makeText(getContext(), "Signes Vitaux Enregistrer!",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}