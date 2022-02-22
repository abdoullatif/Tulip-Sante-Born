package com.example.tulipsante.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tulipsante.interfaces.DisableNfc;
import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.utils.PdfGenerator;
import com.example.tulipsante.viewModel.ConsultationActivityViewModel;
import com.example.tulipsante.views.adapters.ConsultationFragmentAdapter;
import com.example.tulipsante.views.dialogFragment.AddReferenceDialogFragment;
import com.example.tulipsante.views.dialogFragment.AddRelationDialogFragment;
import com.example.tulipsante.views.dialogFragment.MailPdfDialogFragment;
import com.example.tulipsante.views.dialogFragment.ScanNFCDialogFragment;
import com.example.tulipsante.views.fragments.PermissionDialogFragment;
import com.example.tulipsante.R;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class ConsultationActivity extends AppCompatActivity implements View.OnClickListener , DisableNfc {
    private static final String TAG = "ee";

    private String idConsultation;
    private String clickedFrom;
    private Boolean hasConsulted;

    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;

    private ScanNFCDialogFragment nfcDialogFragment;

    private MailPdfDialogFragment dialogFragment;

    // Views
    private ImageView profilePhoto;
    private TextView
            textViewConsultationId,
            textViewId,
            textViewFull,
            textViewGender,
            textViewAge,
            textViewBirthDate,
            textBloodGroup;
    private CardView cardViewBack,cardViewSave, cardViewMail, cardViewRecord, cardViewRef;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    // Dialog fragment
    private PermissionDialogFragment bottomSheetDialog;

    // View Model
    private ConsultationActivityViewModel consultationActivityViewModel;

    private Patient patient;

    private void initViews() {
        cardViewBack = findViewById(R.id.cardViewBack);
        cardViewSave = findViewById(R.id.cardViewSave);
        profilePhoto = findViewById(R.id.profileCircularImage);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPageSearch);
        textViewConsultationId = findViewById(R.id.textViewConsultationId);
        textViewId = findViewById(R.id.textViewId);
        textViewFull = findViewById(R.id.textViewFull);
        textViewGender = findViewById(R.id.textViewGender);
        textViewAge = findViewById(R.id.textViewAge);
        textViewBirthDate = findViewById(R.id.textViewDate);
        textBloodGroup = findViewById(R.id.textViewBloodGroup);
        cardViewMail = findViewById(R.id.cardViewMail);
        cardViewRecord = findViewById(R.id.cardViewRecord);
        cardViewRef = findViewById(R.id.cardViewRef);
    }

    private void init() {
        clickedFrom = getIntent().getStringExtra("From");
        assert clickedFrom != null;
        if(!clickedFrom.equals("Patient List")) {
            cardViewSave.setVisibility(View.INVISIBLE);
            cardViewRecord.setVisibility(View.INVISIBLE);
            cardViewMail.setVisibility(View.VISIBLE);
        }

        // todo if doctor is nurse then disable diagnostic, prescription, advice

    }

    private void initialisation() {
        patient = getIntent().getParcelableExtra("Patient");
        consultationActivityViewModel = ViewModelProviders
                .of(this)
                .get(ConsultationActivityViewModel.class);
        String idPatient = patient.getIdPatient();
        hasConsulted = consultationActivityViewModel
                .hasPatientConsulted(idPatient);

        FragmentManager fm = getSupportFragmentManager();
        // Adapter
        ConsultationFragmentAdapter consultationFragmentAdapter = new ConsultationFragmentAdapter(
                fm,
                getLifecycle(),
                idPatient,
                clickedFrom,
                getIntent().getStringExtra("consultationId"),
                hasConsulted);
        viewPager2.setAdapter(consultationFragmentAdapter);

        consultationActivityViewModel.setSharedPreferencesPatient(patient.getIdPatient());
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);
    }

    private void setUpViewPager() {
        if(hasConsulted) {
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.medical_status)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.vital_signs)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.investigation_procedure)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.diagnostic)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.prescription)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.advice)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.vaccination)));
        }
        else {
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.medical_history)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.medical_status)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.vital_signs)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.investigation_procedure)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.diagnostic)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.prescription)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.advice)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.vaccination)));

        }

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

        viewPager2.setOffscreenPageLimit(8);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_consultation);

        initViews();
        init();
        initialisation();
        setUpViewPager();

        setConsultationId();
        setProfileImage();
        setBasicDetails();
        onBackButtonPressed();
        onSaveButtonPressed();
        onRecordButtonPressed();
        onButtonMailPressed();
        onPatientPicturePressed();
        onRefButtonPressed();
    }

    private void onRefButtonPressed() {
        cardViewRef.setOnClickListener(view -> {
            showDialogBox();
        });
    }

    public void showDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you wish to refer patient?");
        builder.setMessage("Do you wish to refer current consultation to another doctor?");

        builder.setPositiveButton("Yes/Continue", (dialogInterface, i) -> {
            AddReferenceDialogFragment dialogFragment = new AddReferenceDialogFragment(this, idConsultation);
            dialogFragment.show(getSupportFragmentManager(), "ModalBottomSheet");
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
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
            File file = new File(pdfPath, patient.getIdPatient() + ".pdf");
            dialogFragment = new MailPdfDialogFragment(file);
            dialogFragment.show(getSupportFragmentManager(),"ModalBottomSheet");
        });
    }

    private void onButtonMailPressed() {
        cardViewMail.setOnClickListener(view -> {
            // TODO: 12/04/21 Generate consultation pdf
            Consultation consultation = new Consultation();
            consultation.setIdConsultation(getIntent().getStringExtra("consultationId"));
            consultation.setDateConsultation("");
            try {
                PdfGenerator.createPdf(
                        consultation,
                        patient,
                        this,
                        getParent()
                );
            }
            catch (Exception e) {
                e.printStackTrace();
            }

//            dialogFragment = new MailPdfDialogFragment();
//            dialogFragment.show(getSupportFragmentManager(),"ModalBottomSheet");

        });
    }

    private void setApplicationTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentTheme = sharedPreferences.getString("current_Theme",null);
        assert currentTheme != null;
        if(currentTheme.equals("dark")) {
            setTheme(R.style.AppTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
    }

    private void onRecordButtonPressed() {
        cardViewRecord.setOnClickListener(view -> {
            nfcDialogFragment = new ScanNFCDialogFragment(ConsultationActivity.this);
            nfcDialogFragment.show(getSupportFragmentManager(),"ModalBottomSheet");
            enableForegroundMode();
        });
    }

    private void onSaveButtonPressed() {
        cardViewSave.setOnClickListener(this::showAlertDialogButtonSaveClicked);
    }

    private void setConsultationId() {
        if(clickedFrom.equals("Patient List")) {
            idConsultation = consultationActivityViewModel.getConsultationId();
            textViewConsultationId.setText(idConsultation);
        }
        else {
            textViewConsultationId.setText(getIntent().getStringExtra("consultationId"));
        }
    }

    private void setProfileImage() {
        String result = patient.getPhotoPatient();
        File myPath = new File(
                Environment
                        .getExternalStorageDirectory()+
                        File.separator+
                        "Tulip_sante/Patients/"+
                        patient.getIdPatient()+
                        "/Personal/"+
                        result);
        if(myPath.exists()) {
            Glide.with(this).load(myPath).into(profilePhoto);
        }
    }

    private void setBasicDetails() {
        Calendar calendar = Calendar.getInstance();
        Date date = GeneralPurposeFunctions.getStringDate(patient.getDateNaissancePatient());
        calendar.setTime(date);
        String name = patient.getNomPatient() + " " + patient.getPrenomPatient();
        textViewId.setText(patient.getIdPatient());
        textViewFull.setText(name);
        textViewGender.setText(patient.getGenrePatient());
        textViewAge.setText(GeneralPurposeFunctions.getAge(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR)));
        textViewBirthDate.setText(patient.getDateNaissancePatient());
        textBloodGroup.setText(patient.getGroupeSanguinPatient());
    }

    private void onBackButtonPressed() {
        cardViewBack.setOnClickListener(view -> showAlertDialogButtonClicked());
    }

    @Override
    public void onBackPressed() {
        showAlertDialogButtonClicked();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonSave) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            assert tab != null;
            tab.select();
        }
    }

    public void showAlertDialogButtonSaveClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure that you want to leave the module?");

        builder.setPositiveButton("Continue", (dialogInterface, i) -> {
            if(consultationActivityViewModel.consultationExist()) {
                if (consultationActivityViewModel.hasValidPermission(patient.getIdPatient())) {
                    bottomSheetDialog = new PermissionDialogFragment(ConsultationActivity.this,
                            patient.getIdPatient());
                    bottomSheetDialog.show(getSupportFragmentManager(), "ModalBottomSheet");
                } else {
                    consultationActivityViewModel.unSetSharedPreferences();
                    finish();
                }
            }
            else {
                Toast
                        .makeText(
                                this, "Enter data before validation!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert / Warning");
        builder.setMessage(getResources().getString(R.string.by_going_the_data_entered_will_be_discarded_do_you_want_to_continue));

        builder.setPositiveButton("Yes/Continue", (dialogInterface, i) -> {
            consultationActivityViewModel.deleteDataEntered();
            consultationActivityViewModel.unSetSharedPreferences();
            finish();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // NFC stats here
    public void enableForegroundMode() {
        Log.d(TAG, "enableForegroundMode");
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED); // filter for all
        IntentFilter[] writeTagFilters = new IntentFilter[] {tagDetected};
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
    }

    public void disableForegroundMode() {
        Log.d(TAG, "disableForegroundMode");
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            assert tag != null;
            byte[] uidBytes = tag.getId();
            boolean isEqual = consultationActivityViewModel.checkTagAndPatientMatch(uidBytes,patient.getUidPatient());
            if(isEqual) {
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(ConsultationActivity.this, PatientRecordActivity.class);
                patient = getIntent().getParcelableExtra("Patient");
                intent1.putExtra("From", "Consultation");
                intent1.putExtra("Patient", patient);
                intent1.putExtra("ActivityName", "Consultation");
                startActivity(intent1);
            } else {
                Toast.makeText(this, "Tag not correct!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onButtonClicked() {
        disableForegroundMode();
        nfcDialogFragment.dismiss();
    }
}