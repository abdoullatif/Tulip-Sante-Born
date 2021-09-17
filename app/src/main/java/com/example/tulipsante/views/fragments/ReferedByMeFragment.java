package com.example.tulipsante.views.fragments;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.viewModel.ReferredByMeViewModel;
import com.example.tulipsante.viewModel.ReferredByOthersViewModel;
import com.example.tulipsante.views.adapters.ReferredByMeTableAdapter;
import com.example.tulipsante.views.adapters.ReferredByOthersTableAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReferedByMeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // views
    private RecyclerView recView;
    private SwipeRefreshLayout refreshLayout;
    private CardView buttonSearch;
    private EditText editTextSearch;

    // view model
    private ReferredByMeViewModel referredByMeViewModel;

    // adapter
    private ReferredByMeTableAdapter referredByMeTableAdapter;

    private SharedPreferences sharedPreferences;

    public ReferedByMeFragment() {
        // Required empty public constructor
    }
    public static ReferedByMeFragment newInstance(String param1, String param2) {
        ReferedByMeFragment fragment = new ReferedByMeFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_refered_by_me, container, false);
    }

    private void initViews(View view) {
        recView = view.findViewById(R.id.recView);
        refreshLayout = view.findViewById(R.id.swipeRefresh);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        editTextSearch = view.findViewById(R.id.editTextSearch);
    }

    private void initialisation() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        referredByMeViewModel = ViewModelProviders
                .of(this)
                .get(ReferredByMeViewModel.class);

        referredByMeTableAdapter = new ReferredByMeTableAdapter();
        recView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recView.setAdapter(referredByMeTableAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setRecView();
        onButtonSearchPressed();
    }

    @Override
    public void onRefresh() {
        editTextSearch.setText("");
        referredByMeTableAdapter.clear();
        List<String> data = new ArrayList<>();
        data.add(sharedPreferences.getString("IDMEDECIN", null));
        data.add("%%");
        referredByMeViewModel.setFilter(data);
        referredByMeTableAdapter.notifyDataSetChanged();
        new Handler().postDelayed(() -> refreshLayout.setRefreshing(false), 2000);
    }

    private void onButtonSearchPressed() {
        editTextSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_SEARCH) {
                String searchAttr = textView.getText().toString();
                referredByMeTableAdapter.clear();
                List<String> data = new ArrayList<>();
                if(!searchAttr.isEmpty()) {
                    data.add(sharedPreferences.getString("IDMEDECIN", null));
                    data.add("%" + searchAttr + "%");
                    referredByMeViewModel.setFilter(data);
                }
                referredByMeTableAdapter.notifyDataSetChanged();
                return true;
            }
            return false;
        });

        buttonSearch.setOnClickListener(view -> {
            GeneralPurposeFunctions.hideKeyboard(getActivity());
            String searchAttr = "%"+ editTextSearch.getText().toString() + "%";
            referredByMeTableAdapter.clear();
            List<String> data = new ArrayList<>();
            if(!searchAttr.isEmpty()) {
                data.add(sharedPreferences.getString("IDMEDECIN", null));
                data.add("%" + searchAttr + "%");
                referredByMeViewModel.setFilter(data);
            }
            referredByMeTableAdapter.notifyDataSetChanged();
        });
    }

    private void setRecView() {
        referredByMeViewModel.getPatientList().observe(getViewLifecycleOwner(), patientXRefXDocs -> {
            referredByMeTableAdapter.setPatientXRefXDocList(patientXRefXDocs);
            referredByMeTableAdapter.notifyDataSetChanged();
        });
        List<String> data = new ArrayList<>();
        data.add(sharedPreferences.getString("IDMEDECIN", null));
        data.add("%%");
        referredByMeViewModel.setFilter(data);
    }
}