package com.esgi.virtualclassroom.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tools {
    public static void closeKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void printDeviceHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("ovh.bmyguest.bmyguest", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
