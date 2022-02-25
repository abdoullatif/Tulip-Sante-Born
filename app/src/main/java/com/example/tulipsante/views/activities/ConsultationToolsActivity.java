package com.example.tulipsante.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tulipsante.R;

import com.example.tulipsante.pulse.oximeter.view.PulseActivity;
import com.example.tulipsante.views.dialogFragment.EndoscopeModalDialogFragment;
import com.example.tulipsante.views.dialogFragment.PulseModalDialogFragment;
import com.example.tulipsante.views.dialogFragment.ThermalModalDialogFragment;
import com.example.tulipsante.views.dialogFragment.UltrasoundDialogFragment;
import com.example.tulipsante.views.dialogFragment.UltrasoundModalDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class ConsultationToolsActivity extends AppCompatActivity {
    private static final int LAUNCH_SECOND_ACTIVITY = 1;
    private static final int LAUNCH_THIRD_ACTIVITY = 1;
    private static final int DURATION = 3000;
    private boolean pulse = false;
    private boolean endo = false;
    private boolean ult = false;
    private boolean thermal = false;

    private ImageView imageViewBackButton,
            imageViewHuman1,
            imageViewUltra,
            imageViewEndo,
            imageViewTemp,
            imageViewPulse,
            imageViewHuman2;
    private CardView cardViewUltrasound,
            cardViewEndoscope,
            cardViewThermal,
            cardViewPulse,
            cardViewTempCaptured,
            cardViewSave,
            cardViewUltraImages,
            cardViewPulseCaptured
    ;
    private TextView textViewTemp,
            textViewUltra,
            textViewEndo,
            textViewTemperature,
            textViewPulse,
            textViewNumImg,
            textViewSpo2,
            textViewPi,
            textViewPr
    ;

    private SharedPreferences sharedPreferences;

    private boolean hasUltrasoundBeenClicked = false;

    private UltrasoundDialogFragment ultrasoundDialogFragment;


    private void initViews() {
        imageViewBackButton = findViewById(R.id.imageViewBackButton);
        cardViewUltrasound = findViewById(R.id.cardViewUltrasound);
        cardViewEndoscope = findViewById(R.id.cardViewEndoscope);
        cardViewThermal = findViewById(R.id.cardViewThermal);
        cardViewPulse = findViewById(R.id.cardViewPulse);
        cardViewSave = findViewById(R.id.cardViewSave);
        cardViewTempCaptured = findViewById(R.id.cardViewTempCaptured);
        imageViewHuman1 = findViewById(R.id.imageViewHuman);
        //imageViewHuman2 = findViewById(R.id.imageViewHuman2);
        textViewTemp = findViewById(R.id.textViewTemp);
        cardViewUltraImages = findViewById(R.id.cardViewUltraImages);
        cardViewPulseCaptured = findViewById(R.id.cardViewPulseCaptured);

        imageViewUltra = findViewById(R.id.imageViewUltra);
        imageViewEndo = findViewById(R.id.imageViewEndo);
        imageViewTemp = findViewById(R.id.imageViewTemp);
        imageViewPulse = findViewById(R.id.imageViewPulse);
        textViewUltra = findViewById(R.id.textViewUltra);
        textViewEndo = findViewById(R.id.textViewEndo);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewPulse = findViewById(R.id.textViewPulse);
        textViewNumImg = findViewById(R.id.textViewNumImg);
        textViewSpo2 = findViewById(R.id.textViewSpo2);
        textViewPi = findViewById(R.id.textViewPi);
        textViewPr = findViewById(R.id.textViewPr);
    }

    private void initialisation() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ultrasoundDialogFragment = new UltrasoundDialogFragment(this);
        if(Objects.requireNonNull(getIntent().getStringExtra("From")).equals("Vitals")) {
            cardViewUltrasound.setCardBackgroundColor(getColor(R.color.grey));
            imageViewUltra.setColorFilter(getColor(R.color.darkgrey));
            textViewUltra.setTextColor(getColor(R.color.darkgrey));
            cardViewEndoscope.setCardBackgroundColor(getColor(R.color.grey));
            imageViewEndo.setColorFilter(getColor(R.color.darkgrey));
            textViewEndo.setTextColor(getColor(R.color.darkgrey));
            openThermal();
            openPulse();
        }
        else if(Objects.requireNonNull(getIntent().getStringExtra("From")).equals("Investigation")) {
            cardViewThermal.setCardBackgroundColor(getColor(R.color.grey));
            imageViewTemp.setColorFilter(getColor(R.color.darkgrey));
            textViewTemperature.setTextColor(getColor(R.color.darkgrey));
            cardViewPulse.setCardBackgroundColor(getColor(R.color.grey));
            imageViewPulse.setColorFilter(getColor(R.color.darkgrey));
            textViewPulse.setTextColor(getColor(R.color.darkgrey));
            openUltrasound();
            openEndoscope();
        }
        else {
            cardViewSave.setVisibility(View.INVISIBLE);
            openThermal();
            openPulse();
            openUltrasound();
            openEndoscope();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_consultation_tools);

        initViews();
        initialisation();

        navigateBack();
        //onHumanPressed();
        onSaveButtonClicked();

        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, filter);

    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    private void setApplicationTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentTheme = sharedPreferences.getString("current_Theme",null);
        if(Objects.requireNonNull(currentTheme).equals("dark")) {
            setTheme(R.style.AppTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
    }

    private void onHumanPressed() {
        imageViewHuman1.setOnClickListener(view -> {
            imageViewHuman2.setVisibility(View.VISIBLE);
            imageViewHuman1.setVisibility(View.INVISIBLE);
        });
        imageViewHuman2.setOnClickListener(view -> {
            imageViewHuman1.setVisibility(View.VISIBLE);
            imageViewHuman2.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("+++++ On resume ++++++");
        if(hasUltrasoundBeenClicked) {
            ultrasoundDialogFragment.show(getSupportFragmentManager(), "ModalBottomSheet");
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            ArrayList<String> listImages = ultrasoundDialogFragment.listImages;
            if(listImages != null && listImages.size() > 0) {
                Set<String> data = new ArraySet<>();
                String num = listImages.size() + " image(s)";
                data.addAll(listImages);
                editor.putStringSet("ult", data);
                editor.apply();
                cardViewUltraImages.setVisibility(View.VISIBLE);
                textViewNumImg.setText(num);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                if(sharedPreferences.getString("temp", null) != null) {
                    cardViewTempCaptured.setVisibility(View.VISIBLE);
                    textViewTemp.setText(sharedPreferences.getString("temp",null));
                }
                else {
                    cardViewPulseCaptured.setVisibility(View.VISIBLE);
                    textViewSpo2.setText(sharedPreferences.getString("spo2",null));
                    textViewPi.setText(sharedPreferences.getString("pi",null));
                    textViewPr.setText(sharedPreferences.getString("pr",null));
                }
            }
        }
    }

    private void openPulse() {
        cardViewPulse.setOnClickListener(view -> {
//            if (pulse) {
//                hasUltrasoundBeenClicked = false;
                Intent intent = new Intent(ConsultationToolsActivity.this, PulseActivity.class);
                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
//            }
//            else {
//                Toast.makeText(this, "Plug Pulse!", Toast.LENGTH_SHORT).show();
//            }
        });
    }

    private void openThermal() {
        cardViewThermal.setOnClickListener(view -> {
//            if (thermal) {
//                hasUltrasoundBeenClicked = false;
                Intent intent = new Intent(ConsultationToolsActivity.this, ThermalCamActivity.class);
                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
//            }
//            else {
//                Toast.makeText(this, "Plug Thermal Camera!", Toast.LENGTH_SHORT).show();
//            }
        });
    }

    private void openEndoscope() {
        cardViewEndoscope.setOnClickListener(view -> {
//            if (endo) {
//                hasUltrasoundBeenClicked = false;
                Intent intent = new Intent(ConsultationToolsActivity.this, EndoscopeActivity.class);
                startActivity(intent);
//            }
//            else {
//                Toast.makeText(this, "Plug Endoscope!", Toast.LENGTH_SHORT).show();
//            }
        });
    }

    private void openUltrasound() {
        cardViewUltrasound.setOnClickListener(view -> {
//            if(ult) {
                emptyDirectory();
                hasUltrasoundBeenClicked = true;
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.konted.wirelesskus3");
                startActivity(intent);
//            }
//            else {
//                Toast.makeText(this, "Plug Ultrasound!", Toast.LENGTH_SHORT).show();
//            }
        });
    }

    private void emptyDirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "WirelessKUS");
        String[] children = dir.list();
        if (children != null) {
            for (String child : children) {
                new File(dir, child).delete();
            }
        }
    }

    private void navigateBack() {
        imageViewBackButton.setOnClickListener(view -> showAlertDialog());
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to leave?");

        builder.setPositiveButton("Yes/Leave", (dialogInterface, i) -> {
            sharedPreferences.edit().remove("temp").apply();
            setResult(0);
            finish();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onSaveButtonClicked() {
        cardViewSave.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm");
            builder.setMessage("Do you want to save data?");
            builder.setPositiveButton("Yes/Continue", (dialogInterface, i) -> {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("temp ","10");
                setResult(RESULT_OK,returnIntent);
                finish();
            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("===== On Pause =====");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("====== On Stop ========");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("===== On destroy =======");
    }

    private static final String TAG = ConsultationToolsActivity.class.getSimpleName();

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbManager m = (UsbManager)getApplicationContext().getSystemService(USB_SERVICE);
                assert m != null;
                HashMap<String, UsbDevice> usbDevices = m.getDeviceList();
                Collection<UsbDevice> ite = usbDevices.values();
                UsbDevice[] usbs = ite.toArray(new UsbDevice[]{});
                for (UsbDevice usb : usbs) {
                    switch (Objects.requireNonNull(usb.getProductName())) {
                        case "MobIR Air": {
                            thermal = true;
                            ult     = false;
                            endo    = false;
                            pulse   = false;
                            try {
                                ThermalModalDialogFragment fragment = new ThermalModalDialogFragment();
                                fragment.show(getSupportFragmentManager(), "ModalBottomSheet");
                                new Handler().postDelayed(fragment::dismiss, DURATION);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                        case "Ultrasound Device": {
                            thermal = false;
                            ult     = true;
                            endo    = false;
                            pulse   = false;
                            try {
                                UltrasoundModalDialogFragment fragment = new UltrasoundModalDialogFragment();
                                fragment.show(getSupportFragmentManager(), "ModalBottomSheet");
                                new Handler().postDelayed(fragment::dismiss, DURATION);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        case "Integrated Camera": {
                            thermal = false;
                            ult     = false;
                            endo    = true;
                            pulse   = false;
                            try{
                                EndoscopeModalDialogFragment fragment = new EndoscopeModalDialogFragment();
                                fragment.show(getSupportFragmentManager(), "ModalBottomSheet");
                                new Handler().postDelayed(fragment::dismiss, DURATION);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                        }
                        case "USBUART": {
                            thermal = false;
                            ult     = false;
                            endo    = false;
                            pulse   = true;
                            try {
                                PulseModalDialogFragment fragment = new PulseModalDialogFragment();
                                fragment.show(getSupportFragmentManager(), "ModalBottomSheet");
                                new Handler().postDelayed(fragment::dismiss, DURATION);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        default:
                            // show nothing
                            break;
                    }
//                    Toast.makeText(context, "Connected usb devices are "+ usb.getProductName(), Toast.LENGTH_SHORT).show();
                    Log.d("Connected usb devices","Connected usb devices are "+ usb.getDeviceName());
                }
            }
        }
    };


}
