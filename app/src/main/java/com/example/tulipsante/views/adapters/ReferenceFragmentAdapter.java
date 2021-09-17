package com.example.tulipsante.views.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tulipsante.views.fragments.ReferedByMeFragment;
import com.example.tulipsante.views.fragments.ReferedByOthersFragment;

public class ReferenceFragmentAdapter extends FragmentStateAdapter {

    public ReferenceFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new ReferedByMeFragment();
        }
        return new ReferedByOthersFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
