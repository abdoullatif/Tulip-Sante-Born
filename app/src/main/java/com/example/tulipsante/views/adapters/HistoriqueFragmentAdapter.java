package com.example.tulipsante.views.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.example.tulipsante.views.fragments.HistoriquePresenceFragment;
import com.example.tulipsante.views.fragments.HistoriqueTacheFragment;

public class HistoriqueFragmentAdapter extends FragmentStateAdapter {

    public HistoriqueFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new HistoriqueTacheFragment();
        }
        return new HistoriquePresenceFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
