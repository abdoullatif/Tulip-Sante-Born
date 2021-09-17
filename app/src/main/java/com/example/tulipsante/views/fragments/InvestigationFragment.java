package com.example.tulipsante.views.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
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

import com.example.tulipsante.models.TypeExamens;
import com.example.tulipsante.models.uIModels.ExamenXTypeExamen;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.InvestigationFragmentViewModel;
import com.example.tulipsante.views.activities.ConsultationToolsActivity;
import com.example.tulipsante.views.adapters.ExamSpinnerAdapter;
import com.example.tulipsante.views.adapters.ExamTableAdapter;
import com.example.tulipsante.views.adapters.InvestigationStatusTableAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class InvestigationFragment extends Fragment {
    private static final int LAUNCH_SECOND_ACTIVITY = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String typeExamensId, typeExamenType, idConsultation;
    private List<TypeExamens> rowItems = new ArrayList<>();
    // current date
    private final String myFormat = "yyyy/MM/dd";
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    private SharedPreferences sharedPreferences;

    // Views
    private CardView buttonTools, buttonSave;
    private Spinner examSpinner;
    private EditText dateEditText;
    private CardView buttonAdd;
    private RecyclerView recyclerView, recViewIn;
    private TableLayout tableLayout;
    private LinearLayout linearLayout;
    private TextView textViewNoData;

    // Adapter
    private ExamSpinnerAdapter examSpinnerAdapter;
    private ExamTableAdapter examTableAdapter;
    private InvestigationStatusTableAdapter investigationStatusTableAdapter;

    // View Model
    private InvestigationFragmentViewModel investigationFragmentViewModel;

    public InvestigationFragment(String mParam1, String mParam2, String idConsultation) {
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
        return inflater.inflate(R.layout.fragment_investigation, container, false);
    }

    private void initViews(View view) {
        buttonTools = view.findViewById(R.id.buttonTools);
        buttonSave = view.findViewById(R.id.buttonSave);
        examSpinner = view.findViewById(R.id.examSpinner);
        dateEditText = view.findViewById(R.id.editTextDate);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        recyclerView = view.findViewById(R.id.recyclerView);
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
        investigationStatusTableAdapter = new InvestigationStatusTableAdapter(getFragmentManager(),getContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getContext()));
        examSpinnerAdapter = new ExamSpinnerAdapter(
                getContext(),
                R.layout.spinner_region_list_item
                ,R.id.textViewRegionListItem,rowItems);
        investigationFragmentViewModel = ViewModelProviders.of(this)
                .get(InvestigationFragmentViewModel.class);
        examTableAdapter = new ExamTableAdapter();
        examSpinnerAdapter.clear();
        recViewIn.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recViewIn.setAdapter(investigationStatusTableAdapter);

        // add type examen to the spinner
        rowItems.addAll(investigationFragmentViewModel.getTypeExams());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        init();
        initialisation();

        setDate();
        onButtonToolsPressed();
        setExamSpinner();
        setTableExam();
        onButtonSavePressed();
        onButtonAddPressed();
        setInvestigationTable();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                if(sharedPreferences.getStringSet("ult", null) != null) {
                    List<TypeExamens> tE =
                            rowItems
                                    .stream()
                                    .filter(o -> o.getTypeExamens()
                                            .equals("Ultrasound"))
                                    .collect(Collectors.toList());
                    Set<String> path = sharedPreferences.getStringSet("ult", null);
                    assert path != null;
                    if (!path.isEmpty()) {
                        for (String s : path) {
                            try {
                                examTableAdapter.setTypeExamens(new TypeExamens(
                                        tE.get(0).getIdTypeExamens(),
                                        tE.get(0).getTypeExamens(),
                                        s,
                                        ""
                                ));
                            } catch (Exception e) {
                                Toast.makeText(
                                        getContext(),
                                        "Empty ultrasound!", Toast.LENGTH_SHORT).show();
                            }
                            examTableAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    }

    private void setInvestigationTable() {
        if(tableLayout.getVisibility() == View.VISIBLE) {
            if(idConsultation == null) {
                investigationStatusTableAdapter
                        .setInvestigationList(
                                investigationFragmentViewModel
                                        .investigationList(investigationFragmentViewModel.
                                                getIdConsultation()));
            }
            else {
                List<ExamenXTypeExamen> examenList = investigationFragmentViewModel
                        .investigationList(idConsultation);
                if(!examenList.isEmpty()) {
                    investigationStatusTableAdapter
                            .setInvestigationList(examenList);
                }
                else {
                    tableLayout.setVisibility(View.INVISIBLE);
                    textViewNoData.setVisibility(View.VISIBLE);
                }
            }
            investigationStatusTableAdapter.notifyDataSetChanged();
        }
    }

    private void setDate() {
        Date date = calendar.getTime();
        dateEditText.setText(sdf.format(date));
    }

    private void onButtonToolsPressed() {
        buttonTools.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ConsultationToolsActivity.class);
            intent.putExtra("From", "Investigation");
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
        });
    }

    private void setExamSpinner() {
        examSpinner.setPopupBackgroundResource(R.color.colorAccent);
        examSpinner.setAdapter(examSpinnerAdapter);
        examSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj= adapterView.getItemAtPosition(i);
                TypeExamens typeExamens = (TypeExamens) obj;
                typeExamensId = typeExamens.getIdTypeExamens();
                typeExamenType = typeExamens.getTypeExamens();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setTableExam() {
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(examTableAdapter);
    }

    private void onButtonAddPressed() {
        buttonAdd.setOnClickListener(view -> {
            hideKeyboard(getActivity());
            examTableAdapter.setTypeExamens(new TypeExamens(
                    typeExamensId,
                    typeExamenType,
                    "",
                    ""
            ));
            examTableAdapter.notifyDataSetChanged();
        });
    }

    private void onButtonSavePressed() {
        buttonSave.setOnClickListener(view -> {
            if(examTableAdapter.examen.isEmpty()) {
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
            investigationFragmentViewModel
                    .insertValues(examTableAdapter.examen);
            linearLayout.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.INVISIBLE);
            buttonTools.setVisibility(View.INVISIBLE);
            setInvestigationTable();
            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();

        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}