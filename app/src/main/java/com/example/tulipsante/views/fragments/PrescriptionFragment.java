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

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.models.Prescription;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.viewModel.PrescriptionFragmentViewModel;
import com.example.tulipsante.views.adapters.PrescriptionStatusTableAdapter;
import com.example.tulipsante.views.adapters.PrescriptionTableAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrescriptionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String idConsulation;
    // current date
    private final String myFormat = "dd/MM/yyyy";
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    private LinearLayout linearLayout;
    private TableLayout tableLayout;
    private EditText editTextMedicine, editTextDate;
    private CardView buttonSave;
    private RecyclerView recyclerView1;
    private TextView textViewNoData;

    // Adapters
    private PrescriptionTableAdapter prescriptionTableAdapter;
    // Adapter for the hidden table
    private PrescriptionStatusTableAdapter prescriptionStatusTableAdapter;

    // View Model
    private PrescriptionFragmentViewModel prescriptionFragmentViewModel;

    public PrescriptionFragment(String mParam1, String mParam2, String idConsultation) {
        this.mParam1 = mParam1;
        this.mParam2 = mParam2;
        this.idConsulation = idConsultation;
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
        return inflater.inflate(R.layout.fragment_prescription, container, false);
    }

    private void initViews(View view) {
        editTextMedicine = view.findViewById(R.id.editTextMedicine);
        editTextDate = view.findViewById(R.id.editTextDate);
        buttonSave = view.findViewById(R.id.buttonSave);
        linearLayout = view.findViewById(R.id.linear1);
        tableLayout = view.findViewById(R.id.tabLayout1);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
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
        prescriptionTableAdapter = new PrescriptionTableAdapter();
        prescriptionFragmentViewModel = ViewModelProviders.of(this)
                .get(PrescriptionFragmentViewModel.class);
        prescriptionStatusTableAdapter = new PrescriptionStatusTableAdapter();
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(prescriptionStatusTableAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        init();
        initialisation();

        setDate();
        setPrescriptionTable();
        onButtonSavePressed();
    }

    private void setDate() {
        Date date = calendar.getTime();
        editTextDate.setText(sdf.format(date));
    }

    private void setPrescriptionTable() {
        if(tableLayout.getVisibility() == View.VISIBLE) {
            if(idConsulation == null) {
                prescriptionStatusTableAdapter
                        .setPrescriptionList(
                                prescriptionFragmentViewModel
                                        .prescriptionList(prescriptionFragmentViewModel
                                                .getIdConsultation()
                                        )
                        );
            }
            else {
                List<Prescription> prescriptionList =
                        prescriptionFragmentViewModel.prescriptionList(idConsulation);
                if(!prescriptionList.isEmpty()) {
                    prescriptionStatusTableAdapter
                            .setPrescriptionList(prescriptionList);
                }
                else {
                    tableLayout.setVisibility(View.INVISIBLE);
                    textViewNoData.setVisibility(View.VISIBLE);
                }
            }
            prescriptionStatusTableAdapter.notifyDataSetChanged();
        }
    }

    private void onButtonSavePressed() {
        buttonSave.setOnClickListener(view -> {
            hideKeyboard(getActivity());
            if(!editTextMedicine.getText().toString().isEmpty()) {
                showAlertDialogButtonClicked();
            }
            else {
                Toast.makeText(
                        getContext(),
                        "Enter data before saving",
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
            prescriptionTableAdapter.setPrescription(new Prescription(
                    GeneralPurposeFunctions.idTable(),
                    "",
                    editTextDate.getText().toString().trim(),
                    editTextMedicine.getText().toString().trim(),
                    ""
            ));
            prescriptionFragmentViewModel
                    .insertValues(prescriptionTableAdapter.prescriptions);
            prescriptionTableAdapter.notifyDataSetChanged();
            linearLayout.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.INVISIBLE);
            setPrescriptionTable();
            Toast.makeText(getContext(), "Enregistrer!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}