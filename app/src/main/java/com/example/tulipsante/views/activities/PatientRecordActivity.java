package com.example.tulipsante.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.views.adapters.PatientRecordFragmentAdapter;
import com.example.tulipsante.R;
import com.example.tulipsante.views.dialogFragment.MailPdfDialogFragment;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PatientRecordActivity extends AppCompatActivity {
    private ArrayList<String> basicData = new ArrayList<>();

    private ImageView profilePhoto;
    private TextView
            textViewId,
            textViewFull,
            textViewGender,
            textViewAge,
            textViewBirthDate,
            textBloodGroup;
    private CardView cardViewBack, cardViewRelation, cardViewPermission;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private Patient patient;
    private Date date;
    private Calendar calendar;

    private SharedPreferences sharedPreferences;

    private MailPdfDialogFragment dialogFragment;

    private void initViews() {
        cardViewBack = findViewById(R.id.cardViewBack);
        profilePhoto = findViewById(R.id.profileCircularImage);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPageSearch);
        textViewId = findViewById(R.id.textViewId);
        textViewFull = findViewById(R.id.textViewFull);
        textViewGender = findViewById(R.id.textViewGender);
        textViewAge = findViewById(R.id.textViewAge);
        textViewBirthDate = findViewById(R.id.textViewDate);
        textBloodGroup = findViewById(R.id.textViewBloodGroup);
        cardViewRelation = findViewById(R.id.cardViewRelation);
        cardViewPermission = findViewById(R.id.cardViewPermission);
    }

    private void initialisation() {
        patient = getIntent().getParcelableExtra("Patient");
        calendar = Calendar.getInstance();
        try {
            date = GeneralPurposeFunctions.getStringDate(patient.getDateNaissancePatient());
            calendar.setTime(date);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        setBasicDataList();
        FragmentManager fm = getSupportFragmentManager();
        PatientRecordFragmentAdapter patientRecordFragmentAdapter = new PatientRecordFragmentAdapter(
                fm,
                getLifecycle(),
                GeneralPurposeFunctions.getAge(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_YEAR)),
                patient.getGenrePatient(),
                basicData);
        viewPager2.setAdapter(patientRecordFragmentAdapter);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(getIntent().getStringExtra("From").equals("Consultation")) {
            cardViewPermission.setEnabled(true);
            cardViewRelation.setEnabled(false);
            cardViewPermission.setVisibility(View.VISIBLE);
            cardViewRelation.setVisibility(View.INVISIBLE);
        }
    }

    private void setUpViewPager() {
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.dashboard)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.patient_profile)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.medical_history)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.historique_consultation)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.vaccination_history)));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        viewPager2.setOffscreenPageLimit(5);
    }

    private void setBasicDataList() {
        basicData.add(patient.getPhotoPatient());
        basicData.add(patient.getIdPatient());
        basicData.add(patient.getPrenomPatient());
        basicData.add(patient.getNomPatient());
        basicData.add(patient.getGenrePatient());
        basicData.add(GeneralPurposeFunctions.getAge(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR)));
        basicData.add(patient.getDateNaissancePatient());
        basicData.add(patient.getGroupeSanguinPatient());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_patient_record);

        initViews();
        initialisation();
        setSharedPreferencesPatient(patient.getIdPatient());
        setUpViewPager();

        setProfileImage();
        setBasicDetails();
        onBackButtonPressed();
        onRelationButtonPressed();
        onPermissionButtonPressed();
        onPatientPicturePressed();
    }

    private void onPatientPicturePressed() {
        profilePhoto.setOnClickListener(view -> {
            String pdfPath = Environment
                    .getExternalStoragePublicDirectory(
                            "Tulip_sante"
                                    +File.separator
                                    +"Patients"
                                    +File.separator
                                    +patient.getIdPatient()
                                    +File.separator
                                    +"Personal"
                                    +File.separator
                    ).toString();
            File file = new File(pdfPath, patient.getNomPatient() + ".pdf");
            dialogFragment = new MailPdfDialogFragment(file);
            dialogFragment.show(getSupportFragmentManager(),"ModalBottomSheet");
        });
    }

    private void onPermissionButtonPressed() {
        cardViewPermission.setOnClickListener(view -> {
            Intent intent = new Intent(PatientRecordActivity.this, PermissionManagementActivity.class);
            startActivity(intent);
        });
    }

    private void onRelationButtonPressed() {
        cardViewRelation.setOnClickListener(view -> {
            Intent intent = new Intent(PatientRecordActivity.this, RelationManagementActivity.class);
            startActivity(intent);
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

    private void onBackButtonPressed() {
        cardViewBack.setOnClickListener(view -> showAlertDialogButtonClicked());
    }

    @Override
    public void onBackPressed() {
        showAlertDialogButtonClicked();
    }

    private void setSharedPreferencesPatient(String idPatient) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("IDPATIENT", idPatient);
        editor.apply();
    }

    private void setProfileImage() {
        String result = basicData.get(0);
        File myPath = new File(
                Environment
                        .getExternalStorageDirectory()+
                        File.separator+
                        "Tulip_sante/Patients/"+
                        basicData.get(1)+
                        "/Personal/"+
                        result);
        if(myPath.exists()) {
            Glide.with(this).load(myPath).into(profilePhoto);
        }
    }

    private void setBasicDetails() {
        textViewId.setText(basicData.get(1));
        textViewFull.setText(basicData.get(3) + " " + basicData.get(2));
        textViewGender.setText(basicData.get(4));
        textViewAge.setText(basicData.get(5));
        textViewBirthDate.setText(basicData.get(6));
        textBloodGroup.setText(basicData.get(7));
    }

    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.confirm));
        builder.setMessage(getResources().getString(R.string.are_you_sure_that_you_want_to_leave_the_module));

        builder.setPositiveButton("Continue", (dialogInterface, i) -> finish());
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
