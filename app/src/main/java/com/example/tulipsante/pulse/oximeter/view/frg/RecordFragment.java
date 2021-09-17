package com.example.tulipsante.pulse.oximeter.view.frg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tulipsante.pulse.oximeter.BaseFrg;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.example.tulipsante.R;

public class RecordFragment extends BaseFrg {
    private Fragment[] mFragmensts = new Fragment[2];
    private TabLayout mTablayout;
    private ViewPager2 mViewPager2;

    @Override
    protected View getLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_record, container, false);
    }

    @Override
    protected void initView(View container) {
        mFragmensts[0] = new RecordListFragment();
        mFragmensts[1] = new RecordChartFragment();

        mTablayout = container.findViewById(R.id.tablayout);
        mViewPager2 = container.findViewById(R.id.viewPager);
        mViewPager2.setAdapter(new FrgAdapter(this));
        mViewPager2.setUserInputEnabled(false); //true:滑动, false:禁止滑动
        TabLayoutMediator mediator = new TabLayoutMediator(mTablayout, mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText(R.string.list);
                } else if (position == 1) {
                    tab.setText(R.string.chart);
                }
            }
        });
        mediator.attach();
    }


    public class FrgAdapter extends FragmentStateAdapter {

        public FrgAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmensts[position];
        }

        @Override
        public int getItemCount() {
            return mFragmensts.length;
        }
    }

}

