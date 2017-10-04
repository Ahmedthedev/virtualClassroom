package com.esgi.virtualclassroom.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.esgi.virtualclassroom.fragments.FuturEventFragment;
import com.esgi.virtualclassroom.fragments.OffertEventFragment;
import com.esgi.virtualclassroom.fragments.PastEventFragment;

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PastEventFragment.newInstance();
            case 1:
                return OffertEventFragment.newInstance();
            case 2:
                return FuturEventFragment.newInstance();
            default:
                return null;
        }
    }
}
