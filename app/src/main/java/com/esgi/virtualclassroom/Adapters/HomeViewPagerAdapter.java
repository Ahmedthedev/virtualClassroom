package com.esgi.virtualclassroom.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.esgi.virtualclassroom.fragments.FuturEventFragment;
import com.esgi.virtualclassroom.fragments.OffertEventFragment;
import com.esgi.virtualclassroom.fragments.PastEventFragment;

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {

    private int tabsNumber;

    public HomeViewPagerAdapter(FragmentManager fragmentManager, int tabsNumber) {
        super(fragmentManager);
        this.tabsNumber = tabsNumber;
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
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
