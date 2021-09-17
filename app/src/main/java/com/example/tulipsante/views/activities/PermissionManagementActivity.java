package com.example.tulipsante.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.interfaces.OnDeleteClicked;
import com.example.tulipsante.models.PermissionXMedecin;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.viewModel.PermissionManagementViewModel;
import com.example.tulipsante.views.adapters.PermissionManagementTableAdapter;
import com.example.tulipsante.views.fragments.PermissionDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class PermissionManagementActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener, OnDeleteClicked {
    private List<PermissionXMedecin> permissionXMedecinList;
    private SharedPreferences sharedPreferences;

    // Views
    private CardView cardViewBack, cardViewAddPer;
    private RecyclerView recViewPermission;
    private TextView textViewNoData;
    private SwipeRefreshLayout refreshLayout;

    // Adapter
    private PermissionManagementTableAdapter tableAdapter;

    // View Model
    private PermissionManagementViewModel managementViewModel;

    // Dialog fragment
    private PermissionDialogFragment bottomSheetDialog;

    public void initViews() {
        cardViewBack = findViewById(R.id.cardViewBack);
        cardViewAddPer = findViewById(R.id.cardViewAdd);
        recViewPermission = findViewById(R.id.recViewPermission);
        textViewNoData = findViewById(R.id.textViewNoData);
        refreshLayout = findViewById(R.id.swipeRefresh);
    }

    public void initialisation() {
        permissionXMedecinList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        tableAdapter = new PermissionManagementTableAdapter(this, getSupportFragmentManager());
        managementViewModel = ViewModelProviders
                .of(this)
                .get(PermissionManagementViewModel.class);

        recViewPermission.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recViewPermission.setAdapter(tableAdapter);
        refreshLayout.setOnRefreshListener(this);
    }

    private void setApplicationTheme() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String currentTheme = sharedPreferences.getString("current_Theme",null);
        if(currentTheme.equals("dark")) {
            setTheme(R.style.AppTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_permission_management);

        initViews();
        initialisation();

        onBackButtonPressed();
        setData();
        onAddButtonPressed();

    }

    @Override
    public void onRefresh() {
        tableAdapter.clear();
        setData();
        new Handler().postDelayed(() -> refreshLayout.setRefreshing(false),1500);
    }

    private void onBackButtonPressed() {
        cardViewBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void setData() {
        new Thread(() -> {
            permissionXMedecinList.addAll(managementViewModel.getPermissions());
            runOnUiThread(() -> {
                if(permissionXMedecinList.size() > 0) {
                    textViewNoData.setVisibility(View.INVISIBLE);
                    tableAdapter.setPermissionList(permissionXMedecinList);
                    tableAdapter.notifyDataSetChanged();
                }
                else {
                    textViewNoData.setVisibility(View.VISIBLE);
                }
            });
        }).start();
    }

    private void onAddButtonPressed() {
        cardViewAddPer.setOnClickListener(view -> {
            if(
                    permissionXMedecinList.isEmpty() ||
                    GeneralPurposeFunctions.hasExpired(permissionXMedecinList.get(0).getDateExpiration()) ||
                    permissionXMedecinList.get(0).getType().equals("private")
            ) {
                bottomSheetDialog = new PermissionDialogFragment(
                        this, sharedPreferences.getString("IDPATIENT",null));
                bottomSheetDialog.show(getSupportFragmentManager(),"ModalBottomSheet");
            }
            else {
                Toast
                        .makeText(this, "Have valid permission!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void onClick(String param) {
        showAlertDialogButtonClicked(param);
    }

    public void showAlertDialogButtonClicked(String param) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to cancel the permission?");

        builder.setPositiveButton("Yes Sure", (dialogInterface, i) -> {
            managementViewModel.cancelPermission(param);
            Toast.makeText(this, "Canceled!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                tableAdapter.clear();
                permissionXMedecinList.clear();
                setData();
            },1000);

        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}