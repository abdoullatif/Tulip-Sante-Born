package com.example.tulipsante.views.activities;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tulipsante.viewModel.MainMenuViewModel;
import com.example.tulipsante.views.dialogFragment.VersionDialogFragment;
import com.example.tulipsante.views.fragments.DashBoardFragment;
import com.example.tulipsante.views.fragments.PatientFragment;
import com.example.tulipsante.views.fragments.ProfileFragment;
import com.example.tulipsante.interfaces.DashBoardListener;
import com.example.tulipsante.interfaces.NewIntent;
import com.example.tulipsante.R;
import com.example.tulipsante.views.fragments.SupportFragment;
import com.example.tulipsante.views.fragments.SettingsFragment;

import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity implements DashBoardListener {
    private CardView
            cardViewDashboard,
            cardViewPatient,
            cardViewProfile,
            cardViewLogOut,
            cardViewSettings,
            cardViewSupport;
    private ImageView imageViewLogo;

    private NewIntent listener;

    // ViewModel
    private MainMenuViewModel mainMenuViewModel;

    // Bottom diaglog
    private VersionDialogFragment bottomSheetDialog;

    private SharedPreferences sharedPreferences;


    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof NewIntent) {
            listener = (NewIntent) fragment;
        }
    }

    private void initViews() {
        cardViewDashboard = findViewById(R.id.cardViewDashboard);
        cardViewPatient = findViewById(R.id.cardViewPatient);
        cardViewProfile = findViewById(R.id.cardViewProfile);
        cardViewSettings = findViewById(R.id.cardViewSetting);
        cardViewLogOut = findViewById(R.id.cardViewLogout);
        cardViewSupport = findViewById(R.id.cardViewSupport);
        imageViewLogo = findViewById(R.id.imageViewLogo);
    }

    private void initialisation() {
        mainMenuViewModel = ViewModelProviders.of(this).get(MainMenuViewModel.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setApplicationLanguage();
        setContentView(R.layout.activity_main_menu);

        initViews();
        initialisation();
        openFragment(DashBoardFragment.newInstance("",""));

        onNavigationBarItemPressed();
        onImageViewLogoPressed();
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

    private void setApplicationLanguage() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPreferences.getString("Locale.Helper.Selected.Language",null);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
//        LocaleHelper.setLocale(this, language);
    }

    private void onImageViewLogoPressed() {
        imageViewLogo.setOnLongClickListener(view -> {
            bottomSheetDialog = new VersionDialogFragment(MainMenuActivity.this);
            bottomSheetDialog.show(getSupportFragmentManager(),"ModalBottomSheet");
            return false;
        });
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Log-Out to leave!", Toast.LENGTH_SHORT).show();
    }

    private void onNavigationBarItemPressed() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.opaque2, typedValue, true);
        @ColorInt int color = typedValue.data;
        cardViewDashboard.setOnClickListener(view -> {
            cardViewSettings.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewPatient.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewProfile.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewDashboard.setCardBackgroundColor(color);
            cardViewSupport.setCardBackgroundColor(getColor(R.color.transparent));
            openFragment(DashBoardFragment.newInstance("",""));
        });
        cardViewPatient.setOnClickListener(view -> {
            cardViewSettings.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewPatient.setCardBackgroundColor(color);
            cardViewProfile.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewDashboard.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewSupport.setCardBackgroundColor(getColor(R.color.transparent));
            openFragment(PatientFragment.newInstance("",""));
        });
        cardViewProfile.setOnClickListener(view -> {
            cardViewSettings.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewProfile.setCardBackgroundColor(color);
            cardViewPatient.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewDashboard.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewSupport.setCardBackgroundColor(getColor(R.color.transparent));
            openFragment(ProfileFragment.newInstance("",""));
        });
        cardViewSettings.setOnClickListener(view -> {
            cardViewSettings.setCardBackgroundColor(color);
            cardViewProfile.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewPatient.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewDashboard.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewSupport.setCardBackgroundColor(getColor(R.color.transparent));
            openFragment(SettingsFragment.newInstance("",""));
        });
        cardViewLogOut.setOnClickListener(view -> {
            showAlertDialogButtonSaveClicked();
        });
        cardViewSupport.setOnClickListener(view -> {
            cardViewSettings.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewPatient.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewProfile.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewDashboard.setCardBackgroundColor(getColor(R.color.transparent));
            cardViewSupport.setCardBackgroundColor(color);
            openFragment(SupportFragment.newInstance("",""));
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        listener.onNewIntentCreated(intent);
    }

    // Dashboard navigation

    @Override
    public void onViewAllPatientPressed() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.opaque2, typedValue, true);
        @ColorInt int color = typedValue.data;
        cardViewSettings.setCardBackgroundColor(getColor(R.color.transparent));
        cardViewPatient.setCardBackgroundColor(color);
        cardViewProfile.setCardBackgroundColor(getColor(R.color.transparent));
        cardViewDashboard.setCardBackgroundColor(getColor(R.color.transparent));
        cardViewSupport.setCardBackgroundColor(getColor(R.color.transparent));
        openFragment(PatientFragment.newInstance("",""));
    }

    @Override
    public void onViewAllMessagePressed() {
        Intent intent = new Intent(this, HistoriqueActivity.class);
        startActivity(intent);
    }

    @Override
    public void onViewAllProfilePressed() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.opaque2, typedValue, true);
        @ColorInt int color = typedValue.data;
        cardViewSettings.setCardBackgroundColor(getColor(R.color.transparent));
        cardViewProfile.setCardBackgroundColor(color);
        cardViewPatient.setCardBackgroundColor(getColor(R.color.transparent));
        cardViewDashboard.setCardBackgroundColor(getColor(R.color.transparent));
        cardViewSupport.setCardBackgroundColor(getColor(R.color.transparent));
        openFragment(ProfileFragment.newInstance("",""));
    }

    public void showAlertDialogButtonSaveClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.confirm));
        builder.setMessage(getText(R.string.are_you_sure));

        builder.setPositiveButton("Log-Out", (dialogInterface, i) -> {
            mainMenuViewModel.logout(sharedPreferences.getString("IDMEDECIN", null));
            finish();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
