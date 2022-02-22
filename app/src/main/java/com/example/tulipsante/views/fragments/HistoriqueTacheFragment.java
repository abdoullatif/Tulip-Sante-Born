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
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.tulipsante.models.uIModels.HistoriqueTacheMedecin;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.Paginator;
import com.example.tulipsante.viewModel.HistoriqueTacheViewModel;
import com.example.tulipsante.views.adapters.HistoriqueConsultationTableAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HistoriqueTacheFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private final String myFormat = "yyyy/MM/dd";
    final Calendar calendar = Calendar.getInstance();
    private List<HistoriqueTacheMedecin> historiqueTacheMedecinList = new ArrayList<>();

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentIndex = 0;
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;
    private int counter = 15;

    // Views
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private EditText editTextSearchDate;
    private TextView textViewNoData;
    private TableLayout tableLayout;

    // View Model
    private HistoriqueTacheViewModel historiqueTacheViewModel;

    // Adapter
    private HistoriqueConsultationTableAdapter historiqueConsultationTableAdapter;

    private LinearLayoutManager linearLayoutManager;

    public HistoriqueTacheFragment() {
        // Required empty public constructor
    }

    public static HistoriqueTacheFragment newInstance(String param1, String param2) {
        HistoriqueTacheFragment fragment = new HistoriqueTacheFragment();
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
        return inflater.inflate(R.layout.fragment_historique_tache, container, false);
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recViewTaches);
        editTextSearchDate = view.findViewById(R.id.editTextSearch);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        textViewNoData = view.findViewById(R.id.textViewNoData);
        tableLayout = view.findViewById(R.id.tabLayout1);
    }

    private void initialisation() {
        historiqueTacheViewModel = ViewModelProviders
                .of(this)
                .get(HistoriqueTacheViewModel.class);
//        adapterSpinner = new ArrayAdapter<>(getContext(),
//                R.layout.spinner_item,itemsDate);
        historiqueConsultationTableAdapter = new HistoriqueConsultationTableAdapter();
        swipeRefreshLayout.setOnRefreshListener(this);
        TOTAL_PAGES = (int) Math.ceil(Double.parseDouble(historiqueTacheViewModel.getNumberOfRecord()) / 15);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(historiqueConsultationTableAdapter);

    }

    private void setScrollListener() {
        recyclerView.addOnScrollListener(new Paginator(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(() -> loadNextPage(),500);
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
        historiqueConsultationTableAdapter.clear();
        historiqueTacheViewModel.setFilter(String.valueOf(currentIndex));

        if (currentPage < TOTAL_PAGES) {
            historiqueConsultationTableAdapter.addLoadingFooter();
        }
        else if(TOTAL_PAGES == 1) {
            new Handler().postDelayed(() -> {
                historiqueConsultationTableAdapter.addLoadingFooter();
                historiqueConsultationTableAdapter.removeLoadingFooter();
            },500);
        }
        else isLastPage = true;
    }

    private void loadNextPage() {
        if(historiqueConsultationTableAdapter.getItemCount() >= counter) {
            historiqueConsultationTableAdapter.removeLoadingFooter();

            String index = String.valueOf(currentIndex + 15);
            historiqueTacheViewModel.setFilter(index);

            if (currentPage != TOTAL_PAGES) {
                historiqueConsultationTableAdapter.addLoadingFooter();
            }
            else {
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
        setTableHistory();
    }

    @Override
    public void onRefresh() {
        currentIndex = 0;
        currentPage = 1;
        isLoading = false;
        isLastPage = false;
        editTextSearchDate.setText("");
        loadFirstPage();
        new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false),2000);
    }


    private void setTableHistory() {
        historiqueTacheViewModel.getHistoriqueTache()
                .observe(this, historiqueTacheMedecins -> {
                    if(historiqueTacheMedecins.size() <= 0) {
                        // hide views and show no data
                        textViewNoData.setVisibility(View.VISIBLE);
                        tableLayout.setVisibility(View.INVISIBLE);
                        editTextSearchDate.setVisibility(View.INVISIBLE);
                    }
                    else {
                        // set views visible
                        tableLayout.setVisibility(View.VISIBLE);
                        editTextSearchDate.setVisibility(View.VISIBLE);
                        // pass data to adapter
                        historiqueTacheMedecinList.addAll(historiqueTacheMedecins);
                        historiqueConsultationTableAdapter.setHistoriqueTacheMedecinList(historiqueTacheMedecinList);
                        historiqueConsultationTableAdapter.notifyDataSetChanged();
                    }
                });
        loadFirstPage();
    }

    private void openCalendarDialog() {
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        final DatePickerDialog.OnDateSetListener date = (datePicker, i, i1, i2) -> {
            calendar.set(Calendar.YEAR,i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DAY_OF_MONTH,i2);

            historiqueTacheMedecinList.clear();
            historiqueConsultationTableAdapter.clear();
            historiqueTacheViewModel.setFilter("%"+ sdf.format(calendar.getTime()) + "%");
            historiqueConsultationTableAdapter.addLoadingFooter();
            historiqueConsultationTableAdapter.removeLoadingFooter();

            editTextSearchDate.setText(sdf.format(calendar.getTime()));
        };
        editTextSearchDate.setOnClickListener(view -> new DatePickerDialog(
                getContext(),
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
    }
}