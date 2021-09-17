package com.example.tulipsante.pulse.oximeter.view.frg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;



import com.example.tulipsante.R;

import com.example.tulipsante.pulse.oximeter.BaseFrg;
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
import com.example.tulipsante.pulse.oximeter.device.bluetooth.bean.EBleState;
import com.example.tulipsante.pulse.oximeter.device.usb2serial.UsbMgr;
import com.example.tulipsante.pulse.oximeter.repository.DbMgr;
import com.example.tulipsante.pulse.oximeter.repository.bean.SpoRecord;
import com.example.tulipsante.pulse.oximeter.view.Pulse;
import com.example.tulipsante.pulse.oximeter.view.view.SpoView;


import com.example.tulipsante.views.activities.ConsultationToolsActivity;
import com.umeng.cconfig.UMRemoteConfig;

import java.util.List;
import java.util.Locale;


import pl.pawelkleczkowski.customgauge.CustomGauge;

import static com.serenegiant.utils.UIThreadHelper.runOnUiThread;

public class MeasureFragment extends BaseFrg  {
    private static final String TAG = "measure";

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
    private   arcView arcview;
    private  TextView spo2Val, bmpVal;
    private Button cancel;
    private   arcView2 arcview2;
    private  LinearLayout child;
    private  LinearLayout woman;
    private  LinearLayout man;
    private CheckBox sex;
    private TextView pi;
    private rectangle age1,age2,age3,age4,age5,age6,age7,age8,age9,age10,age11,age12,age13,age14,age15,age16;
    private RadioGroup ageChoose;
    private Protocol protocol = new Protocol();
    private boolean isPlay;
    private long lastTick = 0;




    @Override
    protected View getLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_measure, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUsbMgr.closeDevice();
        mBleClient.close();
        mSpoView.stopDraw();
        unRegisterBroadcast();
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






    @Override
    protected void initView(View container) {
        mUsbMgr = UsbMgr.getInstance(getContext());
        mUsbMgr.setDataListener((data, len) -> {
            receiveData(data, len);
        });
       // mCircleView =  container.findViewById(R.id.circleView);
      //  mCircleView.setValueAnimated(50, 1500);
        dataCenter = DataCenter.getInstance(getContext());
        cancel = container.findViewById(R.id.cancelPulse);
        mHeart = container.findViewById(R.id.img_heart2);

        spo2Val = container.findViewById(R.id.spo2Val);
        bmpVal = container.findViewById(R.id.bmpVal);

        mSpoView = container.findViewById(R.id.spoview);

        age1 = container.findViewById(R.id.age1);
       //test
       arcview = container.findViewById(R.id.gauge1);

        arcview2 = container.findViewById(R.id.gauge2);
        pi = container.findViewById(R.id.pi);

        //test
        //indicate age
        age1 = container.findViewById(R.id.age1);
        age2 = container.findViewById(R.id.age2);
        age3 = container.findViewById(R.id.age3);
        age4 = container.findViewById(R.id.age4);

        age5 = container.findViewById(R.id.age5);
        age6 = container.findViewById(R.id.age6);
        age7 = container.findViewById(R.id.age7);
        age8 = container.findViewById(R.id.age8);
        age9 = container.findViewById(R.id.age9);
        age10 = container.findViewById(R.id.age10);

        age11 = container.findViewById(R.id.age11);
        age12= container.findViewById(R.id.age12);
        age13= container.findViewById(R.id.age13);
        age14 = container.findViewById(R.id.age14);
        age15 = container.findViewById(R.id.age15);
        age16 = container.findViewById(R.id.age16);

        ageChoose = container.findViewById(R.id.ageChosse);
        child  = container.findViewById(R.id.child);
        woman = container.findViewById(R.id.woman);
        man = container.findViewById(R.id.man);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ConsultationToolsActivity.class);
                startActivity(intent);
            }
        });
       
        ageChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
           RadioButton  radioButton = (RadioButton) container.findViewById(selectedId);
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
                        starting();

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
                        starting();
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
                        starting();
                    }
            }
        });

        age1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age1.setSweepAngle(50);
                age2.setSweepAngle(0);
                age3.setSweepAngle(0);
                age4.setSweepAngle(0);
                age5.setSweepAngle(0);
                arcview2.setPointSize(0);
                starting();

            }
        });

        age2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age1.setSweepAngle(0);
                age2.setSweepAngle(50);
                age3.setSweepAngle(0);
                age4.setSweepAngle(0);
                arcview2.setPointSize(1);
                starting();

            }
        });
        age3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age1.setSweepAngle(0);
                age2.setSweepAngle(0);
                age3.setSweepAngle(50);
                age4.setSweepAngle(0);
                arcview2.setPointSize(2);
                starting();

            }
        });
        age4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age1.setSweepAngle(0);
                age2.setSweepAngle(0);
                age3.setSweepAngle(0);
                age4.setSweepAngle(50);
                arcview2.setPointSize(10);
                starting();

            }
        });





        age5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age5.setSweepAngle(50);
                age6.setSweepAngle(0);
                age7.setSweepAngle(0);
                age8.setSweepAngle(0);
                age9.setSweepAngle(0);
                age10.setSweepAngle(0);
                arcview2.setPointSize(18);
                starting();
            }
        });
        age6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age5.setSweepAngle(0);
                age6.setSweepAngle(50);
                age7.setSweepAngle(0);
                age8.setSweepAngle(0);
                age9.setSweepAngle(0);
                age10.setSweepAngle(0);
                arcview2.setPointSize(26);
                starting();
            }
        });
        age7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age5.setSweepAngle(0);
                age6.setSweepAngle(0);
                age7.setSweepAngle(50);
                age8.setSweepAngle(0);
                age9.setSweepAngle(0);
                age10.setSweepAngle(0);
                arcview2.setPointSize(36);
                starting();
            }
        });
        age8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age5.setSweepAngle(0);
                age6.setSweepAngle(0);
                age7.setSweepAngle(0);
                age8.setSweepAngle(50);
                age9.setSweepAngle(0);
                age10.setSweepAngle(0);
                arcview2.setPointSize(46);
                starting();
            }
        });
        age9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age5.setSweepAngle(0);
                age6.setSweepAngle(0);
                age7.setSweepAngle(0);
                age8.setSweepAngle(0);
                age9.setSweepAngle(50);
                age10.setSweepAngle(0);
                arcview2.setPointSize(56);
                starting();
            }
        });
        age10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age5.setSweepAngle(0);
                age6.setSweepAngle(0);
                age7.setSweepAngle(0);
                age8.setSweepAngle(0);
                age9.setSweepAngle(0);
                age10.setSweepAngle(50);
                arcview2.setPointSize(66);
                starting();
            }
        });



        age11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age11.setSweepAngle(50);
                age12.setSweepAngle(0);
                age13.setSweepAngle(0);
                age14.setSweepAngle(0);
                age15.setSweepAngle(0);
                age16.setSweepAngle(0);
                arcview2.setPointSize(118);
                starting();
            }
        });
        age12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age11.setSweepAngle(0);
                age12.setSweepAngle(50);
                age13.setSweepAngle(0);
                age14.setSweepAngle(0);
                age15.setSweepAngle(0);
                age16.setSweepAngle(0);
                arcview2.setPointSize(126);
                starting();
            }
        });
        age13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age11.setSweepAngle(0);
                age12.setSweepAngle(0);
                age13.setSweepAngle(50);
                age14.setSweepAngle(0);
                age15.setSweepAngle(0);
                age16.setSweepAngle(0);
                arcview2.setPointSize(136);
                starting();
            }
        });
        age14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age11.setSweepAngle(0);
                age12.setSweepAngle(0);
                age13.setSweepAngle(0);
                age14.setSweepAngle(50);
                age15.setSweepAngle(0);
                age16.setSweepAngle(0);
                arcview2.setPointSize(146);
                starting();
            }
        });
        age15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age11.setSweepAngle(0);
                age12.setSweepAngle(0);
                age13.setSweepAngle(0);
                age14.setSweepAngle(0);
                age15.setSweepAngle(50);
                age16.setSweepAngle(0);
                arcview2.setPointSize(156);
                starting();
            }
        });
        age16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age11.setSweepAngle(0);
                age12.setSweepAngle(0);
                age13.setSweepAngle(0);
                age14.setSweepAngle(0);
                age15.setSweepAngle(0);
                age16.setSweepAngle(50);
                arcview2.setPointSize(166);
                starting();
            }
        });
        //END AGE






        // Customize SpeedometerGauge

        //test


        mAlphaAnim = new AlphaAnimation(1, 0);
        mAlphaAnim.setDuration(200);
        mAlphaAnim.setRepeatCount(0);
        mAlphaAnim.setFillBefore(true);

        clearSpoValue();
        isPlay = false;
        mSpeed = dataCenter.getSpeed();
        mGain = dataCenter.getGain();
        mSpoStats = new SpoStats();
        mDbMgr = DbMgr.getInstance(getContext());
        mBleClient = BleClient.getSingleton(getContext());
        registerBroadcast();

//        TestWaveThread thread = new TestWaveThread();
//        thread.start();


        starting();


    }






    private  void starting() {

        new Thread() {
            public void run() {
                int i=0;
                final Boolean[] go = {true};
                worMode = dataCenter.getWorkMode();
                if (worMode == EWorMode.usb) {

                    try {

                        while (go[0]) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {


                                    if (mUsbMgr.openDevice(dataCenter.getBaudRate())) {
                                       // isPlay = true;
                                        startDevice();
                                        go[0] =false;
                                    }
                                }

                            });

                            Thread.sleep(5000);
                            i++;
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


            }
        }.start();

    };




    public void startDevice() {
        worMode = dataCenter.getWorkMode();
        if (worMode == EWorMode.usb) {

            if (mUsbMgr.openDevice(dataCenter.getBaudRate())) {
                isPlay = true;
            }

        }


        if (isPlay) {

            mSpeed = dataCenter.getSpeed();
            mGain = dataCenter.getGain();
            mSpoView.setFreq(Config.cFreq)
                    .setSpeed(mSpeed)
                    .setGain(mGain)
                    .startDraw();
            mSpoStats.startStats();
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
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(Config.RECEIVE_NEW_SPO_STATS));
            Toast.makeText(getContext(), getContext().getString(R.string.toast_measure_success), Toast.LENGTH_SHORT).show();
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


                } else {
                    mSpoView.setFingerOff(false);
                    int spo2 = bean.getSpO2();

                    if (spo2 == 127) {

                        arcmoveBehind(arcview.getSweepAngle(),70);
                        spo2Val.setText("70");



                    } else {

                        arcmove(arcview.getSweepAngle(),spo2);

                        spo2Val.setText(String.valueOf(spo2));

                    }

                    int pr = bean.getPR();
                    if (pr == 255) {

                        arc2moveBehind(arcview2.getSweepAngle(),50);
                        bmpVal.setText(String.valueOf(50));
                    } else {
                        if(pr>arcview2.getSweepAngle())
                        arc2move(arcview2.getSweepAngle(),pr);
                        else
                            arc2moveBehind(arcview2.getSweepAngle(),pr);
                        bmpVal.setText(String.valueOf(pr));


                    }


                    pi.setText(String.valueOf(bean.getStrength())+"%");

                }
            });
            lastTick = curTick;
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ESpeed speed = dataCenter.getSpeed();
            EGain gain = dataCenter.getGain();
            if (mSpeed != speed) {

                mSpeed = speed;
                mSpoView.setSpeed(mSpeed);
            }

            if (mGain != gain) {

                mGain = gain;
                mSpoView.setGain(mGain);
            }
        }
    };

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.RECEIVE_SETTING_PARAM);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
    }

    private void unRegisterBroadcast() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }
}
