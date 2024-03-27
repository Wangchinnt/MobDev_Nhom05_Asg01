package com.mygdx.game;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPageAdapter extends FragmentStatePagerAdapter {
    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentAircraft1();
            case 1:
                return new FragmentAircraft2();
            case 2:
                return new FragmentAircraft3();
            default:
                return new FragmentAircraft1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "AC1";
            case 1:
                title = "AC2";
            case 2:
                title = "AC3";
            default:
                title = "AC1";
        }
        return title;
    }
}
