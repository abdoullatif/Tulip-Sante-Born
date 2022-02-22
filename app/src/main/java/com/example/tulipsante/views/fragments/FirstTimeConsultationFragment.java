package com.example.tulipsante.views.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tulipsante.views.adapters.AllergiesListViewAdapter;
import com.example.tulipsante.views.adapters.MaladieListViewAdapter;
import com.example.tulipsante.views.adapters.ViceListViewAdapter;
import com.example.tulipsante.models.Allergie;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.models.Vice;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.FirstTimeConsultationViewModel;

import java.util.ArrayList;
import java.util.List;

public class FirstTimeConsultationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    List<Vice> items = new ArrayList<>();
    List<Allergie> foodAllergies = new ArrayList<>();
    List<Allergie> drugAllergies = new ArrayList<>();
    List<Maladie> maladiesList = new ArrayList<>();

    List<Vice> viceList = new ArrayList<>();
    List<Allergie> allergieListF = new ArrayList<>();
    List<Allergie> allergieListD = new ArrayList<>();
    List<Maladie> antecedentList = new ArrayList<>();

    // List Views
    private ListView listViewVice,listViewAllergieF, listViewAllergieD, listViewAntecedent;
    private CardView cardViewSave;

    // Adapters
    private MaladieListViewAdapter maladieLisViewAdapter;
    private AllergiesListViewAdapter allergiesListViewAdapter1, allergiesListViewAdapter2;
    private ViceListViewAdapter viewAdapter;

    //View Model
    private FirstTimeConsultationViewModel firstTimeConsultationViewModel;

    // Click listener
    private View.OnClickListener listener;

    public FirstTimeConsultationFragment(String mParam1, String mParam2) {
        // Required empty public constructor
        this.mParam1 = mParam1;
        this.mParam2 = mParam2;
    }

    private void initViews(View view) {
        listViewVice = view.findViewById(R.id.listViewVice);
        listViewAllergieF = view.findViewById(R.id.listViewAllergieF);
        listViewAllergieD = view.findViewById(R.id.listViewAllergieD);
        listViewAntecedent = view.findViewById(R.id.listViewMaladie);
        cardViewSave = view.findViewById(R.id.buttonSave);
    }

    private void initialisation() {
        firstTimeConsultationViewModel = ViewModelProviders
                .of(this)
                .get(FirstTimeConsultationViewModel.class);
        viewAdapter = new ViceListViewAdapter(
                getContext(),
                R.layout.checkbox_list_view_entry,
                R.id.textViewId,
                items);
        allergiesListViewAdapter1 = new AllergiesListViewAdapter(
                getContext(),
                R.layout.checkbox_list_view_entry,
                R.id.textViewId,
                drugAllergies);
        allergiesListViewAdapter2 = new AllergiesListViewAdapter(
                getContext(),
                R.layout.checkbox_list_view_entry,
                R.id.textViewId,
                foodAllergies);
        maladieLisViewAdapter = new MaladieListViewAdapter(
                getContext(),
                R.layout.checkbox_list_view_entry,
                R.id.textViewId,
                maladiesList);

        listViewVice.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listViewVice.setAdapter(viewAdapter);

        listViewAllergieF.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listViewAllergieF.setAdapter(allergiesListViewAdapter2);

        listViewAllergieD.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listViewAllergieD.setAdapter(allergiesListViewAdapter1);

        listViewAntecedent.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listViewAntecedent.setAdapter(maladieLisViewAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (View.OnClickListener) context;
    }

//    public static FirstTimeConsultationFragment newInstance(String param1, String param2) {
//        FirstTimeConsultationFragment fragment = new FirstTimeConsultationFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        return inflater.inflate(
                R.layout.fragment_first_time_consultation,
                container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setVices();
        setAllergies();
        setAntecedent();
        onSaveButtonPressed();

    }

    private void setAntecedent() {
        firstTimeConsultationViewModel.getAllMaladies().observe(this, maladies -> {
            maladiesList.addAll(maladies);
            maladieLisViewAdapter.notifyDataSetChanged();
        });
    }

    private void setAllergies() {
        firstTimeConsultationViewModel.getDrugAllergies().observe(this, allergies -> {
            drugAllergies.addAll(allergies);
            allergiesListViewAdapter1.notifyDataSetChanged();
        });
        firstTimeConsultationViewModel.getFoodAllergies().observe(this, allergies -> {
            foodAllergies.addAll(allergies);
            allergiesListViewAdapter2.notifyDataSetChanged();
        });
    }

    private void setVices() {
        firstTimeConsultationViewModel.getAllVices().observe(this, vices -> {
            items.addAll(vices);
            viewAdapter.notifyDataSetChanged();
        });
    }

    private void onSaveButtonPressed() {
        cardViewSave.setOnClickListener(view -> showAlertDialogButtonClicked(view));
    }

    public void showAlertDialogButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Would you like to validate all the entry?");

        builder.setPositiveButton("Continue", (dialogInterface, i) -> {
            viceList.addAll(viewAdapter.selectedVice);
            allergieListF.addAll(allergiesListViewAdapter2.selectedAllergie);
            allergieListD.addAll(allergiesListViewAdapter1.selectedAllergie);
            antecedentList.addAll(maladieLisViewAdapter.selectedMaladie);

            firstTimeConsultationViewModel
                    .insertValues(mParam1, viceList, allergieListD,allergieListF,antecedentList);

            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();

            listener.onClick(view);
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}