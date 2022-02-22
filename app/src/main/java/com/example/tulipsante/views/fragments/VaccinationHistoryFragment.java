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
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.tulipsante.models.uIModels.VaccinePatient;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.VaccinationHistoryViewModel;
import com.example.tulipsante.views.adapters.VaccineHistoryTableAdapter;

import java.util.ArrayList;
import java.util.List;


public class VaccinationHistoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String[] itemsDate = {
            "Valid",
    };
    private List<VaccinePatient> vaccineList = new ArrayList<>();

    // Views
    private RecyclerView recViewVaccine;
    private Spinner spinnerSort;
    private EditText editTextSearch;
    private LinearLayout linearLayout;
    private TableLayout tableLayout;
    private TextView textViewNoData;

    // Adapters
    private ArrayAdapter<String> adapterSpinner;
    private VaccineHistoryTableAdapter vaccineHistoryTableAdapter;

    // View Model
    private VaccinationHistoryViewModel vaccinationHistoryViewModel;

    public VaccinationHistoryFragment() {
        // Required empty public constructor
    }

    public static VaccinationHistoryFragment newInstance(String param1, String param2) {
        VaccinationHistoryFragment fragment = new VaccinationHistoryFragment();
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
        return inflater.inflate(R.layout.fragment_image_analysis_patient, container, false);
    }

    private void initViews(View view) {
        recViewVaccine = view.findViewById(R.id.recViewVaccination);
        spinnerSort = view.findViewById(R.id.spinner1);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        tableLayout = view.findViewById(R.id.tabLayout);
        linearLayout = view.findViewById(R.id.linearLayout1);
        textViewNoData = view.findViewById(R.id.textViewNoData);
    }

    private void initialisation() {
        adapterSpinner = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, itemsDate);
        vaccinationHistoryViewModel = ViewModelProviders
                .of(this)
                .get(VaccinationHistoryViewModel.class);
        vaccineHistoryTableAdapter = new VaccineHistoryTableAdapter(getFragmentManager(), getContext());
        recViewVaccine.setHasFixedSize(true);
        recViewVaccine.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recViewVaccine.setAdapter(vaccineHistoryTableAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setSortSpinner();
        onSortSpinnerItemsPressed();
        setVaccineHistory();
        onSearchButtonPressed();
    }

    private void setSortSpinner() {
        spinnerSort.setPopupBackgroundResource(R.color.colorAccent);
        spinnerSort.setAdapter(adapterSpinner);
    }

    private void onSortSpinnerItemsPressed() {
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object object = adapterView.getItemAtPosition(i);
                String val = (String) object;
                vaccineList.clear();
                new Thread(() -> {
                    vaccineList.addAll(vaccinationHistoryViewModel.getVaccinationHistory(val));
                    getActivity().runOnUiThread(() -> {
                        if(vaccineList.size() <= 0) {
                            linearLayout.setVisibility(View.INVISIBLE);
                            tableLayout.setVisibility(View.INVISIBLE);
                            textViewNoData.setVisibility(View.VISIBLE);
                        }
                        vaccineHistoryTableAdapter.notifyDataSetChanged();
                    });
                }).start();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setVaccineHistory() {
        vaccineHistoryTableAdapter.setVaccinePatientList(vaccineList);
        vaccineHistoryTableAdapter.notifyDataSetChanged();
    }

    private void onSearchButtonPressed() {
        editTextSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_SEARCH) {
                vaccineList.clear();
                vaccineList.addAll(vaccinationHistoryViewModel.getVaccinationHistory(textView.getText().toString()));
                vaccineHistoryTableAdapter.notifyDataSetChanged();
                return true;
            }
            return false;
        });
    }
}