package com.example.tulipsante.views.dialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.VaccinationHistoryViewModel;
import com.example.tulipsante.views.adapters.VaccMoreTableAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class VaccHistoryDialogFragment extends BottomSheetDialogFragment {
    private Context context;
    private String type;

    // Views
    private CardView cancel;
    private RecyclerView recyclerView;
    private TextView textViewMore;

    // View Model
    private VaccinationHistoryViewModel vaccinationHistoryViewModel;

    // Adapters
    private VaccMoreTableAdapter vaccMoreTableAdapter;

    public VaccHistoryDialogFragment(Context context, String type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vacchistorydialog, container, false);
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
        recyclerView = view.findViewById(R.id.recViewVaccination);
        textViewMore = view.findViewById(R.id.textViewMore);

    }

    private void initialisation() {
        vaccMoreTableAdapter = new VaccMoreTableAdapter();
        vaccinationHistoryViewModel = ViewModelProviders
                .of(this).get(VaccinationHistoryViewModel.class);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(vaccMoreTableAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setHeader();
        setTable();
        onDialogCancel();

    }

    private void setHeader() {
        textViewMore.setText(type);
    }

    private void setTable() {
        vaccMoreTableAdapter.setVaccineTableDialogs(vaccinationHistoryViewModel.getVaccineMore(type));
        vaccMoreTableAdapter.notifyDataSetChanged();
    }

    private void onDialogCancel() {
        cancel.setOnClickListener(view -> dismiss());
    }
}
