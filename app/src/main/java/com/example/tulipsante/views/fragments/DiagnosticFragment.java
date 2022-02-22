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

import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.DiagnosticFragmentViewModel;
import com.example.tulipsante.views.adapters.DiagnosticSpinnerAdapter;
import com.example.tulipsante.views.adapters.DiagnosticStatusTableAdapter;
import com.example.tulipsante.views.adapters.PatologyTableAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String idConsultation;
    private String maladieId, maladieDescription;
    private List<Maladie> rowItems = new ArrayList<>();


    private LinearLayout linearLayout;
    private TableLayout tableLayout;
    private Spinner patologySpinner;
    private EditText editTextDiagnostic;
    private CardView buttonAdd, buttonSave;
    private RecyclerView recyclerView, recyclerViewDiagnostic;
    private TextView textViewNoData;

    // Adapter
    private DiagnosticSpinnerAdapter diagnosticSpinnerAdapter;
    private PatologyTableAdapter patologyTableAdapter;
    private DiagnosticStatusTableAdapter diagnosticStatusTableAdapter;

    // View Model
    private DiagnosticFragmentViewModel diagnosticFragmentViewModel;

    public DiagnosticFragment(String mParam1, String mParam2, String idConsultation) {
        this.mParam1 = mParam1;
        this.mParam2 = mParam2;
        this.idConsultation = idConsultation;
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
        return inflater.inflate(R.layout.fragment_diagnostic, container, false);
    }

    private void initViews(View view) {
        buttonSave = view.findViewById(R.id.buttonSave);
        patologySpinner = view.findViewById(R.id.patologySpinner);
        editTextDiagnostic = view.findViewById(R.id.editTextDiagnostic);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        recyclerView = view.findViewById(R.id.recyclerView);
        linearLayout = view.findViewById(R.id.linear1);
        tableLayout = view.findViewById(R.id.tabLayout1);
        recyclerViewDiagnostic = view.findViewById(R.id.recyclerViewDiagnostic);
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
        }
    }

    private void initialisation() {
        diagnosticSpinnerAdapter = new DiagnosticSpinnerAdapter(
                getContext(),
                R.layout.spinner_region_list_item
                ,R.id.textViewRegionListItem,rowItems);
        diagnosticFragmentViewModel = ViewModelProviders.of(this)
                .get(DiagnosticFragmentViewModel.class);
        patologyTableAdapter = new PatologyTableAdapter();
        diagnosticSpinnerAdapter.clear();
        diagnosticStatusTableAdapter = new DiagnosticStatusTableAdapter();
        recyclerViewDiagnostic.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerViewDiagnostic.setAdapter(diagnosticStatusTableAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        init();
        initialisation();

        setPatologySpinner();
        setTablePatology();
        setDiagnosticTable();
        onButtonSavePressed();
        onButtonAddPressed();
    }

    private void setDiagnosticTable() {
        if(tableLayout.getVisibility() == View.VISIBLE) {
            if(idConsultation == null) {
                diagnosticStatusTableAdapter
                        .setDiagnosticList(
                                diagnosticFragmentViewModel
                                        .diagnosticList(diagnosticFragmentViewModel.
                                                getIdConsultation()));
            }
            else {
                List<String> diagnosticList = diagnosticFragmentViewModel
                        .diagnosticList(idConsultation);
                if(!diagnosticList.isEmpty()) {
                    diagnosticStatusTableAdapter
                            .setDiagnosticList(diagnosticList);
                }
                else {
                    tableLayout.setVisibility(View.INVISIBLE);
                    textViewNoData.setVisibility(View.VISIBLE);
                }
            }
            diagnosticStatusTableAdapter.notifyDataSetChanged();
        }
    }

    private void setPatologySpinner() {
        rowItems.addAll(diagnosticFragmentViewModel.getMaladies());
        patologySpinner.setPopupBackgroundResource(R.color.colorAccent);
        patologySpinner.setAdapter(diagnosticSpinnerAdapter);
        patologySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj= adapterView.getItemAtPosition(i);
                Maladie maladie = (Maladie) obj;
                maladieId = maladie.getIdMaladie();
                maladieDescription = maladie.getDescription();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setTablePatology() {
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(patologyTableAdapter);
    }

    private void onButtonAddPressed() {
        buttonAdd.setOnClickListener(view -> {
            hideKeyboard(getActivity());
            patologyTableAdapter.setMaladies(new Maladie(
                    maladieId,
                    maladieDescription,
                    "",
                    ""
            ));
            patologyTableAdapter.notifyDataSetChanged();
        });
    }

    private void onButtonSavePressed() {
        buttonSave.setOnClickListener(view -> {
            if(!editTextDiagnostic.getText().toString().isEmpty()
                    || !patologyTableAdapter.diagnostics.isEmpty()) {
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
            diagnosticFragmentViewModel
                    .insertValues(
                            patologyTableAdapter.diagnostics,
                            editTextDiagnostic.getText().toString().trim());
            linearLayout.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.INVISIBLE);
            setDiagnosticTable();
            Toast.makeText(getContext(), "Added Successfully!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
