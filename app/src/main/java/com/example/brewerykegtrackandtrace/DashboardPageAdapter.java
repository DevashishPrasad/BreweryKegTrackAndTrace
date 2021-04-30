package com.example.brewerykegtrackandtrace;

import android.util.Log;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DashboardPageAdapter extends FragmentPagerAdapter {

    int tab_count;
    public DashboardPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tab_count=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new DashboardDailyListFragment();
            case 1:
                return new DashboardWeeklyListFragment();
            case 2:
                return new DashboardMonthlyListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tab_count;
    }
}
