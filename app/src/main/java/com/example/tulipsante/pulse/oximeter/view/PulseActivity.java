package com.example.tulipsante.pulse.oximeter.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.tulipsante.R;
import com.example.tulipsante.pulse.oximeter.Config;
import com.example.tulipsante.pulse.oximeter.DataCenter;
import com.example.tulipsante.pulse.oximeter.SoundMgr;
import com.example.tulipsante.pulse.oximeter.bean.EGain;
import com.example.tulipsante.pulse.oximeter.bean.ESpeed;
import com.example.tulipsante.pulse.oximeter.bean.EWorMode;
import com.example.tulipsante.pulse.oximeter.device.Protocol;
import com.example.tulipsante.pulse.oximeter.device.SpoBean;
import com.example.tulipsante.pulse.oximeter.device.SpoStats;
import com.example.tulipsante.pulse.oximeter.device.bluetooth.BleClient;
import com.example.tulipsante.pulse.oximeter.device.bluetooth.IBleClient;
import com.example.tulipsante.pulse.oximeter.device.usb2serial.UsbMgr;
import com.example.tulipsante.pulse.oximeter.repository.DbMgr;
import com.example.tulipsante.pulse.oximeter.repository.bean.SpoRecord;
import com.example.tulipsante.pulse.oximeter.view.frg.arcView;
import com.example.tulipsante.pulse.oximeter.view.frg.arcView2;
import com.example.tulipsante.pulse.oximeter.view.frg.rectangle;
import com.example.tulipsante.pulse.oximeter.view.view.SpoView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.umeng.cconfig.UMRemoteConfig;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.O)
public class PulseActivity extends DemoBase implements
        OnChartValueSelectedListener {
    private static final String TAG = "measure";

    private CardView cardViewBack;

    /*new added*/

    private ImageView mHeart;

    private SpoView mSpoView;

    private UsbMgr mUsbMgr;
    private DataCenter dataCenter;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private AlphaAnimation mAlphaAnim;
    private ESpeed mSpeed;
    private EGain mGain;
    private SpoStats mSpoStats;
    private DbMgr mDbMgr;
    private IBleClient mBleClient;
    private EWorMode worMode;

    private arcView arcview;
    private TextView spo2Val, bmpVal;
    private Button cancel;
    private arcView2 arcview2;
    private LinearLayout child;
    private  LinearLayout woman;
    private  LinearLayout man;
    private CheckBox sex;
    private TextView pi;


    private rectangle age1,age2,age3,age4,age5,age6,age7,age8,age9,age10,age11,age12,age13,age14,age15,age16;
    private RadioGroup ageChoose;
    private Protocol protocol = new Protocol();
    private boolean isPlay;


    boolean running = false;
    boolean reopen = false;

    private long lastTick = 0;
    private long chartime = 0;

    private LineChart chart;
    int go = 1;
   // private SeekBar seekBarX, seekBarY;
   // private TextView tvX, tvY;


    // end new added

    private void initViews() {
        cardViewBack = findViewById(R.id.cardViewBack);
        dataCenter = DataCenter.getInstance(this);
        cancel = findViewById(R.id.cancelPulse);
        mHeart = findViewById(R.id.img_heart2);

        spo2Val = findViewById(R.id.spo2Val);
        bmpVal = findViewById(R.id.bmpVal);

        mSpoView = findViewById(R.id.spoview);

        age1 = findViewById(R.id.age1);
        //test
        arcview = findViewById(R.id.gauge1);

        arcview2 = findViewById(R.id.gauge2);
        pi = findViewById(R.id.pi);

        //test
        //indicate age
        age1 = findViewById(R.id.age1);
        age2 = findViewById(R.id.age2);
        age3 = findViewById(R.id.age3);
        age4 = findViewById(R.id.age4);

        age5 = findViewById(R.id.age5);
        age6 = findViewById(R.id.age6);
        age7 = findViewById(R.id.age7);
        age8 = findViewById(R.id.age8);
        age9 = findViewById(R.id.age9);
        age10 = findViewById(R.id.age10);

        age11 = findViewById(R.id.age11);
        age12=  findViewById(R.id.age12);
        age13=  findViewById(R.id.age13);
        age14 = findViewById(R.id.age14);
        age15 = findViewById(R.id.age15);
        age16 = findViewById(R.id.age16);

        ageChoose = findViewById(R.id.ageChosse);
        child  = findViewById(R.id.child);
        woman = findViewById(R.id.woman);
        man = findViewById(R.id.man);
        chart = findViewById(R.id.chart1);

      //  tvX = findViewById(R.id.tvXMax);
       // tvY = findViewById(R.id.tvYMax);


        //seekBarX = findViewById(R.id.seekBar1);


    }

    private void initialisation() {
        mUsbMgr = UsbMgr.getInstance(this);
        mUsbMgr.setDataListener((data, len) -> {
            receiveData(data, len);
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_pulse_act);

        initViews();
        initialisation();

        onRadioButtonPressed();
        onBackButtonPressed();


     //   mUsbMgr.resumeUsbPermission();
       // arcview2.setPointSize();
     //   starting();
     //  startDevice();


        //Toast.makeText(this,String.valueOf(Thread.activeCount()), Toast.LENGTH_SHORT).show();;

       // if(Thread.activeCount()<=19)
        starting.start();




        //chart lie


      /*  tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);


        seekBarX = findViewById(R.id.seekBar1);
        seekBarX.setOnSeekBarChangeListener(this);

        seekBarY = findViewById(R.id.seekBar2);
        seekBarY.setMax(180);
        seekBarY.setOnSeekBarChangeListener(this);

       */



        //end
        chart.setBackgroundColor(Color.parseColor("#283F5F"));

    }

    void chartStatement(ArrayList<Entry> values){


        {   // // Chart Style // //


            // background color
            chart.setBackgroundColor(Color.parseColor("#283F5F"));

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // create marker to display box when values are selected
            MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

            // Set the marker to the chart
            mv.setChartView(chart);
            chart.setMarker(mv);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);

        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);

           // xAxis.setAxisMaximum(100);

        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum(250f);
            yAxis.setAxisMinimum(30f);
        }


        {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(9f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);
            llXAxis.setTypeface(tfRegular);

            LimitLine ll1 = new LimitLine(230f, " BMP UPPER Limit");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
            ll1.setTypeface(tfRegular);

            LimitLine ll2 = new LimitLine(50f, "BMP Lower Limit");
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);
            ll2.setTypeface(tfRegular);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);
        }

        // add dataa

        //seekBarX.setProgress(45);
        //seekBarY.setProgress(180);
        setData(values);

        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);



    }



    private void onRadioButtonPressed() {

        ageChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                if( radioButton.getText().toString().compareTo("kid")==0){
                    woman.setVisibility(View.INVISIBLE);
                    man.setVisibility(View.INVISIBLE);
                    child.setVisibility(View.VISIBLE);
                    age1.setSweepAngle(50);
                    age2.setSweepAngle(0);
                    age3.setSweepAngle(0);
                    age4.setSweepAngle(0);
                    age5.setSweepAngle(0);
                    arcview2.setPointSize(0);


                }else if(radioButton.getText().toString().compareTo("woman")==0){
                    child.setVisibility(View.INVISIBLE);
                    man.setVisibility(View.INVISIBLE);
                    woman.setVisibility(View.VISIBLE);
                    age5.setSweepAngle(50);
                    age6.setSweepAngle(0);
                    age7.setSweepAngle(0);
                    age8.setSweepAngle(0);
                    age9.setSweepAngle(0);
                    age10.setSweepAngle(0);
                    arcview2.setPointSize(18);


                }else if(radioButton.getText().toString().compareTo("man")==0){
                    child.setVisibility(View.INVISIBLE);
                    woman.setVisibility(View.INVISIBLE);
                    man.setVisibility(View.VISIBLE);
                    age11.setSweepAngle(50);
                    age12.setSweepAngle(0);
                    age13.setSweepAngle(0);
                    age14.setSweepAngle(0);
                    age15.setSweepAngle(0);
                    age16.setSweepAngle(0);
                    arcview2.setPointSize(118);

                }
            }
        });

        age1.setOnClickListener(view -> {
            age1.setSweepAngle(50);
            age2.setSweepAngle(0);
            age3.setSweepAngle(0);
            age4.setSweepAngle(0);
            age5.setSweepAngle(0);
            arcview2.setPointSize(0);



        });

        age2.setOnClickListener(view -> {
            age1.setSweepAngle(0);
            age2.setSweepAngle(50);
            age3.setSweepAngle(0);
            age4.setSweepAngle(0);
            arcview2.setPointSize(2);


        });
        age3.setOnClickListener(view -> {
            age1.setSweepAngle(0);
            age2.setSweepAngle(0);
            age3.setSweepAngle(50);
            age4.setSweepAngle(0);
            arcview2.setPointSize(10);


        });
        age4.setOnClickListener(view -> {
            age1.setSweepAngle(0);
            age2.setSweepAngle(0);
            age3.setSweepAngle(0);
            age4.setSweepAngle(50);
            arcview2.setPointSize(17);


        });
        age5.setOnClickListener(view -> {
            age5.setSweepAngle(50);
            age6.setSweepAngle(0);
            age7.setSweepAngle(0);
            age8.setSweepAngle(0);
            age9.setSweepAngle(0);
            age10.setSweepAngle(0);
            arcview2.setPointSize(18);

        });
        age6.setOnClickListener(view -> {
            age5.setSweepAngle(0);
            age6.setSweepAngle(50);
            age7.setSweepAngle(0);
            age8.setSweepAngle(0);
            age9.setSweepAngle(0);
            age10.setSweepAngle(0);
            arcview2.setPointSize(26);

        });
        age7.setOnClickListener(view -> {
            age5.setSweepAngle(0);
            age6.setSweepAngle(0);
            age7.setSweepAngle(50);
            age8.setSweepAngle(0);
            age9.setSweepAngle(0);
            age10.setSweepAngle(0);
            arcview2.setPointSize(36);

        });
        age8.setOnClickListener(view -> {
            age5.setSweepAngle(0);
            age6.setSweepAngle(0);
            age7.setSweepAngle(0);
            age8.setSweepAngle(50);
            age9.setSweepAngle(0);
            age10.setSweepAngle(0);
            arcview2.setPointSize(46);

        });
        age9.setOnClickListener(view -> {
            age5.setSweepAngle(0);
            age6.setSweepAngle(0);
            age7.setSweepAngle(0);
            age8.setSweepAngle(0);
            age9.setSweepAngle(50);
            age10.setSweepAngle(0);
            arcview2.setPointSize(56);

        });
        age10.setOnClickListener(view -> {
            age5.setSweepAngle(0);
            age6.setSweepAngle(0);
            age7.setSweepAngle(0);
            age8.setSweepAngle(0);
            age9.setSweepAngle(0);
            age10.setSweepAngle(50);
            arcview2.setPointSize(66);

        });
        age11.setOnClickListener(view -> {
            age11.setSweepAngle(50);
            age12.setSweepAngle(0);
            age13.setSweepAngle(0);
            age14.setSweepAngle(0);
            age15.setSweepAngle(0);
            age16.setSweepAngle(0);
            arcview2.setPointSize(118);

        });
        age12.setOnClickListener(view -> {
            age11.setSweepAngle(0);
            age12.setSweepAngle(50);
            age13.setSweepAngle(0);
            age14.setSweepAngle(0);
            age15.setSweepAngle(0);
            age16.setSweepAngle(0);
            arcview2.setPointSize(126);

        });
        age13.setOnClickListener(view -> {
            age11.setSweepAngle(0);
            age12.setSweepAngle(0);
            age13.setSweepAngle(50);
            age14.setSweepAngle(0);
            age15.setSweepAngle(0);
            age16.setSweepAngle(0);
            arcview2.setPointSize(136);

        });
        age14.setOnClickListener(view -> {
            age11.setSweepAngle(0);
            age12.setSweepAngle(0);
            age13.setSweepAngle(0);
            age14.setSweepAngle(50);
            age15.setSweepAngle(0);
            age16.setSweepAngle(0);
            arcview2.setPointSize(146);

        });
        age15.setOnClickListener(view -> {
            age11.setSweepAngle(0);
            age12.setSweepAngle(0);
            age13.setSweepAngle(0);
            age14.setSweepAngle(0);
            age15.setSweepAngle(50);
            age16.setSweepAngle(0);
            arcview2.setPointSize(156);

        });
        age16.setOnClickListener(view -> {
            age11.setSweepAngle(0);
            age12.setSweepAngle(0);
            age13.setSweepAngle(0);
            age14.setSweepAngle(0);
            age15.setSweepAngle(0);
            age16.setSweepAngle(50);
            arcview2.setPointSize(166);

        });
        //END AGE

        mAlphaAnim = new AlphaAnimation(1, 0);
        mAlphaAnim.setDuration(200);
        mAlphaAnim.setRepeatCount(0);
        mAlphaAnim.setFillBefore(true);

        clearSpoValue();
        isPlay = false;
        mSpeed = dataCenter.getSpeed();
        mGain = dataCenter.getGain();
        mSpoStats = new SpoStats();
        mDbMgr = DbMgr.getInstance(this);
        mBleClient = BleClient.getSingleton(this);


//        TestWaveThread thread = new TestWaveThread();
//        thread.start();


        //startDevice();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        starting.interrupt();
        mUsbMgr.closeDevice();
        mBleClient.close();
        mSpoView.stopDraw();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        reopen =true;
        stopDevice();

        starting.interrupt();
        finish();

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

    public void onBackButtonPressed() {
        cardViewBack.setOnClickListener(view -> {
            reopen =true;
            stopDevice();

            starting.interrupt();
            finish();
        });
    }

    //for arc 1
    private  void arcmove(int a,int b) {

        new Thread() {
            public void run() {
                int i;
                if(a>=70 && b<=100) {
                    for (i = a; i <= b; i++) {

                        try {
                            int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arcview.setSweepAngle(finalI);

                                }
                            });
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }.start();

    };

    private  void arcmoveBehind(int a,int b) {

        new Thread() {
            public void run() {
                int i;
                if(a>=70 && b<=100) {
                    for (i = a; i >= b; i--) {

                        try {
                            int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arcview.setSweepAngle(finalI);

                                }
                            });
                            Thread.sleep(50);
                            if (i == 70)
                                break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }.start();

    };
    //for arc2

    private  void arc2move(int a,int b) {

        new Thread() {
            public void run() {
                int i;
                if(a>=50 && b<=230) {
                    for (i = a; i <= b; i++) {

                        try {
                            int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arcview2.setSweepAngle(finalI);

                                }
                            });
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }.start();

    };



    private  void arc2moveBehind(int a,int b) {

        new Thread() {
            public void run() {
                int i;
                if(a>=50 && b<=230) {
                    for (i = a; i >= b; i--) {


                        try {
                            int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arcview2.setSweepAngle(finalI);

                                }
                            });
                            Thread.sleep(50);
                            if (i == 50)
                                break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }.start();

    };







    Thread starting =   new Thread() {
            public void run() {
                int i=0;



                while (!reopen) {



                    try {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                commence();

                            }
                        });
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }




                }

            }
        };






    public void commence() {

        if(!mUsbMgr.checkPermission().isConnected()){
            mUsbMgr.resumeUsbPermission();
            mSpoView.stopDraw();
            clearSpoValue();


            if(running ){
               // Toast.makeText(this, "AGAIN", Toast.LENGTH_SHORT).show();
                isPlay = true;
                running = false;
            }
        }


        String canUser = UMRemoteConfig.getInstance().getConfigValue("canUse");
        Log.e(TAG, "canUse: " + canUser);
        if ("false".equals(canUser)) {
            Toast.makeText(this, "SOORY！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!running ) {
            if (isPlay) {
                stopDevice();
            } else {
                startDevice();
            }
        }

    }



    public void startDevice() {


        worMode = dataCenter.getWorkMode();


            if (mUsbMgr.openDevice(dataCenter.getBaudRate())) {

                isPlay = true;

            }



        if (isPlay) {
          //  Toast.makeText(this, "is play is true", Toast.LENGTH_SHORT).show();
          //  mSpoView = findViewById(R.id.spoview);
            mSpeed = dataCenter.getSpeed();
            mGain = dataCenter.getGain();

            mSpoView.setFreq(Config.cFreq)
                    .setSpeed(mSpeed)
                    .setGain(mGain)
                    .startDraw();
            mSpoStats.startStats();
            Toast.makeText(this, "analysing", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopDevice() {
        if (worMode == EWorMode.usb) {
            mUsbMgr.closeDevice();
        } else {
            mBleClient.setBleRecvCallback(null);
        }


        mSpoView.stopDraw();
        clearSpoValue();
        SpoRecord record = mSpoStats.stopStats();
        if (record != null) {
            mDbMgr.insertSpoRecord(record);
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Config.RECEIVE_NEW_SPO_STATS));
         //   Toast.makeText(this, this.getString(R.string.toast_measure_success), Toast.LENGTH_SHORT).show();
        }
        isPlay = false;
        arcmoveBehind(arcview.getSweepAngle(),70);
        arc2moveBehind(arcview2.getSweepAngle(),50);
    }

    public void receiveData(byte[] data, int len) {
        List<SpoBean> list = protocol.decode(data, len);
        for (SpoBean bean : list) {
            mSpoView.addPoint(bean.getPlethWave());
            mSpoStats.setSpo(bean);
            setSpoValue(bean);
        }
    }

    public void clearSpoValue() {

        arcmoveBehind(arcview.getSweepAngle(),70);
        spo2Val.setText(String.valueOf("70"));
        bmpVal.setText(String.valueOf("50"));

        arc2moveBehind(arcview2.getSweepAngle(),50);

        pi.setText("...");

    }
    ArrayList<Entry> values = new ArrayList<>();



    public void setSpoValue(SpoBean bean) {

        if (bean.isHeartFlag()) {
            mHandler.post(() -> {
                if (dataCenter.isPulseSound()) {
                    SoundMgr.getSoundManager().playHeartBeat();
                }
                mHeart.startAnimation(mAlphaAnim);
            });
        }

        long curTick = System.currentTimeMillis();
        if (curTick - lastTick > 500) {
            mHandler.post(() -> {
                if (bean.isProbeOff() || bean.isFingerOff()) {
                    mSpoView.setFingerOff(true);
                    clearSpoValue();

                    values = new ArrayList<Entry>();
                    go=0;

                } else {
                    mSpoView.setFingerOff(false);
                    int spo2 = bean.getSpO2();

                    if (spo2 == 127) {

                        arcmoveBehind(arcview.getSweepAngle(),70);
                        spo2Val.setText("70");

                    } else {
                        if(spo2>arcview.getSweepAngle())
                            arcmove(arcview.getSweepAngle(),spo2);
                        else
                            arcmoveBehind(arcview.getSweepAngle(),spo2);



                        spo2Val.setText(String.valueOf(spo2));
                        starting.interrupt();
                        running = true;
                    }

                    int pr = bean.getPR();
                    if (pr == 255) {

                        arc2moveBehind(arcview2.getSweepAngle(),50);
                        bmpVal.setText(String.valueOf(50));
                        values = new ArrayList<Entry>();
                        go=0;
                    } else {
                        if(pr>arcview2.getSweepAngle())
                            arc2move(arcview2.getSweepAngle(),pr);
                        else
                            arc2moveBehind(arcview2.getSweepAngle(),pr);
                        bmpVal.setText(String.valueOf(pr));

                        if (curTick - chartime > 5000) {
                            go+=5;

                            values.add(new Entry(go, pr, getResources().getDrawable(R.drawable.star)));

                            chartime= curTick;
                            setData(values);


                            // draw points over time
                            chart.animateX(10);

                            // get the legend (only possible after setting data)
                            Legend l = chart.getLegend();

                            // draw legend entries as lines
                            l.setForm(Legend.LegendForm.LINE);
                        }

                    }

                    pi.setText(String.valueOf(bean.getStrength())+"%");

                }
            });
            lastTick = curTick;
        }
    }


    //chart function


    private void setData(ArrayList<Entry> values) {



        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "time finger analysing (second)");

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }





    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "LineChartActivity1");
    }



    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOW HIGH", "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX());
        Log.i("MIN MAX", "xMin: " + chart.getXChartMin() + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin() + ", yMax: " + chart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }



    //en chart function



}

