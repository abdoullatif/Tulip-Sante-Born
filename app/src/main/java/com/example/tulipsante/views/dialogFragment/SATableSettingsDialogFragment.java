package com.example.tulipsante.views.dialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.models.Parametre;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.SettingsSAViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class SATableSettingsDialogFragment extends BottomSheetDialogFragment {
    private Context context;
    private List<Parametre> parametreList;

    private CardView cancel, cardViewSave;
    private TextView
            eTTabId,
            eTFtp,
            eTFtpPass,
            eTDB,
            eTDBPass,
            eTWeb;

    private SettingsSAViewModel settingsSAViewModel;

    public SATableSettingsDialogFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_s_a_table_settings_dialog, container, false);
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
        eTTabId = view.findViewById(R.id.eTTabId);
        eTFtp = view.findViewById(R.id.eTFtp);
        eTFtpPass = view.findViewById(R.id.eTFtpPass);
        eTDB = view.findViewById(R.id.eTDB);
        eTDBPass = view.findViewById(R.id.eTDBPass);
        eTWeb = view.findViewById(R.id.eTWeb);
        cardViewSave = view.findViewById(R.id.cardViewSave);
    }

    private void initialisation() {
        settingsSAViewModel = ViewModelProviders.of(this).get(SettingsSAViewModel.class);
        parametreList = new ArrayList<>();
        settingsSAViewModel.getParametreList().observe(this, parametres -> {
            if(!parametres.isEmpty()) {
                parametreList.addAll(parametres);
                Parametre parametre = parametres.get(0);
                eTTabId.setText(parametre.getDeviceName());
                eTFtp.setText(parametre.getFtpName());
                eTFtpPass.setText(parametre.getFtpPass());
                eTDB.setText(parametre.getDbName());
                eTDBPass.setText(parametre.getDbPass());
                eTWeb.setText(parametre.getWebSiteName());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        onDialogCancel();
        onButtonValidatePressed();
    }

    private void onButtonValidatePressed() {
        cardViewSave.setOnClickListener(view -> {
            String tabId = eTTabId.getText().toString().trim();
            String ftpName = eTFtp.getText().toString().trim();
            String ftpPass = eTFtpPass.getText().toString().trim();
            String dbName = eTDB.getText().toString().trim();
            String dbPass = eTDBPass.getText().toString().trim();
            String web = eTWeb.getText().toString().trim();
            if(!tabId.isEmpty()
                    && !ftpName.isEmpty()
                    && !ftpPass.isEmpty()
                    && !dbName.isEmpty()
                    && !dbPass.isEmpty()
                    && !web.isEmpty()
            ) {
                if(parametreList == null || parametreList.isEmpty()) {
                    settingsSAViewModel.insertParametre(new Parametre(
                            "1001",
                            tabId,
                            dbName,
                            dbPass,
                            ftpName,
                            ftpPass,
                            web
                    ));
                    dismiss();
                    Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    settingsSAViewModel.updateParametre(new Parametre(
                            "1001",
                            tabId,
                            dbName,
                            dbPass,
                            ftpName,
                            ftpPass,
                            web
                    ));
                    dismiss();
                    Toast.makeText(context, "Modified Successfully!", Toast.LENGTH_SHORT).show();
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