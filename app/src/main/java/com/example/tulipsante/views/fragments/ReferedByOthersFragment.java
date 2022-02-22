package com.example.tulipsante.views.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tulipsante.R;
import com.example.tulipsante.models.uIModels.PatientXRefXDoc;
import com.example.tulipsante.viewModel.ReferredByOthersViewModel;
import com.example.tulipsante.views.adapters.ReferredByOthersTableAdapter;

import java.util.List;

public class ReferedByOthersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // views
    private RecyclerView recView;

    // view model
    private ReferredByOthersViewModel referredByOthersViewModel;

    // adapter
    private ReferredByOthersTableAdapter referredByOthersTableAdapter;

    private SharedPreferences sharedPreferences;

    public ReferedByOthersFragment() {
        // Required empty public constructor
    }
    public static ReferedByOthersFragment newInstance(String param1, String param2) {
        ReferedByOthersFragment fragment = new ReferedByOthersFragment();
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
        return inflater.inflate(R.layout.fragment_refered_by_others, container, false);
    }

    private void initViews(View view) {
        recView = view.findViewById(R.id.recView);
    }

    private void initialisation() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        referredByOthersViewModel = ViewModelProviders
                .of(this)
                .get(ReferredByOthersViewModel.class);

        referredByOthersTableAdapter = new ReferredByOthersTableAdapter();
        recView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recView.setAdapter(referredByOthersTableAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setRecView();
    }

    private void setRecView() {
        referredByOthersViewModel.getPatientList().observe(this, patientXRefXDocs -> {
            referredByOthersTableAdapter.setPatientXRefXDocList(patientXRefXDocs);
            referredByOthersTableAdapter.notifyDataSetChanged();
        });
        referredByOthersViewModel.setFilter(sharedPreferences.getString("IDMEDECIN", null));
    }

}