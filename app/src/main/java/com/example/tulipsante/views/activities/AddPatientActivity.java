package com.example.tulipsante.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.models.ContactUrgence;
import com.example.tulipsante.utils.PdfGenerator;
import com.example.tulipsante.views.adapters.CommuneSpinnerAdapter;
import com.example.tulipsante.views.adapters.RegionSpinnerAdapter;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.models.Addresse;
import com.example.tulipsante.models.Commune;
import com.example.tulipsante.models.Contact;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.Region;
import com.example.tulipsante.viewModel.AddPatientViewModel;
import com.example.tulipsante.views.dialogFragment.ChooseImageDialogFragment;
import com.example.tulipsante.interfaces.DisableNfc;
import com.example.tulipsante.R;
import com.example.tulipsante.views.dialogFragment.ScanNFCDialogFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddPatientActivity extends AppCompatActivity implements DisableNfc, View.OnClickListener {
    private static final String TAG = AddPatientActivity.class.getName();
    private CardView cardViewBack, cardViewSave;
    private CardView cardViewTag;
    private CardView cardViewTagStatus;
    private ImageView circleImageView;
    private EditText editTextDateOfBirth;
    private TextView textViewChoosePhoto;

    private RadioGroup radioGroupGender, radioGroupSituation, radioGroupNationality, radioGroupRelation;
    private EditText surnameET, firstNameET, idNumberET;
    private EditText pMobileET, emailET, firstAddresseET, sMobileET;
    private EditText fullNameET, mobileNumberET ;
    private Spinner bloodGroupSpinner, regionSpinner, citySpinner;

    private RegionSpinnerAdapter regionSpinnerAdapter;
    private CommuneSpinnerAdapter communeSpinnerAdapter;

    private List<Region> rowItemRegion = new ArrayList<>();
    private List<Commune> rowItemCommune = new ArrayList<>();

    private String[] items = new String[]{
            "",
            "AB",
            "O+",
            "B+",
            "AB+",
            "A-",
            "O-",
            "B-",
            "AB-"
    };

    //Basic details
    private String
            surname,
            firstName,
            idNumber,
            dateOfBirth,
            gender,
            situation,
            nationality,
            bloodGroup;
    private String photoPath = "";
    private String uidPatient = "";

    //Contact Details
    private String primaryPhone,
        email,
        street,
        locality;
    private String secondaryPhone = "";

    //Emergency contact
    private String relation,
        fullName,
        mobileNumber;

    private String date;

    // View Model
    private AddPatientViewModel addPatientViewModel;

    protected NfcAdapter nfcAdapter;
    protected PendingIntent nfcPendingIntent;
    private ScanNFCDialogFragment bottomSheetDialog;
    private ChooseImageDialogFragment cIBottomSheetDialog;

    final Calendar calendar = Calendar.getInstance();

    private void initViews() {
        cardViewBack = findViewById(R.id.cardViewBack);
        cardViewTag = findViewById(R.id.cardViewTag);
        cardViewTagStatus = findViewById(R.id.cardViewTagStatus);
        editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth);
        textViewChoosePhoto = findViewById(R.id.textViewChangeProfilePic);
        circleImageView = findViewById(R.id.profileCircularImage);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        cardViewSave = findViewById(R.id.cardViewSave);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioGroupNationality = findViewById(R.id.radionGroupNationality);
        radioGroupRelation = findViewById(R.id.radionRelation);
        radioGroupSituation = findViewById(R.id.radionGroupSituation);

        regionSpinner = findViewById(R.id.regionSpinner);
        citySpinner = findViewById(R.id.citySpinner);

        surnameET = findViewById(R.id.editTextSurname);
        firstNameET = findViewById(R.id.editTextFirstName);
        idNumberET = findViewById(R.id.editTextIdentity);
        pMobileET = findViewById(R.id.editTextPrimaryMobile);
        sMobileET = findViewById(R.id.editTextSecondaryMobile);
        emailET = findViewById(R.id.editTextEmail);
        firstAddresseET = findViewById(R.id.editTextAddressStreet);
        fullNameET = findViewById(R.id.editTextFullNameRelation);
        mobileNumberET = findViewById(R.id.editTextRelationPhone);
    }

    private void initialisation() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);
        addPatientViewModel = ViewModelProviders.of(this).get(AddPatientViewModel.class);
        regionSpinnerAdapter = new RegionSpinnerAdapter(
                this,R.layout.spinner_region_list_item,R.id.textViewRegionListItem,rowItemRegion);
        communeSpinnerAdapter = new CommuneSpinnerAdapter(
                this,R.layout.spinner_region_list_item,R.id.textViewRegionListItem,rowItemCommune);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item,items);
        bloodGroupSpinner.setPopupBackgroundResource(R.color.colorAccent);
        bloodGroupSpinner.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_add_patient);

        initViews();
        initialisation();

        navigateBack();
        onAddTagPressed();
        openCalendarDialog();
        onChangePhotoPressed();
        onSaveButtonPressed();
        spinner();
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

    private void spinner() {
        rowItemRegion.addAll(addPatientViewModel.regionList());
        regionSpinner.setPopupBackgroundResource(R.color.colorAccent);
        regionSpinner.setAdapter(regionSpinnerAdapter);
        citySpinner.setPopupBackgroundResource(R.color.colorAccent);
        citySpinner.setAdapter(communeSpinnerAdapter);

        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj= adapterView.getItemAtPosition(i);
                Region region = (Region) obj;
                List<Commune> communes = addPatientViewModel.getCommuneFromRegion(region.getIdRegion());
                communeSpinnerAdapter.clear();
                rowItemCommune.addAll(communes);
                communeSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                communeSpinnerAdapter.clear();
            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj= adapterView.getItemAtPosition(i);
                Commune commune = (Commune) obj;
                locality = commune.getIdCommune();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private boolean validation() {
        boolean isValid = false;
        Calendar calendar1 = Calendar.getInstance();
        String myFormat = "yyyy-MM-dd hh:MM:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        RadioButton radioButtonGender =
                findViewById(radioGroupGender.getCheckedRadioButtonId());
        RadioButton radioButtonSituation =
                findViewById(radioGroupSituation.getCheckedRadioButtonId());
        RadioButton radioButtonNationality =
                findViewById(radioGroupNationality.getCheckedRadioButtonId());
        RadioButton radioButtonRelation =
                findViewById(radioGroupRelation.getCheckedRadioButtonId());
        if(
                radioButtonGender != null &&
                radioButtonSituation != null &&
                radioButtonNationality != null &&
                radioButtonRelation != null
        )
        {
            gender = radioButtonGender.getText().toString().trim();
            situation = radioButtonSituation.getText().toString().trim();
            nationality = radioButtonNationality.getText().toString().trim();
            relation = radioButtonRelation.getText().toString().trim();
        }

        surname = surnameET.getText().toString().trim();
        firstName = firstNameET.getText().toString().trim();
        dateOfBirth = editTextDateOfBirth.getText().toString().trim();
        idNumber = idNumberET.getText().toString().trim();
        bloodGroup = bloodGroupSpinner.getSelectedItem().toString().trim();
        primaryPhone = pMobileET.getText().toString().trim();
        secondaryPhone = sMobileET.getText().toString().trim();
        email = emailET.getText().toString().trim();
        street = firstAddresseET.getText().toString().trim();
        fullName = fullNameET.getText().toString().trim();
        mobileNumber = mobileNumberET.getText().toString().trim();
        Date dateD = calendar1.getTime();
        date = sdf.format(dateD);
        //if field are not empty
        if(
                !photoPath.isEmpty()&&
                !surname.isEmpty() &&
                !firstName.isEmpty()&&
                !dateOfBirth.isEmpty() &&
                !gender.isEmpty() &&
                !situation.isEmpty()&&
                !nationality.isEmpty() &&
                !bloodGroup.isEmpty() &&
                !locality.isEmpty() &&
                !relation.isEmpty() &&
                !fullName.isEmpty()
        ) {
            isValid = true;
        }
        return isValid;
    }

    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Would you like to validate all the entry?");

        builder.setPositiveButton("Continue", (dialogInterface, i) -> {
            String id = GeneralPurposeFunctions.idTable();
            File parentDirectory = new File(Environment.getExternalStorageDirectory()+File.separator+"Tulip_sante");
            File childDirectory1 = new File(parentDirectory+File.separator+"Patients");
            String photoName = photoPath;
            if(childDirectory1.exists()) {
                File childDirectory2 = new File(childDirectory1 + File.separator + id);
                File childDirectory3 = new File(childDirectory2 + File.separator + "Personal");
                File childDirectory4 = new File(childDirectory2 + File.separator + "Diagnostic");
                File file = new File(photoPath);
                photoName = file.getName();
                boolean dCreated2 = childDirectory2.mkdirs();
                boolean dCreated3 = childDirectory3.mkdirs();
                boolean dCreated4 = childDirectory4.mkdirs();
                System.out.println("creating directories");
                if(dCreated2 || dCreated3 || dCreated4) {
                    System.out.println("created");
                    GeneralPurposeFunctions.moveFile(photoPath, childDirectory3.toString() + File.separator + file.getName());
                    photoPath = childDirectory3.toString() + File.separator + file.getName();
                }
            }
            addPatientViewModel.insertNewPatient(
                    new Patient(
                            id,
                            surname,
                            firstName,
                            gender,
                            dateOfBirth,
                            bloodGroup,
                            photoName,
                            idNumber,
                            uidPatient,
                            nationality,
                            situation,
                            "",
                            date,
                            ""
                    ),
                    new Addresse(
                            GeneralPurposeFunctions.idTable(),
                            street,
                            locality,
                            ""
                    ),
                    new Contact(
                            GeneralPurposeFunctions.idTable(),
                            " ",
                            primaryPhone,
                            secondaryPhone,
                            email,
                            ""
                    ),
                    new ContactUrgence(
                            GeneralPurposeFunctions.idTable(),
                            "",
                            relation,
                            fullName,
                            mobileNumber,
                            ""
                    )
            );
            Calendar calendar = Calendar.getInstance();
            Date date = GeneralPurposeFunctions.getStringDate(dateOfBirth);
            calendar.setTime(date);
            try {
                PdfGenerator.createPatientCard(firstName, dateOfBirth, primaryPhone, gender, id);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddPatientActivity.this, ConsultationActivity.class);
            intent.putExtra("From", "Patient List");
            Patient patient = new Patient(
                    id,
                    surname,
                    firstName,
                    gender,
                    dateOfBirth,
                    bloodGroup,
                    photoName,
                    idNumber,
                    uidPatient,
                    nationality,
                    situation,
                    "",
                    "",
                    "");
            intent.putExtra("Patient", patient);
            startActivity(intent);
            finish();

        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onSaveButtonPressed() {
        cardViewSave.setOnClickListener(view -> {
            if(validation()) {
                showAlertDialogButtonClicked();
            }else {
                Toast.makeText(
                        AddPatientActivity.this,
                        "Fill All The Fields",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int LAUNCH_SECOND_ACTIVITY = 1;
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                String result = data.getStringExtra("imagePath");
                photoPath = result;
                circleImageView.setImageURI(Uri.parse(result));
            }
        }
    }

    private void onChangePhotoPressed() {
        textViewChoosePhoto.setOnClickListener(view -> {
            cIBottomSheetDialog = new ChooseImageDialogFragment(AddPatientActivity.this);
            cIBottomSheetDialog.show(getSupportFragmentManager(),"ModalBottomSheet");
        });
    }

    private void openCalendarDialog() {
        String myFormat = "yyyy/MM/dd";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        final DatePickerDialog.OnDateSetListener date = (datePicker, i, i1, i2) -> {
            calendar.set(Calendar.YEAR,i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DAY_OF_MONTH,i2);
            editTextDateOfBirth.setText(sdf.format(calendar.getTime()));
        };
        editTextDateOfBirth.setOnClickListener(view -> new DatePickerDialog(
                AddPatientActivity.this,
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void onAddTagPressed() {
        cardViewTag.setOnClickListener(view -> {
            bottomSheetDialog = new ScanNFCDialogFragment(AddPatientActivity.this);
            bottomSheetDialog.show(getSupportFragmentManager(),"ModalBottomSheet");
            enableForegroundMode();
        });
    }

    public void showAlertDialogButtonBackClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Entered data will be deleted?");

        builder.setPositiveButton("Continue", (dialogInterface, i) -> finish());
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void navigateBack() {
        cardViewBack.setOnClickListener(view -> {
            if(!validation()) {
                showAlertDialogButtonBackClicked();
            }
        });
    }

    // NFC stats here
    public void enableForegroundMode() {
        Log.d(TAG, "enableForegroundMode");
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED); // filter for all
        IntentFilter[] writeTagFilters = new IntentFilter[] {tagDetected};
        nfcAdapter.enableForegroundDispatch(
                this,
                nfcPendingIntent,
                writeTagFilters,
                null);
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
            cardViewTagStatus.setCardBackgroundColor(getColor(R.color.green));
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            // get the uid patient
            assert tag != null;
            uidPatient = GeneralPurposeFunctions.bytesToHexString(tag.getId());
            bottomSheetDialog.dismiss();
        }  // ignore

    }

    // NFC ends here

    @Override
    public void onButtonClicked() {
        disableForegroundMode();
        bottomSheetDialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        int LAUNCH_SECOND_ACTIVITY = 1;
        if (view.getId() == R.id.textViewChoose) {
            Intent intent2 = new Intent(this, ChooseImageActivity.class);
            startActivityForResult(intent2, LAUNCH_SECOND_ACTIVITY);
        }
    }
}
