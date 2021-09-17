package com.example.tulipsante.views.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tulipsante.models.Symptome;
import com.example.tulipsante.views.adapters.AllTableAdapter;
import com.example.tulipsante.views.adapters.AllergiesListViewAdapter;
import com.example.tulipsante.views.adapters.AnteTableAdapter;
import com.example.tulipsante.views.adapters.MaladieListViewAdapter;
import com.example.tulipsante.views.adapters.ViceListViewAdapter;
import com.example.tulipsante.models.Allergie;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.models.Vice;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.FirstTimeConsultationViewModel;
import com.example.tulipsante.views.adapters.ViceTableAdapter;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import static com.example.tulipsante.views.fragments.MedicalStatusFragment.hideKeyboard;

public class FirstTimeConsultationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private String viceId, viceDescription;
    private String anteId, anteDescription;
    private String foodId, foodDescription;
    private String medId, medDescription;

    List<Vice> viceList = new ArrayList<>();
    List<Allergie> allergieListF = new ArrayList<>();
    List<Allergie> allergieListD = new ArrayList<>();
    List<Maladie> antecedentList = new ArrayList<>();


    private SearchableSpinner viceSpinner, anteSpinner, allFoodSpinner, allMedSpinner;
    private RecyclerView recViewVices,recViewAnte,recViewFood,recViewMed;
    private CardView addVice, addAnte, addFood, addMed, cardViewSave;

    // Adapters
    private MaladieListViewAdapter maladieLisViewAdapter;
    private AllergiesListViewAdapter allergiesListViewAdapter1, allergiesListViewAdapter2;
    private ViceListViewAdapter viewAdapter;

    // new Adapters
    private ViceTableAdapter viceTableAdapter;
    private AnteTableAdapter anteTableAdapter;
    private AllTableAdapter allFTableAdapter, allDTableAdapter;

    //View Model
    private FirstTimeConsultationViewModel firstTimeConsultationViewModel;

    // Click listener
    private View.OnClickListener listener;

    public FirstTimeConsultationFragment(String mParam1, String mParam2) {
        this.mParam1 = mParam1;
        this.mParam2 = mParam2;
    }

    private void initViews(View view) {
        // init vice views
        addVice = view.findViewById(R.id.addVice);
        viceSpinner = view.findViewById(R.id.viceSpinner);
        recViewVices = view.findViewById(R.id.recViewVices);


        // init antecedents
        addAnte = view.findViewById(R.id.addAnte);
        anteSpinner = view.findViewById(R.id.anteSpinner);
        recViewAnte = view.findViewById(R.id.recViewAnte);

        // init all food
        addFood = view.findViewById(R.id.addFood);
        allFoodSpinner = view.findViewById(R.id.foodSpinner);
        recViewFood = view.findViewById(R.id.recViewFood);

        // init all med
        addMed = view.findViewById(R.id.addDrug);
        allMedSpinner = view.findViewById(R.id.drugSpinner);
        recViewMed = view.findViewById(R.id.recViewDrug);

        cardViewSave = view.findViewById(R.id.buttonSave);
    }

    private void initialisation() {
        firstTimeConsultationViewModel = ViewModelProviders
                .of(this)
                .get(FirstTimeConsultationViewModel.class);

        // ****
        // vice

        viewAdapter = new ViceListViewAdapter(
                getContext(),
                R.layout.spinner_region_list_item,
                R.id.textViewRegionListItem,
                viceList);

        viceTableAdapter = new ViceTableAdapter(getContext());

        recViewVices.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recViewVices.setAdapter(viceTableAdapter);

        viceSpinner.setTitle("Select vice");
        viceSpinner.setPositiveButton("OK");

        // *** ****
        // end vice

        // ****
        // Antecedent

        maladieLisViewAdapter = new MaladieListViewAdapter(
                getContext(),
                R.layout.spinner_region_list_item,
                R.id.textViewRegionListItem,
                antecedentList);

        anteTableAdapter = new AnteTableAdapter(getContext());

        recViewAnte.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recViewAnte.setAdapter(anteTableAdapter);

        anteSpinner.setTitle("Select Antecedent");
        anteSpinner.setPositiveButton("OK");

        // *** ****
        // end Antecedent

        // ****
        // Allergie Food

        allergiesListViewAdapter1 = new AllergiesListViewAdapter(
                getContext(),
                R.layout.spinner_region_list_item,
                R.id.textViewRegionListItem,
                allergieListF);

        allFTableAdapter = new AllTableAdapter(getContext());

        recViewFood.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recViewFood.setAdapter(allFTableAdapter);

        allFoodSpinner.setTitle("Select Food");
        allFoodSpinner.setPositiveButton("OK");

        // *** ****
        // end Allergie Food

        // ****
        // Allergie Medics

        allergiesListViewAdapter2 = new AllergiesListViewAdapter(
                getContext(),
                R.layout.spinner_region_list_item,
                R.id.textViewRegionListItem,
                allergieListD);

        allDTableAdapter = new AllTableAdapter(getContext());

        recViewMed.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recViewMed.setAdapter(allDTableAdapter);

        allMedSpinner.setTitle("Select Medecines");
        allMedSpinner.setPositiveButton("OK");

        // *** ****
        // end Allergie Medics
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (View.OnClickListener) context;
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

        onSaveButtonPressed();

        setViceSpinner();
        onAddVicePressed();

        setAnteSpinner();
        onAddAntePressed();

        setAllFoodSpinner();
        onAddAllFoodPressed();

        setAllDrugSpinner();
        onAddAllDrugPressed();

    }

    private void setViceSpinner() {
        firstTimeConsultationViewModel.getAllVices()
                .observe(getViewLifecycleOwner(), vices -> {
            this.viceList.addAll(vices);
            viewAdapter.notifyDataSetChanged();
        });
        viceSpinner.setPopupBackgroundResource(R.color.colorAccent);
        viceSpinner.setAdapter(viewAdapter);
        viceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj= adapterView.getItemAtPosition(i);
                Vice vice = (Vice) obj;
                viceId = vice.getIdVice();
                viceDescription = vice.getDescription();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void onAddVicePressed() {
        addVice.setOnClickListener(view -> {
            hideKeyboard(getActivity());
            viceTableAdapter.setViceList(new Vice(
                    viceId,
                    viceDescription,
                    ""
            ));
            viceTableAdapter.notifyDataSetChanged();
        });
    }

    private void setAnteSpinner() {
        firstTimeConsultationViewModel.getAllMaladies()
                .observe(getViewLifecycleOwner(), maladies -> {
                    this.antecedentList.addAll(maladies);
                    maladieLisViewAdapter.notifyDataSetChanged();
                });
        anteSpinner.setPopupBackgroundResource(R.color.colorAccent);
        anteSpinner.setAdapter(maladieLisViewAdapter);
        anteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj= adapterView.getItemAtPosition(i);
                Maladie maladie = (Maladie) obj;
                anteId = maladie.getIdMaladie();
                anteDescription = maladie.getDescription();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void onAddAntePressed() {
        addAnte.setOnClickListener(view -> {
            hideKeyboard(getActivity());
            anteTableAdapter.setMaladies(new Maladie(
                    anteId,
                    anteDescription,
                    "",
                    ""
            ));
            anteTableAdapter.notifyDataSetChanged();
        });
    }

    private void setAllFoodSpinner() {
        firstTimeConsultationViewModel.getFoodAllergies()
                .observe(getViewLifecycleOwner(), food -> {
                    this.allergieListF.addAll(food);
                    allergiesListViewAdapter1.notifyDataSetChanged();
                });
        allFoodSpinner.setPopupBackgroundResource(R.color.colorAccent);
        allFoodSpinner.setAdapter(allergiesListViewAdapter1);
        allFoodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj= adapterView.getItemAtPosition(i);
                Allergie all = (Allergie) obj;
                foodId = all.getIdAllergie();
                foodDescription = all.getDescription();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void onAddAllFoodPressed() {
        addFood.setOnClickListener(view -> {
            hideKeyboard(getActivity());
            allFTableAdapter.setAllergies(new Allergie(
                    foodId,
                    "food",
                    foodDescription,
                    ""
            ));
            allFTableAdapter.notifyDataSetChanged();
        });
    }

    private void setAllDrugSpinner() {
        firstTimeConsultationViewModel.getDrugAllergies()
                .observe(getViewLifecycleOwner(), all -> {
                    this.allergieListD.addAll(all);
                    allergiesListViewAdapter2.notifyDataSetChanged();
                });
        allMedSpinner.setPopupBackgroundResource(R.color.colorAccent);
        allMedSpinner.setAdapter(allergiesListViewAdapter2);
        allMedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj= adapterView.getItemAtPosition(i);
                Allergie allergie = (Allergie) obj;
                medId = allergie.getIdAllergie();
                medDescription = allergie.getDescription();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void onAddAllDrugPressed() {
        addMed.setOnClickListener(view -> {
            hideKeyboard(getActivity());
            allDTableAdapter.setAllergies(new Allergie(
                    medId,
                    "drug",
                    medDescription,
                    ""
            ));
            allDTableAdapter.notifyDataSetChanged();
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

            firstTimeConsultationViewModel
                    .insertValues(mParam1, viceTableAdapter.viceList, allDTableAdapter.allergies,allFTableAdapter.allergies,anteTableAdapter.maladies);

            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();

            listener.onClick(view);
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}