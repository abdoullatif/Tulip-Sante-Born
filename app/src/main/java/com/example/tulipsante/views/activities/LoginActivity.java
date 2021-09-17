package com.example.tulipsante.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tulipsante.interfaces.DisableNfc;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.viewModel.LoginViewModel;
import com.example.tulipsante.views.dialogFragment.LoginSADialogFragment;
import com.example.tulipsante.views.dialogFragment.ScanNFCDialogFragment;


public class LoginActivity extends AppCompatActivity implements DisableNfc {
    private static final String TAG = "ee";
    private int counter = 0;

    private EditText editTextUsername, editTextPassword;
    private ImageView imageViewLogo;
    private AppCompatButton buttonLogin;
    private CardView cardViewTag;

    private ScanNFCDialogFragment bottomSheetDialog;

    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;

    // View Model
    private LoginViewModel loginViewModel;

    private String username;
    private String password;

    private void initViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        cardViewTag = findViewById(R.id.cardViewTag);
        imageViewLogo = findViewById(R.id.imageViewLogo);
    }

    private void initialisation() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getString("current_Theme",null) == null) {
            editor.putString("current_Theme","dark");
            editor.apply();
        }
        if(sharedPreferences.getString("Locale.Helper.Selected.Language",null) == null) {
            editor.putString("Locale.Helper.Selected.Language","en");
            editor.apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        initViews();
        initialisation();

        onButtonLoginPressed();
        onTagPressed();
        onImageViewLogoPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            counter = 0;
        }
    }

//    E/SQLiteLog: (5) statement aborts at 1: [PRAGMA journal_mode=TRUNCATE] database is locked

    private void onImageViewLogoPressed() {
        imageViewLogo.setOnClickListener(view -> {
            counter++;
            if(counter == 10) {
                LoginSADialogFragment bottomDialog = new LoginSADialogFragment(this,this);
                bottomDialog.show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
    }

    private void onTagPressed() {
        cardViewTag.setOnClickListener(view -> {
            bottomSheetDialog = new ScanNFCDialogFragment(LoginActivity.this);
            bottomSheetDialog.show(getSupportFragmentManager(),"ModalBottomSheet");
            enableForegroundMode();
        });
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
            boolean isEqual = loginViewModel.checkTagEqualsUid(GeneralPurposeFunctions.bytesToHexString(uidBytes));
            if(isEqual) {
                Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(intent1);
                finish();
            } else {
                Toast.makeText(this, "Tag not correct!", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        }
    }

    private void onButtonLoginPressed() {
        buttonLogin.setOnClickListener(view -> {
            username = editTextUsername.getText().toString().trim();
            password = editTextPassword.getText().toString().trim();
            if(!username.isEmpty() && !password.isEmpty()) {
                boolean isValid = loginViewModel.checkValidLogin(username, password);
                if (isValid) {
                    Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(this, "Username or Password incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Fill all the fields!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onButtonClicked() {
        disableForegroundMode();
        bottomSheetDialog.dismiss();
    }
}
