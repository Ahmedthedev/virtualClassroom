package com.esgi.virtualclassroom.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

public class Tools {
    public static void closeKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void switchFragment(AppCompatActivity activity, int containerId, Fragment fragment, boolean addToBackStack) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        fragment.setRetainInstance(true);
        transaction.replace(containerId, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
        Tools.closeKeyboard(activity);
    }
}
