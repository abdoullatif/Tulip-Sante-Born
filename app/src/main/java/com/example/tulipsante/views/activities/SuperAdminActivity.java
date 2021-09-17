package com.example.tulipsante.views.activities;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.R;
import com.example.tulipsante.views.fragments.DashboardSAFragment;
import com.example.tulipsante.views.fragments.SettingsSAFragment;

public class SuperAdminActivity extends AppCompatActivity {
    private CardView
            cardViewDashboard,
            cardViewLogOut,
            cardViewSettings;

    private TextView textViewUsername;

    private SharedPreferences sharedPreferences;

    private void initViews() {
        cardViewDashboard = findViewById(R.id.cardViewDashboard);
        cardViewSettings = findViewById(R.id.cardViewSetting);
        cardViewLogOut = findViewById(R.id.cardViewLogout);
        textViewUsername = findViewById(R.id.textViewUsername);
    }

    private void initialisation() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        openFragment(DashboardSAFragment.newInstance("",""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setApplicationTheme();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_super_admin);

        initViews();
        initialisation();

        setUsername();
        onNavigationBarItemPressed();
    }

    private void setUsername() {
        textViewUsername.setText(sharedPreferences.getString("SAUsername", null));
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Log-Out to leave!", Toast.LENGTH_SHORT).show();
    }

    private void setApplicationTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentTheme = sharedPreferences.getString("current_Theme",null);
        if(currentTheme.equals("dark")) {
            setTheme(R.style.AppTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void onNavigationBarItemPressed() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.opaque2, typedValue, true);
        @ColorInt int color = typedValue.data;
        cardViewDashboard.setOnClickListener(view -> {
            cardViewSettings.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewDashboard.setCardBackgroundColor(color);
            openFragment(DashboardSAFragment.newInstance("",""));
        });
        cardViewSettings.setOnClickListener(view -> {
            cardViewSettings.setCardBackgroundColor(color);
            cardViewDashboard.setCardBackgroundColor(getColor(R.color.transparent));
            openFragment(SettingsSAFragment.newInstance("",""));
        });
        cardViewLogOut.setOnClickListener(view -> {
            showAlertDialogButtonSaveClicked();
        });
    }

    public void showAlertDialogButtonSaveClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.confirm));
        builder.setMessage(getText(R.string.are_you_sure));

        builder.setPositiveButton("Log-Out", (dialogInterface, i) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("IDSA");
            editor.apply();
            finish();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}