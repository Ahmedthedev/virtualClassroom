package com.esgi.virtualclassroom.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.esgi.virtualclassroom.modules.classroom.fragments.AttachmentsFragment;
import com.esgi.virtualclassroom.fragments.RecorderFragment;


public class RecorderViewPagerAdapter extends FragmentStatePagerAdapter {

    private int tabsNumber;
    private String moduleId;
    private boolean isProf;

    public RecorderViewPagerAdapter(FragmentManager fragmentManager, int tabsNumber,String moduleId,boolean isProf) {
        super(fragmentManager);
        this.tabsNumber = tabsNumber;
        this.moduleId = moduleId;
        this.isProf = isProf;
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }

    public RecorderViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RecorderFragment.newInstance(moduleId,isProf);
            case 1:
                return AttachmentsFragment.newInstance(moduleId);
            default:
                return null;
        }
    }
}