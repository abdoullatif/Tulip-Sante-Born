package com.example.tulipsante.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.R;
import com.example.tulipsante.utils.DataParser;
import com.example.tulipsante.utils.PackageParser;
import com.example.tulipsante.utils.ParseRunnable;
import com.example.tulipsante.utils.PermissionUtils;
import com.example.tulipsante.utils.Protocol;
import com.example.tulipsante.utils.PulseParser;
import com.example.tulipsante.utils.SpoBean;
import com.example.tulipsante.utils.USBCommManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PulseOxymeterActivity extends AppCompatActivity
        implements PackageParser.OnDataChangeListener
{
    private String TAG = this.getClass().getSimpleName();

    private USBCommManager mUSBCommManager;
    private DataParser mDataParser;
    private PackageParser mPackageParser;

    private Protocol protocol = new Protocol();

    private PulseParser pulseParser;

    //Views
    private TextView tvSpO2;
    private TextView tvPulseRate;
    private LineChart lineChart1;
    private LineChart lineChart2;
    private CardView cardViewBack;

    private Thread thread;
    private boolean plotData;
    private Timer mRecordTimer;

    private void initViews() {
        tvSpO2 =  findViewById(R.id.tvSpO2);
        tvPulseRate =  findViewById(R.id.tvPulseRate);
        lineChart1 = findViewById(R.id.lineChart1);
        lineChart2 = findViewById(R.id.lineChart2);
        cardViewBack = findViewById(R.id.cardViewBack);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_pulse_oxymeter);

        initViews();

        setChart1();
        setChart2();
        onBackButtonPressed();
        
        this.pulseParser = new PulseParser();

//        init USB serial connection
        mUSBCommManager = USBCommManager.getUSBManager(this);

        mUSBCommManager.setListener(new USBCommManager.USBCommListener() {
            @Override
            public void onReceiveData(byte[] dat, int paramInt) {
                System.out.println("===== On Receive ======");
                System.out.println(Arrays.toString(dat));
                runOnUiThread(() -> {
                    tvSpO2.setText(""+Arrays.toString(dat));
//                    Toast.makeText(PulseOxymeterActivity.this, Arrays.toString(dat), Toast.LENGTH_SHORT).show();
                });
                List<SpoBean> data = pulseParser.decode(dat, paramInt);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(PulseOxymeterActivity.this, data.size(), Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            Toast.makeText(PulseOxymeterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                for (SpoBean spoBean : pulseParser.decode(dat, paramInt)) {
//                    runOnUiThread(() -> {
                    tvSpO2.setText(""+spoBean.isFingerOff());
                    Toast.makeText(PulseOxymeterActivity.this, spoBean.getSpO2(), Toast.LENGTH_SHORT).show();
//                    });
                }
//                mDataParser.add(dat);
            }

            @Override
            public void onUSBStateChanged(boolean isPlugged) {
                System.out.println("===== Is Plugged ======");
                tvPulseRate.setText(String.valueOf(isPlugged));
                System.out.println(isPlugged);
            }
        });
        mUSBCommManager.initConnection();

//        mDataParser = new DataParser(DataParser.Protocol.BCI, dat -> {
//            System.out.println("========= Begin =========");
//            Log.i(TAG, "onPackageReceived: " + Arrays.toString(dat));
//            if(mPackageParser == null) {
//                mPackageParser = new PackageParser(PulseOxymeterActivity.this);
//            }
//
//            mPackageParser.parse(dat);
//        });
//        mDataParser.start();

    }

    public void onBackButtonPressed() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mDataParser.stop();
    }

    private void setChart1() {
        lineChart1.getDescription().setEnabled(false);

        lineChart1.setTouchEnabled(true);

        lineChart1.setDragEnabled(true);
        lineChart1.setScaleEnabled(true);
        lineChart1.setDrawGridBackground(false);

        lineChart1.setPinchZoom(true);

        lineChart1.setBackgroundColor(getColor(R.color.colorPrimaryDark));

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        lineChart1.setData(data);

        Legend l = lineChart1.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis x1 = lineChart1.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(true);
        x1.setAvoidFirstLastClipping(true);
        x1.setEnabled(true);

        YAxis leftAxis = lineChart1.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = lineChart1.getAxisRight();
        rightAxis.setEnabled(false);

        lineChart1.getAxisLeft().setDrawGridLines(false);
        lineChart1.getXAxis().setDrawGridLines(false);
        lineChart1.setDrawBorders(false);

        feedMultiple();
    }

    private void setChart2() {
        lineChart2.getDescription().setEnabled(false);
        lineChart2.setTouchEnabled(true);
        lineChart2.setDragEnabled(true);
        lineChart2.setScaleEnabled(true);
        lineChart2.setDrawGridBackground(false);
        lineChart2.setPinchZoom(true);
        lineChart2.setBackgroundColor(getColor(R.color.colorPrimaryDark));

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        lineChart2.setData(data);

        Legend l = lineChart2.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis x1 = lineChart2.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(true);
        x1.setAvoidFirstLastClipping(true);
        x1.setEnabled(true);

        YAxis leftAxis = lineChart2.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = lineChart2.getAxisRight();
        rightAxis.setEnabled(false);

        lineChart2.getAxisLeft().setDrawGridLines(false);
        lineChart2.getXAxis().setDrawGridLines(false);
        lineChart2.setDrawBorders(false);
    }

    private void feedMultiple() {

        if(thread != null) {
            thread.interrupt();
        }

        thread = new Thread(() -> {
            while (true) {
                plotData = true;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void addEntrySpo2(int spo2) {
        LineData data = lineChart1.getData();
        if(data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if(set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(), spo2), 0);
            data.notifyDataChanged();

            lineChart1.notifyDataSetChanged();
            lineChart1.setVisibleXRange(0,300);
            lineChart1.setVisibleXRangeMaximum(300);
            lineChart1.moveViewToX(data.getEntryCount());
        }
    }

    private void addEntryPulse(int pu) {
        LineData data = lineChart2.getData();
        if(data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if(set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(), pu), 0);
            data.notifyDataChanged();
            lineChart2.notifyDataSetChanged();
            lineChart2.setVisibleXRange(0,300);
            lineChart2.setVisibleXRangeMaximum(300);
            lineChart2.moveViewToX(data.getEntryCount());
        }
    }



    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(2f);
        set.setColor(Color.GREEN);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    public void onClick(View v) {
//        mUSBCommManager = USBCommManager.getUSBManager(this);
//        mUSBCommManager.setListener(new USBCommManager.USBCommListener() {
//            @Override
//            public void onReceiveData(byte[] dat) {
//                System.out.println("===== On Receive ======");
//                System.out.println(dat);
//                System.out.println("=== Array of Bytes over here ===");
////                mPackageParser.parseData(dat);
//                mDataParser.add(dat);
//            }
//
//            @Override
//            public void onUSBStateChanged(boolean isPlugged) {
//                System.out.println("===== Is Plugged ======");
//                System.out.println(isPlugged);
//            }
//        });
//        mUSBCommManager.initConnection();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0) {
            onResume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUSBCommManager = USBCommManager.getUSBManager(this);
        mUSBCommManager.initConnection();
    }

    @Override
    public void onParamsChanged() {
//        runOnUiThread(() -> {
//            PackageParser.OxiParams params = mPackageParser.getOxiParams();
//
//            if(params.getSpo2() != params.SPO2_INVALID_VALUE) {
//                tvSpO2.setText(""+params.getSpo2());
//            }else {
//                tvSpO2.setText("- -");
//            }
//
//            if(params.getPulseRate() != params.PULSE_RATE_INVALID_VALUE) {
//                tvPulseRate.setText(""+params.getPulseRate());
//            }else {
//                tvPulseRate.setText("- -");
//            }
//        });
    }

    @Override
    public void onWaveChanged(int wave) {
        addEntrySpo2(wave);
    }
}

//2021-05-04 10:16:33.972 1914-1980/? I/System.out: [-1, -86, -109, 0, 99, 60, 62, -57, 0, -117, -64, 2, 0, -50, 109, 3, 0, 0, -56, 51]
