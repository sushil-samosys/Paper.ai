package com.samosys.paperai.activity.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.samosys.paperai.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by samosys on 6/7/17.
 */

public class AppConstants {
    public static final String PREFS_NAME = "MyPrefs";
    //New Main server url

public static  int workPOS=00;
    public static boolean ALL = false;
    public static void getstatusbar(Activity context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window =context. getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#e5e5e5"));

        }
    }
    public static void hideKeyboard(Activity activity) {
      activity.  getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
    public static void getTranparentstatusbar(Activity context){

        Window window = context.getWindow();

        window.setStatusBarColor(context.getResources().getColor(R.color.blacktrans));
    }

    public static void keyboardhide(Activity context){

       context. getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String savePreferences(Context context, String key, String value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        try {
            settings = context.getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);

            editor = settings.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (NullPointerException e) {
            System.out.print("new_errrroe" + e);
        }

        return key;
    }

    public static String loadPreferences(Context context, String key) {
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        String value = settings.getString(key, "00");
        return value;

    }
    public static ProgressDialog showProgressDialog(Context context, String message){
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        m_Dialog.setCancelable(false);
//
//        m_Dialog.setCanceledOnTouchOutside(false);
        m_Dialog.show();
        return m_Dialog;
    }
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }


    public static void ClearPreferences(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        try {
            settings = context.getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);

            editor = settings.edit();
            editor.clear();
            editor.apply();
        } catch (NullPointerException e) {
            System.out.print("new_errrroe" + e);
        }

//        return key;
    }


}
