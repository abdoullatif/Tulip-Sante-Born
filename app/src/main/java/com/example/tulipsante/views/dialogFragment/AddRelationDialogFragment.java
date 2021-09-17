package com.example.tulipsante.views.dialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tulipsante.interfaces.OnAddRelationClicked;
import com.example.tulipsante.interfaces.OnSaveListener;
import com.example.tulipsante.models.Relation;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.AddRelationViewModel;
import com.example.tulipsante.views.adapters.AddRelationTableAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddRelationDialogFragment
        extends BottomSheetDialogFragment
        implements OnAddRelationClicked {
    private Context context;
    private String searchAttr;
    private String[] items = new String[]{
            "",
            "Mère",
            "Père",
            "Enfant",
    };

    // Views
    private CardView cancel;
    private RecyclerView recyclerView;
    private EditText editTextSearch;
    private Spinner spinnerRelation;

    // Adapter
    private ArrayAdapter<String> spinnerAdapter;
    private AddRelationTableAdapter addRelationTableAdapter;

    // View Model
    private AddRelationViewModel addRelationViewModel;

    private OnSaveListener listener;

    public AddRelationDialogFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_relation_dialog, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        final View view = getView();
        view.post(() -> {
            View parent = (View) view.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight(),true);
        });
    }

    private void initViews(View view) {
        cancel = view.findViewById(R.id.cancelDialog);
        recyclerView = view.findViewById(R.id.recViewRelation);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        spinnerRelation = view.findViewById(R.id.spinnerTypeRelation);

    }

    private void initialisation() {
        spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item,items);
        addRelationTableAdapter = new AddRelationTableAdapter(getContext(),this);

        addRelationViewModel = ViewModelProviders
                .of(this).get(AddRelationViewModel.class);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(addRelationTableAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = (OnSaveListener) context;

        initViews(view);
        initialisation();

        setSpinner();
        searchFunction();
        onDialogCancel();

    }

    private void setSpinner() {
        spinnerRelation.setPopupBackgroundResource(R.color.colorAccent);
        spinnerRelation.setAdapter(spinnerAdapter);
    }

    private void searchFunction() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchAttr = charSequence.toString();
//                if (!searchAttr.equals("")) {
                addRelationViewModel.
                        getPatientSearch(searchAttr)
                        .observe(getViewLifecycleOwner(), patients -> {
                            addRelationTableAdapter.setPatients(patients);
                            addRelationTableAdapter.notifyDataSetChanged();
                        });
//                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onAddRelation(Relation relation, String genre) {
        if(!spinnerRelation.getSelectedItem().toString().equals("")) {
            relation.setTypeRelation(spinnerRelation.getSelectedItem().toString());
            if(addRelationViewModel.insertRelation(relation, genre)) {
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                listener.onSavePressed();
            } else {
                Toast.makeText(context, "Relation exist already!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(context, "Choose the relation link!", Toast.LENGTH_SHORT).show();
        }
    }

    private void onDialogCancel() {
        cancel.setOnClickListener(view -> dismiss());
    }
}