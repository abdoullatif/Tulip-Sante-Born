package com.example.tulipsante.views.dialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.LoginSAViewModel;
import com.example.tulipsante.views.activities.SuperAdminActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class LoginSADialogFragment extends BottomSheetDialogFragment {
    private Context context;
    private Activity activity;

    // Views
    private CardView cancel;
    private EditText editTextUsername, editTextPassword;
    private AppCompatButton buttonLogin;

    // View model
    private LoginSAViewModel loginSAViewModel;

    private String username;
    private String password;

    public LoginSADialogFragment(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_s_a_dialog, container, false);
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
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextUsername = view.findViewById(R.id.editTextUsername);
        buttonLogin = view.findViewById(R.id.buttonLogin);
    }

    private void initialisation() {
        loginSAViewModel = ViewModelProviders.of(this).get(LoginSAViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        onDialogCancel();
        onButtonLoginPressed();

    }

    private void onButtonLoginPressed() {
        buttonLogin.setOnClickListener(view -> {
            username = editTextUsername.getText().toString().trim();
            password = editTextPassword.getText().toString().trim();
            if(!username.isEmpty() && !password.isEmpty()) {
                boolean isValid = loginSAViewModel.checkValidLogin(username, password);
                if (isValid) {
                    Toast.makeText(context, "Login Success!", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(context, SuperAdminActivity.class);
                    startActivity(intent1);
                    activity.finish();
                } else {
                    Toast.makeText(context, "Username or Password incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(context, "Fill all the fields!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onDialogCancel() {
        cancel.setOnClickListener(view -> {
            dismiss();
        });
    }
}