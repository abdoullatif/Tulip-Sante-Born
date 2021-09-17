package com.example.tulipsante.views.fragments;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.tulipsante.models.Patient;
import com.example.tulipsante.utils.Paginator;
import com.example.tulipsante.views.activities.FullPatientListActivity;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.views.activities.ScanQRActivity;
import com.example.tulipsante.views.adapters.MyPatientAdapter;
import com.example.tulipsante.viewModel.PatientFragmentViewModel;
import com.example.tulipsante.interfaces.NewIntent;
import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;


public class PatientFragment extends Fragment
        implements NewIntent, SwipeRefreshLayout.OnRefreshListener {
    private static final int LAUNCH_SECOND_ACTIVITY = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String searchAttr = " ";
    private String mParam1;
    private String mParam2;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentIndex = 0;
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;
    private int counter = 10;

    private List<Patient> patientList;

    // Views
    private EditText editTextSearch;
    private CardView cardViewAddPatient,
            cardViewTag,
            cardViewTagStatus,
            cardViewQr;
    private RecyclerView recyclerView;
    private TableLayout tabLayout;
    private TextView textViewNoData;
    private SwipeRefreshLayout swipeRefreshLayout;


    private Context context;
    protected NfcAdapter nfcAdapter;
    protected PendingIntent nfcPendingIntent;

    // View Model
    private PatientFragmentViewModel patientFragmentViewModel;

    // Adapter
    private MyPatientAdapter myPatientAdapter;

    private LinearLayoutManager linearLayoutManager;

    public PatientFragment() {
        // Required empty public constructor
    }

    public static PatientFragment newInstance(String param1, String param2) {
        PatientFragment fragment = new PatientFragment();
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
        context = getContext();
        return inflater.inflate(R.layout.fragment_patient, container, false);
    }

    private void initViews(View view) {
        cardViewAddPatient = view.findViewById(R.id.buttonAddPatient);
        cardViewTag = view.findViewById(R.id.cardViewTag);
        cardViewTagStatus = view.findViewById(R.id.cardViewTagStatus);
        recyclerView = view.findViewById(R.id.recyclerViewPatients);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        tabLayout = view.findViewById(R.id.tabLayout);
        textViewNoData = view.findViewById(R.id.textViewNoData);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        cardViewQr = view.findViewById(R.id.cardViewQr);
    }

    private void initialisation() {
        myPatientAdapter = new MyPatientAdapter(getContext(),"My Patients");
        patientList = new ArrayList<>();
        nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());
        nfcPendingIntent = PendingIntent.getActivity(
                getContext(),
                0,
                new Intent(
                        getContext(),
                        getContext().getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myPatientAdapter);
        patientFragmentViewModel = ViewModelProviders
                .of(this)
                .get(PatientFragmentViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this);
        TOTAL_PAGES = (int) Math.ceil(Double.parseDouble(patientFragmentViewModel.getNumberOfRecord()) / 10);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myPatientAdapter);

    }

    private void setScrollListener() {
        recyclerView.addOnScrollListener(new Paginator(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
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
        myPatientAdapter.clear();
        List<String> data = new ArrayList<>();
        data.add(String.valueOf(currentIndex));
        patientFragmentViewModel.setFilter(data);

        if (currentPage < TOTAL_PAGES) {
            myPatientAdapter.addLoadingFooter();
        }
        else {
            isLastPage = true;
        }
    }

    private void loadNextPage() {
        if(myPatientAdapter.getItemCount() >= counter) {
            myPatientAdapter.removeLoadingFooter();

            String index = String.valueOf(currentIndex + 10);
            List<String> data = new ArrayList<>();
            data.add(index);
            patientFragmentViewModel.setFilter(data);

            if (currentPage != TOTAL_PAGES) {
                    myPatientAdapter.addLoadingFooter();
            } else {
                isLastPage = true;
            }
            isLoading = false;
            currentIndex += 10;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setScrollListener();
        setPatientTable();
        searchFunction();
        navigateToAddPatient();
        onAddTagPressed();
        onQrButtonPressed();
    }

    private void onQrButtonPressed() {
        cardViewQr.setOnClickListener(view -> {
            Intent intent = new Intent(
                    getContext(),
                    ScanQRActivity.class);
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                searchAttr = data.getStringExtra("res");
                patientList.clear();
                myPatientAdapter.clear();
                List<String> data1 = new ArrayList<>();
                data1.add("%" + searchAttr + "%");
                patientFragmentViewModel.setFilter(data1);
                myPatientAdapter.addLoadingFooter();
                myPatientAdapter.removeLoadingFooter();
            }
        }
    }

    @Override
    public void onRefresh() {
        currentIndex = 0;
        currentPage = 1;
        isLoading = false;
        isLastPage = false;
        loadFirstPage();
        new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false),1500);
    }

    private void setPatientTable() {
        loadFirstPage();
        patientFragmentViewModel.getSearchBy().observe(getViewLifecycleOwner(), patients -> {
            if(patients.size() <= 0) {
                // hide views and show no data
                textViewNoData.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.INVISIBLE);
//                editTextSearch.setVisibility(View.INVISIBLE);
                cardViewTagStatus.setVisibility(View.INVISIBLE);
                cardViewQr.setVisibility(View.INVISIBLE);
                cardViewTag.setVisibility(View.INVISIBLE);
            }
            else {
                // set views visible
                tabLayout.setVisibility(View.VISIBLE);
//                editTextSearch.setVisibility(View.VISIBLE);
                cardViewTagStatus.setVisibility(View.VISIBLE);
                cardViewQr.setVisibility(View.VISIBLE);
                cardViewTag.setVisibility(View.VISIBLE);
                // pass data to adapter
                myPatientAdapter.setPatients(patients);
                myPatientAdapter.notifyDataSetChanged();
            }
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
                myPatientAdapter.clear();
                List<String> data = new ArrayList<>();
                if(!searchAttr.isEmpty()) {
                    data.add("%" + searchAttr + "%");
                    patientFragmentViewModel.setFilter(data);
                }
                myPatientAdapter.addLoadingFooter();
                myPatientAdapter.removeLoadingFooter();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void navigateToAddPatient() {
        cardViewAddPatient.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), FullPatientListActivity.class);
            startActivity(intent);
        });
    }

    private void onAddTagPressed() {
        cardViewTag.setOnClickListener(view -> {
            cardViewTag.setCardBackgroundColor(getResources().getColor(R.color.green));
            enableForegroundMode();
        });
    }

    // For the nfc
    public void enableForegroundMode() {
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] writeTagFilters = new IntentFilter[] {tagDetected};
        nfcAdapter.enableForegroundDispatch(
                getActivity(),
                nfcPendingIntent,
                writeTagFilters,
                null);
    }

    public void disableForegroundMode() {
        nfcAdapter.disableForegroundDispatch(getActivity());
        cardViewTagStatus.setCardBackgroundColor(getResources().getColor(R.color.red));
        cardViewTag.setCardBackgroundColor(getResources().getColor(R.color.blue));
    }

    @Override
    public void onResume() {
        super.onResume();
        disableForegroundMode();
    }

    @Override
    public void onNewIntentCreated (Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            cardViewTag.setCardBackgroundColor(getResources().getColor(R.color.blue));
            cardViewTagStatus.setCardBackgroundColor(getResources().getColor(R.color.green));
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            patientList.clear();
            List<String> data = new ArrayList<>();
            data.add(GeneralPurposeFunctions.bytesToHexString(tag.getId()));
            patientFragmentViewModel.setFilter(data);
            myPatientAdapter.addLoadingFooter();
            myPatientAdapter.removeLoadingFooter();
        }
    }

    public void getDataWithTag(byte[] tagId) {
        myPatientAdapter.setPatients(patientFragmentViewModel
                .getDocPatientWithTag(GeneralPurposeFunctions.bytesToHexString(tagId)));
        myPatientAdapter.notifyDataSetChanged();
    }
}
