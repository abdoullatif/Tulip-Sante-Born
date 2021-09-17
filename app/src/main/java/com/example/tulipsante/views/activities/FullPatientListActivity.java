package com.example.tulipsante.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.models.CategorieSigneVitaux;
import com.example.tulipsante.models.uIModels.TempVitalValue;
import com.example.tulipsante.utils.Paginator;
import com.example.tulipsante.views.adapters.PatientAdapter;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.FullPatientListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FullPatientListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int LAUNCH_SECOND_ACTIVITY = 1;
    private String[] items = {
            "Date",
    };
    private String[] itemsDate = {
            "Newest",
            "Oldest",
    };
    private String spinnerValue = "Newest";
    private List<Patient> patientList = new ArrayList<>();

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentIndex = 0;
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;

    // Views
    private EditText editTextSearch;
    private CardView
            buttonAddNewPatient,
            cardViewBack,
            cardViewTag,
            cardViewTagStatus,
            cardViewQr;
    private RecyclerView recyclerView;
    private Spinner spinner1, spinner2;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textViewNumber;

    protected NfcAdapter nfcAdapter;
    protected PendingIntent nfcPendingIntent;

    private ArrayAdapter<String> adapterSpinner;

    // View Model
    private FullPatientListViewModel fullPatientListViewModel;

    // Recycler View Adapter
    private PatientAdapter patientAdapter;

    private LinearLayoutManager linearLayoutManager;

    String searchAttr = "";

    private void initViews() {
        buttonAddNewPatient = findViewById(R.id.buttonAddPatient);
        cardViewBack = findViewById(R.id.cardViewBack);
        recyclerView = findViewById(R.id.recyclerViewPatients);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        editTextSearch = findViewById(R.id.editTextSearch);
        cardViewTag = findViewById(R.id.cardViewTag);
        cardViewTagStatus = findViewById(R.id.cardViewTagStatus);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        textViewNumber = findViewById(R.id.textViewNumber);
        cardViewQr = findViewById(R.id.cardViewQr);
    }

    @SuppressLint("SetTextI18n")
    private void initialisation() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(
                this,
                0, new
                        Intent(
                                this,
                        getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        patientAdapter = new PatientAdapter( "All Patients");
        fullPatientListViewModel = ViewModelProviders.of(this)
                .get(FullPatientListViewModel.class);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item,items);
        adapterSpinner = new ArrayAdapter<>(this,
                R.layout.spinner_item,itemsDate);

        spinner1.setPopupBackgroundResource(R.color.colorAccent);
        spinner1.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        try {
            String num = fullPatientListViewModel.getNumberOfRecord();
            TOTAL_PAGES = (int) Math.ceil(Double.parseDouble(num) / 10);
            textViewNumber.setText("("+num+")");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(patientAdapter);
    }

    private void setScrollListener() {
        recyclerView.addOnScrollListener(new Paginator(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(() -> loadNextPage(spinnerValue),500);
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

    private void loadFirstPage(String val) {
        patientAdapter.clear();
        List<String> data = new ArrayList<>();
        data.add(val);
        data.add(String.valueOf(currentIndex));
        fullPatientListViewModel.setFilter(data);

        if (currentPage <= TOTAL_PAGES) {
            recyclerView.post(() -> patientAdapter.addLoadingFooter());
        }
        else isLastPage = true;
    }

    private void loadNextPage(String val) {
        int counter = 10;
        System.out.println(patientAdapter.getItemCount() >= counter);
        if(patientAdapter.getItemCount() >= counter) {
            patientAdapter.removeLoadingFooter();

            String index = String.valueOf(currentIndex + 10);
            List<String> data = new ArrayList<>();
            data.add(val);
            data.add(index);
            fullPatientListViewModel.setFilter(data);


            if (currentPage != TOTAL_PAGES) {
                recyclerView.post(() -> patientAdapter.addLoadingFooter());
            } else {
                isLastPage = true;
            }
            isLoading = false;
            currentIndex += 10;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_full_patient_list);

        initViews();
        initialisation();

        setScrollListener();
        onSpinnersPressed();
        setPatientTable();
        onButtonAddNewPatientPressed();
        onBackButtonPressed();
        searchFunction();
        onNfcButtonPressed();
        onQrButtonPressed();
    }

    private void onQrButtonPressed() {
        cardViewQr.setOnClickListener(view -> {
            Intent intent = new Intent(
                    FullPatientListActivity.this,
                    ScanQRActivity.class);
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
        });
    }

    private void setApplicationTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentTheme = sharedPreferences.getString("current_Theme",null);
        assert currentTheme != null;
        if(currentTheme.equals("dark")) {
            setTheme(R.style.AppTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
    }

    @Override
    public void onRefresh() {
        currentIndex = 0;
        currentPage = 1;
        isLoading = false;
        isLastPage = false;
        patientList.clear();
        patientAdapter.clear();
        new Handler().postDelayed(() -> loadFirstPage(spinnerValue),1000);
        new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false),2000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                searchAttr = data.getStringExtra("res");
                patientList.clear();
                patientAdapter.clear();
                System.out.println(searchAttr);
                List<String> data1 = new ArrayList<>();
                data1.add("");
                data1.add("");
                data1.add("%" + searchAttr + "%");
                fullPatientListViewModel.setFilter(data1);
                patientAdapter.addLoadingFooter();
                patientAdapter.removeLoadingFooter();
            }
        }
    }

    private void setPatientTable() {
        fullPatientListViewModel.getAllPatients().observe(this, patients -> {
            patientList.addAll(patients);
            patientAdapter.setPatients(patientList);
            patientAdapter.notifyDataSetChanged();
        });
    }

    private void onSpinnersPressed() {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object object = adapterView.getItemAtPosition(i);
                String val = (String) object;
                if(val.equals("Date")) {
                    spinner2.setPopupBackgroundResource(R.color.colorAccent);
                    spinner2.setAdapter(adapterSpinner);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object object = adapterView.getItemAtPosition(i);
                String val = (String) object;
                spinnerValue = val;
                currentIndex = 0;
                currentPage = 1;
                isLoading = false;
                isLastPage = false;
                patientList.clear();
                loadFirstPage(val);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void onNfcButtonPressed() {
        cardViewTag.setOnClickListener(view -> {
            cardViewTag.setCardBackgroundColor(getColor(R.color.green));
            enableForegroundMode();
        });
    }

    private void searchFunction() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchAttr = charSequence.toString();
                patientList.clear();
                patientAdapter.clear();
                List<String> data = new ArrayList<>();
                data.add("");
                data.add("");
                data.add("%" + searchAttr + "%");
                fullPatientListViewModel.setFilter(data);
                patientAdapter.addLoadingFooter();
                patientAdapter.removeLoadingFooter();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void onBackButtonPressed() {
        cardViewBack.setOnClickListener(view -> finish());
    }

    private void onButtonAddNewPatientPressed() {
        buttonAddNewPatient.setOnClickListener(view -> {
            Intent intent = new Intent(
                    FullPatientListActivity.this,
                    AddPatientActivity.class);
            startActivity(intent);
        });
    }

    // For the nfc
    public void enableForegroundMode() {
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED); // filter for all
        IntentFilter[] writeTagFilters = new IntentFilter[] {tagDetected};
        nfcAdapter.enableForegroundDispatch(
                this,
                nfcPendingIntent,
                writeTagFilters,
                null);
    }

    public void disableForegroundMode() {
        nfcAdapter.disableForegroundDispatch(this);
        cardViewTagStatus.setCardBackgroundColor(getColor(R.color.red));
        cardViewTag.setCardBackgroundColor(getColor(R.color.blue));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            cardViewTag.setCardBackgroundColor(getColor(R.color.blue));
            cardViewTagStatus.setCardBackgroundColor(getColor(R.color.green));
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            patientList.clear();
            List<String> data = new ArrayList<>();
            data.add("");
            data.add("");
            assert tag != null;
            data.add(GeneralPurposeFunctions.bytesToHexString(tag.getId()));
            fullPatientListViewModel.setFilter(data);
            patientAdapter.addLoadingFooter();
            patientAdapter.removeLoadingFooter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        disableForegroundMode();
    }
}