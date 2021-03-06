package com.example.brewerykegtrackandtrace;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {


    int tab_count;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tab_count=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new k50();
            case 1: return new k30();
            case 2: return new kempty();
            case 3: return new kCO2();
            case 4: return new kDispenser();
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return tab_count;
    }
}
