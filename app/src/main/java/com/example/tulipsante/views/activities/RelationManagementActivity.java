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
import com.example.tulipsante.interfaces.OnSaveListener;
import com.example.tulipsante.models.uIModels.RelationXPatient;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.RelationMViewModel;
import com.example.tulipsante.views.adapters.RelationMTableAdapter;
import com.example.tulipsante.views.dialogFragment.AddRelationDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class RelationManagementActivity extends AppCompatActivity implements OnDeleteClicked, OnSaveListener, SwipeRefreshLayout.OnRefreshListener {
    private List<RelationXPatient> relationXPatientList = new ArrayList<>();
    // Views
    private CardView cardViewBack, cardViewAddRelation;
    private RecyclerView recViewPatients;
    private TextView textViewNoData;
    private SwipeRefreshLayout refreshLayout;

    // View Model
    private RelationMViewModel relationMViewModel;

    // Adapter
    private RelationMTableAdapter relationMTableAdapter;

    private AddRelationDialogFragment dialogFragment;

    private void initViews() {
        cardViewBack = findViewById(R.id.cardViewBack);
        cardViewAddRelation = findViewById(R.id.cardViewAdd);
        recViewPatients = findViewById(R.id.recyclerViewPatients);
        textViewNoData = findViewById(R.id.textViewNoData);
        refreshLayout = findViewById(R.id.swipeRefresh);
    }

    private void initialisation() {
        relationMTableAdapter = new RelationMTableAdapter();
        relationMViewModel = ViewModelProviders.of(this).get(RelationMViewModel.class);
        recViewPatients.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recViewPatients.setAdapter(relationMTableAdapter);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_relation_management);

        initViews();
        initialisation();

        setData();
        onBackButtonPressed();
        onAddRelationButtonPressed();
    }

    private void setData() {
        new Thread(() -> {
            relationXPatientList.addAll(relationMViewModel.getRelations());
            runOnUiThread(() -> {
                if(relationXPatientList.size() > 0) {
                    textViewNoData.setVisibility(View.INVISIBLE);
                    relationMTableAdapter.setRelationData(relationXPatientList);
                    relationMTableAdapter.notifyDataSetChanged();
                }
                else {
                    textViewNoData.setVisibility(View.VISIBLE);
                }
            });
        }).start();
    }

    @Override
    public void onRefresh() {
        relationMTableAdapter.clear();
        setData();
        new Handler().postDelayed(() -> refreshLayout.setRefreshing(false),2000);
    }

    @Override
    public void onClick(String idRelation) {
        showAlertDialogButtonClicked(idRelation);
    }

    public void showAlertDialogButtonClicked(String idRelation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete the relation?");

        builder.setPositiveButton("Yes Sure", (dialogInterface, i) -> {
            relationMViewModel.deleteRelation(idRelation);
            relationMTableAdapter.clear();
            setData();
            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onSavePressed() {
        dialogFragment.dismiss();
        relationMTableAdapter.clear();
        setData();
    }

    @Override
    public void onSavePressed(String fullName, String id) {
        // empty
    }

    private void onAddRelationButtonPressed() {
        cardViewAddRelation.setOnClickListener(view -> {
            dialogFragment = new AddRelationDialogFragment(this);
            dialogFragment.show(getSupportFragmentManager(),"ModalBottomSheet");
        });
    }

    private void onBackButtonPressed() {
        cardViewBack.setOnClickListener(view -> {
            finish();
        });
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
}