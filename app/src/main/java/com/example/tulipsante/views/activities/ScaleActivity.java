package com.example.tulipsante.views.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.UsbService;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Set;

public class ScaleActivity extends AppCompatActivity {



    private ImageView imageViewBackButton;
    private CardView cardViewSave;
    private HalfGauge halfGauge;


    //
    private UsbService usbService;
    private EditText display;
    private EditText editTextPoids;
    private MyHandler mHandler;






    private void initViews() {
        imageViewBackButton = findViewById(R.id.imageViewBackButton);
        cardViewSave = findViewById(R.id.cardViewSave);
        halfGauge = findViewById(R.id.halfGauge);
        //set min max and current value
        halfGauge.setMinValue(0.0);
        halfGauge.setMaxValue(150.0);
        halfGauge.setValueColor(Color.parseColor("#FFFFFF"));
    }

    /*
     * Notifications from UsbService will be received here.
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_scale);

        initViews();

        mHandler = new MyHandler(this);

        onSaveButtonClicked();


        navigateBack();

    }

    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<ScaleActivity> mActivity;

        public MyHandler(ScaleActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            String resultBalance = "";
            boolean verifie = true;
            String resultPrime = "";
            int cpt = 0;

            switch (msg.what) {

                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    int s = data.length();

                    //
                    if(!data.equals("")){
                        if(data.length() >= 3){
                            //
                            if (data.equals("0.0")) {
                                //mActivity.get().display.setText(data);
                                mActivity.get().halfGauge.setValueColor(Color.parseColor("#FFFFFF"));
                                mActivity.get().halfGauge.setValue(Double.parseDouble(data));
                                verifie = true;
                            }
                            //
                            if (data.length() == 3) {
                                resultPrime = data.substring(0, 1);
                            } else {
                                resultPrime = data.substring(0, 2);
                            }
                            //
                            if (resultPrime.equals(resultBalance)) {
                                cpt++;
                                System.out.println("compteur " + cpt);
                            } else {
                                resultBalance = resultPrime;
                                cpt = 0;
                                if (verifie) {
                                    //mActivity.get().display.setText(data);
                                    mActivity.get().halfGauge.setValue(Double.parseDouble(data));
                                }
                            }
                            //
                            if (cpt == 4) {
                                //mActivity.get().display.setText(data);
                                mActivity.get().halfGauge.setValue(Double.parseDouble(data));
                                System.out.println("POID VALIDER ");
                                verifie = false;
                                cpt = 0;
                            }
                            //

                        }
                    }

                    //mActivity.get().display.setText(data);


                    //Toast.makeText(mActivity.get(), "longueur " + s, Toast.LENGTH_SHORT).show();
                    break;

                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    //

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

    private void navigateBack() {
        imageViewBackButton.setOnClickListener(view -> finish());
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


}