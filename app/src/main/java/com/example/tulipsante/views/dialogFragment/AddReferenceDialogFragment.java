package com.example.tulipsante.views.dialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
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

import com.example.tulipsante.R;
import com.example.tulipsante.interfaces.OnSaveListener;
import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.models.Relation;
import com.example.tulipsante.viewModel.AddReferenceViewModel;
import com.example.tulipsante.viewModel.AddRelationViewModel;
import com.example.tulipsante.views.adapters.AddReferenceTableAdapter;
import com.example.tulipsante.views.adapters.AddRelationTableAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddReferenceDialogFragment
        extends BottomSheetDialogFragment
{
    private Context context;
    private String searchAttr;

    // Views
    private CardView cancel;
    private RecyclerView recyclerView;
    private EditText editTextSearch;

    // Adapter
    private AddReferenceTableAdapter addReferenceTableAdapter;

    // View Model
    private AddReferenceViewModel addReferenceViewModel;

    private String idConsultation;

    public AddReferenceDialogFragment(Context context, String idConsultation) {
        this.context = context;
        this.idConsultation = idConsultation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_reference_dialog, container, false);
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
        recyclerView = view.findViewById(R.id.recViewReference);
        editTextSearch = view.findViewById(R.id.editTextSearch);
    }

    private void initialisation() {
        addReferenceViewModel = ViewModelProviders
                .of(this).get(AddReferenceViewModel.class);

        addReferenceTableAdapter = new AddReferenceTableAdapter(
                getContext(),
                addReferenceViewModel,
                idConsultation);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(addReferenceTableAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setRecyclerView();
        searchFunction();
        onDialogCancel();

    }

    private void setRecyclerView() {
        addReferenceViewModel.
                getDoctorList()
                .observe(getViewLifecycleOwner(), medecinList -> {
                    addReferenceTableAdapter.setMedecinList(medecinList);
                    addReferenceTableAdapter.notifyDataSetChanged();
                });
        addReferenceViewModel.setFilter("%%");
    }

    private void searchFunction() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchAttr = charSequence.toString();
                if (!searchAttr.equals("")) {
                    addReferenceViewModel.setFilter("%" + searchAttr + "%");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void onDialogCancel() {
        cancel.setOnClickListener(view -> dismiss());
    }
}