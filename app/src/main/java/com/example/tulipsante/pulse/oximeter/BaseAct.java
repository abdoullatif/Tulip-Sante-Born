package com.example.tulipsante.pulse.oximeter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tulipsante.R;

import com.example.tulipsante.pulse.oximeter.bean.ELang;
import com.example.tulipsante.pulse.oximeter.util.util.LanguageUtil;


public class BaseAct extends AppCompatActivity {
    /**
     * 此方法先于 onCreate()方法执行
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        ELang lang = DataCenter.getInstance(newBase).getLanguage();
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, lang.toString()));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  ActionBar actionBar = getSupportActionBar();
      //  if (actionBar != null) {
       //     actionBar.setTitle(R.string.app_name);
      //  }
    }
}
