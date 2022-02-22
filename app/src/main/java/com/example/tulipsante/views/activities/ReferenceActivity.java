package com.example.tulipsante.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import com.example.tulipsante.R;
import com.example.tulipsante.views.adapters.HistoriqueFragmentAdapter;
import com.example.tulipsante.views.adapters.ReferenceFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class ReferenceActivity extends AppCompatActivity {

    // Views
    private CardView buttonBack;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    // View pager adapter
    private ReferenceFragmentAdapter referenceFragmentAdapter;

    private void initViews() {
        buttonBack = findViewById(R.id.cardViewBack);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPageSearch);
    }

    private void initialisation() {
        FragmentManager fm = getSupportFragmentManager();
        referenceFragmentAdapter = new ReferenceFragmentAdapter(fm, getLifecycle());
        viewPager2.setAdapter(referenceFragmentAdapter);
    }

    private void setUpViewPager() {
        tabLayout.addTab(tabLayout.newTab().setText(getText(R.string.referred_by_others)
                .toString()));
        tabLayout.addTab(tabLayout.newTab().setText(getText(R.string.referred_by_me)
                .toString()));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_reference);

        initViews();
        initialisation();
        setUpViewPager();

        onBackButtonPressed();
    }

    private void setApplicationTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentTheme = sharedPreferences.getString("current_Theme",null);
        if(currentTheme.equals("dark")) {
            setTheme(R.style.AppTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
    }

    private void onBackButtonPressed() {
        buttonBack.setOnClickListener(view -> finish());
    }
}