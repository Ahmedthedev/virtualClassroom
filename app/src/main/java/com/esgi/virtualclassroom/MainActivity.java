package com.esgi.virtualclassroom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esgi.virtualclassroom.fragments.HomeFragment;
import com.esgi.virtualclassroom.utils.Tools;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchFragment(HomeFragment.newInstance(),true);
    }

    private void switchFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        fragment.setRetainInstance(true);
        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
        Tools.closeKeyboard(this);
    }
}