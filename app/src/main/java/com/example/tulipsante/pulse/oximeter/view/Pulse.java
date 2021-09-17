package com.example.tulipsante.pulse.oximeter.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tulipsante.R;
import com.example.tulipsante.pulse.oximeter.BaseAct;
import com.example.tulipsante.pulse.oximeter.DataCenter;
import com.example.tulipsante.pulse.oximeter.bean.ELang;
import com.example.tulipsante.pulse.oximeter.bean.EWorMode;
import com.example.tulipsante.pulse.oximeter.view.frg.MeasureFragment;
import com.example.tulipsante.pulse.oximeter.view.frg.RecordFragment;
import com.example.tulipsante.pulse.oximeter.view.frg.SettingFragment;
import com.example.tulipsante.views.activities.ConsultationToolsActivity;
import com.google.android.material.tabs.TabLayout;

public class Pulse extends BaseAct {

    private TabLayout mTablayout;
    private Fragment[] mFragmensts = new Fragment[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse);

        initFrg();
        mTablayout = findViewById(R.id.tablayout);
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showFrg(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTablayout.addTab(mTablayout.newTab().setIcon(R.drawable.ic_spo2).setText(R.string.measure), true);
       // mTablayout.addTab(mTablayout.newTab().setIcon(R.drawable.ic_record).setText(R.string.record));
       // mTablayout.addTab(mTablayout.newTab().setIcon(R.drawable.ic_setting).setText(R.string.setting));
    }




    private void initFrg() {
        mFragmensts[0] = new MeasureFragment();
     // ²   mFragmensts[1] = new SettingFragment();
      //  mFragmensts[2] = new SettingFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment frg : mFragmensts) {
            transaction.add(R.id.container, frg).hide(frg);
        }
        transaction.commit();
    }

    private void showFrg(int pos) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragmensts.length; i++) {
            if (pos == i) {
                transaction.show(mFragmensts[i]);
            } else {
                transaction.hide(mFragmensts[i]);
            }
        }
        transaction.commit();
    }

    public static void reStart(Activity act, String lang) {
        Log.e("reStart", act.getComponentName().toString());
        Intent intent = new Intent();
        intent.setComponent(act.getComponentName());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        act.startActivity(intent);
        act.finish();
        Toast.makeText(act, act.getString(R.string.toast_switch_lang), Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).post(() -> setLaunchName(act, lang));
    }

    private static void setLaunchName(Context ctx, String lang) {
        PackageManager pm = ctx.getPackageManager();

           // pm.setComponentEnabledSetting(new ComponentName(ctx, ctx.getPackageName() + ".ChAlias"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(new ComponentName(ctx, ctx.getPackageName() + ".EnAlias"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }
}
