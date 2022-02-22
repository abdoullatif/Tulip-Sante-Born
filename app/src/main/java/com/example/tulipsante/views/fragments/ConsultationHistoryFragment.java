package com.example.tulipsante.views.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tulipsante.models.uIModels.ConsultationUMedecin;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.Paginator;
import com.example.tulipsante.viewModel.ConsultationHistoryViewModel;
import com.example.tulipsante.views.adapters.ConsultationHistoryTableAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ConsultationHistoryFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private List<String> basicData = new ArrayList<>();
    private final String myFormat = "dd/MM/yyyy";
    final Calendar calendar = Calendar.getInstance();
    private List<ConsultationUMedecin> consultationList = new ArrayList<>();
    private int numOfConsultation;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentIndex = 0;
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;
    private int counter = 15;

    private String spinnerValue;

    private String[] itemsDate = {
            "Newest",
            "Oldest",
    };

    // View
    private RecyclerView recViewConsultation;
    private Spinner spinnerSort;
    private EditText editTextSearchDate;
    private SwipeRefreshLayout swipeRefreshLayout;

    // View Model
    private ConsultationHistoryViewModel consultationHistoryViewModel;

    // Adapter
    private ConsultationHistoryTableAdapter consultationHistoryTableAdapter;
    private ArrayAdapter<String> adapterSpinner;

    private LinearLayoutManager linearLayoutManager;

    public ConsultationHistoryFragment() {
        // empty constructor
    }

    public ConsultationHistoryFragment(List<String> basicData) {
        this.basicData = basicData;
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
        return inflater.inflate(R.layout.fragment_consultation_history,
                container, false);
    }

    private void initViews(View view) {
        recViewConsultation = view.findViewById(R.id.recViewConsultations);
        spinnerSort = view.findViewById(R.id.spinner1);
        editTextSearchDate = view.findViewById(R.id.editTextSearch);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
    }

    private void initialisation() {
        consultationHistoryViewModel = ViewModelProviders
                .of(this)
                .get(ConsultationHistoryViewModel.class);
        adapterSpinner = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                R.layout.spinner_item, itemsDate);
        consultationHistoryTableAdapter = new ConsultationHistoryTableAdapter(
                basicData, Objects.requireNonNull(getActivity()).getSupportFragmentManager(), getActivity());

        TOTAL_PAGES = (int) Math.ceil(Double.parseDouble(consultationHistoryViewModel.getNumberOfRecord()) / 15);

        swipeRefreshLayout.setOnRefreshListener(this);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recViewConsultation.setLayoutManager(linearLayoutManager);
        recViewConsultation.setHasFixedSize(true);
        recViewConsultation.setAdapter(consultationHistoryTableAdapter);
    }

    private void setScrollListener() {
        recViewConsultation.addOnScrollListener(new Paginator(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(() -> loadNextPage(), 500);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void loadFirstPage() {
        consultationHistoryTableAdapter.clear();
        consultationHistoryViewModel.setFilter(String.valueOf(currentIndex));

        if (currentPage < TOTAL_PAGES) {
            consultationHistoryTableAdapter.addLoadingFooter();
        } else if (TOTAL_PAGES == 1) {
            new Handler().postDelayed(() -> {
                consultationHistoryTableAdapter.addLoadingFooter();
                consultationHistoryTableAdapter.removeLoadingFooter();
            }, 500);
        } else isLastPage = true;
    }

    private void loadNextPage() {
        if (consultationHistoryTableAdapter.getItemCount() >= counter) {
            consultationHistoryTableAdapter.removeLoadingFooter();

            String index = String.valueOf(currentIndex + 15);
            consultationHistoryViewModel.setFilter(index);

            if (currentPage != TOTAL_PAGES) {
                consultationHistoryTableAdapter.addLoadingFooter();
            } else {
                isLastPage = true;
            }
            isLoading = false;
            currentIndex += 15;
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setScrollListener();
        openCalendarDialog();
        setSortSpinner();
        onSortSpinnerItemsPressed();
        setConsultationHistory();
    }

    @Override
    public void onRefresh() {
        currentIndex = 0;
        currentPage = 1;
        isLoading = false;
        isLastPage = false;
        editTextSearchDate.setText("");
        new Handler().postDelayed(this::loadFirstPage, 1000);
        new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 2000);
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
                spinnerValue = (String) object;
                consultationList.clear();
                loadFirstPage();
                consultationHistoryTableAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setConsultationHistory() {
        consultationHistoryViewModel.getNumberOfConsRecord()
                .observe(this, historiqueTacheMedecins -> {
                    consultationList.addAll(historiqueTacheMedecins);
                    consultationHistoryTableAdapter.setConsultationList(consultationList);
                    consultationHistoryTableAdapter.notifyDataSetChanged();
                });
        loadFirstPage();
    }

    private void openCalendarDialog() {
        String myFormat = "yyyy/MM/dd";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        final DatePickerDialog.OnDateSetListener date = (datePicker, i, i1, i2) -> {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);

            consultationList.clear();
            consultationHistoryTableAdapter.clear();
            consultationHistoryViewModel.setFilter("%" + sdf.format(calendar.getTime()) + "%");
            consultationHistoryTableAdapter.addLoadingFooter();
            consultationHistoryTableAdapter.removeLoadingFooter();

            editTextSearchDate.setText(sdf.format(calendar.getTime()));
        };
        editTextSearchDate.setOnClickListener(view -> new DatePickerDialog(
                Objects.requireNonNull(getContext()),
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
    }
}