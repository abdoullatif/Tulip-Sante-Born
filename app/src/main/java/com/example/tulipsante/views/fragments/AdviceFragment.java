package com.example.tulipsante.views.fragments;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.models.Conseil;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.viewModel.AdviceFragmentViewModel;
import com.example.tulipsante.views.adapters.AdviceStatusTableAdapter;
import com.example.tulipsante.views.adapters.AdviceTableAdapter;

import java.util.List;
import java.util.Objects;

public class AdviceFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private final String idConsulation;

    private LinearLayout linearLayout;
    private TableLayout tableLayout;
    private EditText editTextAdvice;
    private RecyclerView recyclerViewAdvice;
    private CardView cardViewSave;
    private TextView textViewNoData;

    // Adapter
    private AdviceTableAdapter adviceTableAdapter;
    private AdviceStatusTableAdapter adviceStatusTableAdapter;

    //View Model
    private AdviceFragmentViewModel adviceFragmentViewModel;

    public AdviceFragment(String mParam1, String mParam2, String idConsulation) {
        this.mParam1 = mParam1;
        this.mParam2 = mParam2;
        this.idConsulation = idConsulation;
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
        return inflater.inflate(R.layout.fragment_advice, container, false);
    }

    private void initViews(View view) {
        editTextAdvice = view.findViewById(R.id.editTextAdvice);
        cardViewSave = view.findViewById(R.id.buttonSave);
        linearLayout = view.findViewById(R.id.linear1);
        tableLayout = view.findViewById(R.id.tabLayout1);
        recyclerViewAdvice = view.findViewById(R.id.recViewAdvice);
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
            cardViewSave.setVisibility(View.INVISIBLE);
        }
    }

    private void initialisation() {
        adviceTableAdapter = new AdviceTableAdapter();
        adviceFragmentViewModel = ViewModelProviders
                .of(this)
                .get(AdviceFragmentViewModel.class);
        adviceStatusTableAdapter = new AdviceStatusTableAdapter();
        recyclerViewAdvice.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerViewAdvice.setAdapter(adviceStatusTableAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        init();
        initialisation();

        setAdviceTable();
        onButtonSavePressed();

    }

    private void setAdviceTable() {
        if(tableLayout.getVisibility() == View.VISIBLE) {
            if(idConsulation == null) {
                adviceStatusTableAdapter
                        .setConseilList(
                                adviceFragmentViewModel
                                        .conseilList(adviceFragmentViewModel
                                                .getIdConsultation()
                                        )
                        );
            }
            else {
                List<Conseil> conseilList = adviceFragmentViewModel.conseilList(idConsulation);
                if(!conseilList.isEmpty()) {
                    adviceStatusTableAdapter
                            .setConseilList(
                                    conseilList);
                }
                else {
                    tableLayout.setVisibility(View.INVISIBLE);
                    textViewNoData.setVisibility(View.VISIBLE);
                }
            }
            adviceStatusTableAdapter.notifyDataSetChanged();
        }
    }

    private void onButtonSavePressed() {
        cardViewSave.setOnClickListener(view -> {
            hideKeyboard(Objects.requireNonNull(getActivity()));
            if(!editTextAdvice.getText().toString().isEmpty()) {
                showAlertDialogButtonClicked();
            }
            else {
                Toast.makeText(
                        getContext(),
                        "Enter data before saving",
                        Toast.LENGTH_SHORT).show();
            }        });
    }

    // close keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if(view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // dialog alert
    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure that you want to save the data?");

        builder.setPositiveButton("Continue", (dialogInterface, i) -> {
            adviceTableAdapter.setAdvice(new Conseil(
                    GeneralPurposeFunctions.idTable(),
                    "",
                    editTextAdvice.getText().toString().trim(),
                    ""
            ));
            adviceFragmentViewModel
                    .insertValues(adviceTableAdapter.conseils);
            adviceTableAdapter.notifyDataSetChanged();
            linearLayout.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.VISIBLE);
            cardViewSave.setVisibility(View.INVISIBLE);
            setAdviceTable();
            Toast.makeText(getContext(), "Enregistrer!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}