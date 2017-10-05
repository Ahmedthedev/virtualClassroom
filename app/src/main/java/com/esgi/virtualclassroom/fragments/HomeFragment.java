package com.esgi.virtualclassroom.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.adapters.HomeViewPagerAdapter;

public class HomeFragment extends Fragment {
    private ViewPager profileViewPager;
    private int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle(getString(R.string.fragment_home_title));
        }

        displayViewPager();
    }
    private void displayViewPager() {
        if (getView() == null) {
            return;
        }

        TabLayout tabLayout = getView().findViewById(R.id.tab_layout);
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fragment_home_tab_past)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fragment_home_tab_future)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fragment_home_tab_available)));

        profileViewPager = getView().findViewById(R.id.profile_viewpager);
        HomeViewPagerAdapter adapterViewPager = new HomeViewPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        profileViewPager.setAdapter(adapterViewPager);
        profileViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                profileViewPager.setCurrentItem(tab.getPosition());
                if (profileViewPager.getCurrentItem() != 0) {
                    currentPage = profileViewPager.getCurrentItem();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        profileViewPager.setCurrentItem(currentPage);
    }
}
