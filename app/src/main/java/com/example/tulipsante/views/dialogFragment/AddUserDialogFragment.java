package com.example.tulipsante.views.dialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.tulipsante.interfaces.OnSaveListener;
import com.example.tulipsante.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddUserDialogFragment extends BottomSheetDialogFragment {
    private CardView cancel;
    private Context context;

    // View
    private EditText editTextName, editTextId;
    private CardView cardViewSave;

    private OnSaveListener listener;

    public AddUserDialogFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_adduserdialogfragment, container, false);
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
        editTextName = view.findViewById(R.id.editTextName);
        editTextId = view.findViewById(R.id.editTextId);
        cardViewSave = view.findViewById(R.id.cardViewSave);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = (OnSaveListener) context;

        initViews(view);

        onDialogCancelPressed();
        onSaveButtonPressed();
    }

    private void onSaveButtonPressed() {
        cardViewSave.setOnClickListener(view -> {
            String fullName = editTextName.getText().toString().trim();
            String id = editTextId.getText().toString().trim();
            if(!fullName.isEmpty() && !id.isEmpty()) {
                listener.onSavePressed(fullName, id);
            }
            else {
                Toast.makeText(context, "Fill all the fields!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onDialogCancelPressed() {
        cancel.setOnClickListener(view -> dismiss());
    }
}
