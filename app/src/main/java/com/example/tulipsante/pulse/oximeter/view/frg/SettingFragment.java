package com.example.tulipsante.pulse.oximeter.view.frg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.example.tulipsante.pulse.oximeter.BaseFrg;
import com.example.tulipsante.pulse.oximeter.Config;
import com.example.tulipsante.pulse.oximeter.DataCenter;
import com.example.tulipsante.pulse.oximeter.bean.EGain;
import com.example.tulipsante.pulse.oximeter.bean.ELang;
import com.example.tulipsante.pulse.oximeter.bean.ESpeed;
import com.example.tulipsante.pulse.oximeter.bean.EWorMode;
import com.example.tulipsante.pulse.oximeter.bean.Range;
import com.example.tulipsante.pulse.oximeter.device.usb2serial.bean.BaudRate;
import com.example.tulipsante.pulse.oximeter.util.util.AndroidUtil;
import com.example.tulipsante.pulse.oximeter.util.util.ContinuousClick;
import com.example.tulipsante.pulse.oximeter.view.Pulse;
import com.example.tulipsante.pulse.oximeter.view.view.dialog.BaudRateDialog;
import com.example.tulipsante.R;
import com.example.tulipsante.pulse.oximeter.view.view.dialog.DoubleSeekBarDialog;
import com.example.tulipsante.pulse.oximeter.view.view.dialog.SelectDialog;

import java.util.List;

public class SettingFragment extends BaseFrg implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView mWorkMode;
    private TextView mSpeed;
    private TextView mGain;
    private TextView mSpo2AlarmVal;
    private TextView mPrAlarmVal;
    private TextView mAppVer;
    private SwitchCompat mPulseSoundSw;
    private SwitchCompat mSpo2AlarmSw;
    private SwitchCompat mPrAlarmSw;
    private SwitchCompat mLangSw;

    private DataCenter dataCenter;
    private ContinuousClick mContinuousClick = new ContinuousClick(10, new ContinuousClick.OnContiClick() {
        @Override
        public void onContiClick() {
            BaudRate baudRate = dataCenter.getBaudRate();
            BaudRateDialog.showDialog(getContext(), baudRate, new BaudRateDialog.OnValueListener() {
                @Override
                public void onValue(BaudRate baudRate) {
                    dataCenter.setBaudRate(baudRate);
                }
            });
        }
    });

    @Override
    protected View getLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_setting, container, false);
    }

    @Override
    protected void initView(View container) {
        mWorkMode = container.findViewById(R.id.txt_work_mode);
        mSpeed = container.findViewById(R.id.txt_speed);
        mGain = container.findViewById(R.id.txt_gain);
        mSpo2AlarmVal = container.findViewById(R.id.txt_spo2_val);
        mPrAlarmVal = container.findViewById(R.id.txt_pr_val);
        mAppVer = container.findViewById(R.id.txt_app_version);
        mPulseSoundSw = container.findViewById(R.id.sw_pulse_sound);
        mSpo2AlarmSw = container.findViewById(R.id.sw_spo2_alarm);
        mPrAlarmSw = container.findViewById(R.id.sw_pr_alarm);
        mLangSw = container.findViewById(R.id.sw_language);

        container.findViewById(R.id.layout_work_mode).setOnClickListener(this);
        container.findViewById(R.id.layout_speed).setOnClickListener(this);
        container.findViewById(R.id.layout_gain).setOnClickListener(this);
        container.findViewById(R.id.layout_spo2_val).setOnClickListener(this);
        container.findViewById(R.id.layout_pr_val).setOnClickListener(this);
        container.findViewById(R.id.layout_app_version).setOnClickListener(this);

        mPulseSoundSw.setOnCheckedChangeListener(this);
        mSpo2AlarmSw.setOnCheckedChangeListener(this);
        mPrAlarmSw.setOnCheckedChangeListener(this);
        mLangSw.setOnCheckedChangeListener(this);

        dataCenter = DataCenter.getInstance(getContext());
        setWorkMode(dataCenter.getWorkMode().toString());
        mSpeed.setText(dataCenter.getSpeed().toString());
        mGain.setText(dataCenter.getGain().toString());
        mSpo2AlarmVal.setText(dataCenter.getSpo2AlarmVal().toString());
        mPrAlarmVal.setText(dataCenter.getPrAlarmVal().toString());
        mAppVer.setText(AndroidUtil.getVersionName(getContext()));
        mPulseSoundSw.setChecked(dataCenter.isPulseSound());
        mSpo2AlarmSw.setChecked(dataCenter.isSpo2Alarm());
        mPrAlarmSw.setChecked(dataCenter.isPrAlarm());
        setLanguage(dataCenter.getLanguage().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_work_mode: {
                EWorMode workMode = dataCenter.getWorkMode();
                List<String> list = EWorMode.getList();
                SelectDialog.showDialog(getContext(), getString(R.string.work_mode), list, workMode.toString(), position -> {
                    EWorMode newWorkMode = EWorMode.getEnum(list.get(position));
                    if (workMode != newWorkMode) {
                        dataCenter.setWorkMode(newWorkMode);
                        setWorkMode(list.get(position));
                        Pulse act = (Pulse) getActivity();
                        if (act != null) {
                            act.invalidateOptionsMenu();
                        }


                    }
                });
            }
            break;
            case R.id.layout_speed: {
                ESpeed speed = dataCenter.getSpeed();
                List<String> list = ESpeed.getList();
                SelectDialog.showDialog(getContext(), getString(R.string.speed_level), list, speed.toString(), position -> {
                    dataCenter.setSpeed(ESpeed.getEnum(list.get(position)));
                    mSpeed.setText(list.get(position));
                    notifySettingParam(getContext());
                });
            }
            break;
            case R.id.layout_gain: {
                EGain gain = dataCenter.getGain();
                List<String> list = EGain.getList();
                SelectDialog.showDialog(getContext(), getString(R.string.gain_level), list, gain.toString(), position -> {
                    dataCenter.setGain(EGain.getEnum(list.get(position)));
                    mGain.setText(list.get(position));
                    notifySettingParam(getContext());
                });
            }
            break;
            case R.id.layout_spo2_val: {
                boolean isOpen = dataCenter.isSpo2Alarm();
                if (!isOpen) {
                    return;
                }
                Range range = dataCenter.getSpo2AlarmVal();
                DoubleSeekBarDialog.showDialog(getContext(), getString(R.string.alarm_spo2_value), Config.cSpo2Min, Config.cSpo2Max, range.from, range.to, (lower, upper) -> {
                    dataCenter.setSpo2AlarmVal(lower, upper);
                    mSpo2AlarmVal.setText(lower + "-" + upper);
                });
            }
            break;
            case R.id.layout_pr_val: {
                boolean isOpen = dataCenter.isPrAlarm();
                if (!isOpen) {
                    return;
                }

                Range range = dataCenter.getPrAlarmVal();
                DoubleSeekBarDialog.showDialog(getContext(), getString(R.string.alarm_pr_value), Config.cPrMin, Config.cPrMax, range.from, range.to, (lower, upper) -> {
                    dataCenter.setPrAlarmVal(lower, upper);
                    mPrAlarmVal.setText(lower + "-" + upper);
                });
            }
            break;
            case R.id.layout_app_version: {
                mContinuousClick.click();
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_pulse_sound:
                dataCenter.setPulseSound(isChecked);
                break;
            case R.id.sw_spo2_alarm:
                dataCenter.setSpo2Alarm(isChecked);
                break;
            case R.id.sw_pr_alarm:
                dataCenter.setPrAlarm(isChecked);
                break;
            case R.id.sw_language:
                String lang = ELang.en.toString();


                String pLang = dataCenter.getLanguage().toString();
                if (!pLang.equals(lang)) {
                    dataCenter.setLanguage(ELang.getEnum(lang));
                    Pulse.reStart(getActivity(), lang);


                }
                break;
            default:
                break;
        }
    }

    private void setWorkMode(String mode) {
        if (EWorMode.usb.toString().equals(mode)) {
            mWorkMode.setText(R.string.work_mode_usb);
        } else {
            mWorkMode.setText(R.string.work_mode_ble);
        }
    }

    private void setLanguage(String lang) {

            mLangSw.setChecked(true);

    }

    private void notifySettingParam(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Config.RECEIVE_SETTING_PARAM));
    }
}
