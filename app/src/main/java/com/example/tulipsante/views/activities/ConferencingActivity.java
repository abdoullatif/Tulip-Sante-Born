package com.example.tulipsante.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.interfaces.OnDeleteClicked;
import com.example.tulipsante.interfaces.OnSaveListener;
import com.example.tulipsante.models.ConferenceContact;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.viewModel.ConferencingViewModel;
import com.example.tulipsante.views.adapters.ConferencingTableAdapter;
import com.example.tulipsante.views.dialogFragment.AddUserDialogFragment;

import java.util.List;

public class ConferencingActivity extends AppCompatActivity
        implements OnSaveListener, OnDeleteClicked, SwipeRefreshLayout.OnRefreshListener {

    // Views
    private CardView cardViewBack, buttonAdd, buttonSearch;
    private EditText editTextSearch;
    private RecyclerView recViewContacts;
    private SwipeRefreshLayout swipeRefresh;
    private TextView textViewNoData;
    private TableLayout tableLayout;

    // Adapter
    private ConferencingTableAdapter conferencingTableAdapter;

    // View Model
    private ConferencingViewModel conferencingViewModel;

    // Fragment
    private AddUserDialogFragment addUserDialogFragment;


    private void initViews() {
        cardViewBack = findViewById(R.id.cardViewBack);
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonAdd = findViewById(R.id.buttonAdd);
        recViewContacts = findViewById(R.id.recViewContacts);
        buttonSearch = findViewById(R.id.buttonSearch);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        textViewNoData = findViewById(R.id.textViewNoData);
        tableLayout = findViewById(R.id.tabLayout);
    }

    private void initialisation() {
        conferencingViewModel = ViewModelProviders
                .of(this)
                .get(ConferencingViewModel.class);
        conferencingTableAdapter = new ConferencingTableAdapter(this);
        recViewContacts.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recViewContacts.setAdapter(conferencingTableAdapter);
        swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_conferencing);

        initViews();
        initialisation();

        setContactData();
        onBackButtonPressed();
        onAddUserPressed();
        onSearchButtonPressed();
    }

    @Override
    public void onRefresh() {
        editTextSearch.setText("");
        setContactData();
        new Handler().postDelayed(() -> swipeRefresh.setRefreshing(false),2000);
    }

    private void onSearchButtonPressed() {
        buttonSearch.setOnClickListener(view -> {
            String searchString = editTextSearch.getText().toString().trim();
            GeneralPurposeFunctions.hideKeyboard(this);
            conferencingTableAdapter.clear();
            conferencingTableAdapter
                    .setConfContactList(conferencingViewModel
                            .getConfByName("%"+searchString+"%"));
            conferencingTableAdapter.notifyDataSetChanged();
        });
    }

    private void onAddUserPressed() {
        buttonAdd.setOnClickListener(view -> {
            addUserDialogFragment =
                    new AddUserDialogFragment(ConferencingActivity.this);
            addUserDialogFragment.show(getSupportFragmentManager(),"ModalBottomSheet");
        });
    }

    private void setContactData() {
        List<ConferenceContact> confContactList = conferencingViewModel.getConfContact();
        if(confContactList.size() <= 0) {
            textViewNoData.setVisibility(View.VISIBLE);
            tableLayout.setVisibility(View.INVISIBLE);
            editTextSearch.setVisibility(View.INVISIBLE);
            buttonSearch.setVisibility(View.INVISIBLE);
        }
        else {
            textViewNoData.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.VISIBLE);
            editTextSearch.setVisibility(View.VISIBLE);
            buttonSearch.setVisibility(View.VISIBLE);
            conferencingTableAdapter.setConfContactList(confContactList);
            conferencingTableAdapter.notifyDataSetChanged();
        }
    }

    private void setApplicationTheme() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String currentTheme = sharedPreferences.getString("current_Theme",null);
        if(currentTheme.equals("dark")) {
            setTheme(R.style.AppTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
    }

    private void onBackButtonPressed() {
        cardViewBack.setOnClickListener(view -> {
            finish();
        });
    }

    public static void skype(String number, Context context) {
        try {
            Intent sky = new Intent("android.intent.action.VIEW");
            sky.setData(Uri.parse("skype:"+ number));
            Log.d("UTILS", "skype: "+ number);
            context.startActivity(sky);
        }
        catch (ActivityNotFoundException e) {
            Log.e("SKYPE CALL", "skype failed ", e);
        }

    }

    @Override
    public void onSavePressed() {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            setContactData();
        }
    }

    @Override
    public void onSavePressed(String fullName, String id) {
        conferencingViewModel.insertConfContact(fullName,id);
        addUserDialogFragment.dismiss();
        Toast.makeText(this, "Inserted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(String idRelation) {
        showAlertDialogButtonClicked(idRelation);
    }

    public void showAlertDialogButtonClicked(String idRelation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete the contact?");

        builder.setPositiveButton("Yes Sure", (dialogInterface, i) -> {
            conferencingViewModel.deleteConfContact(idRelation);
            setContactData();
            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}