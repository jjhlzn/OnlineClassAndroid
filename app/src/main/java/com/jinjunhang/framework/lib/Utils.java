package com.jinjunhang.framework.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.afollestad.materialdialogs.MaterialDialog;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.player.utils.LogHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lzn on 16/4/3.
 */
public class Utils {
    private final static String TAG = "Utils";

    public final static int PAGESIZE_APPROVAL = 25;
    public final static int PAGESIZE_ORDER = 25;

    public final static long UPDATE_TIME_DELTA = 1000 * 60 * 5;

    public static void showMessage(Context context, String message) {
        /*
        new SweetAlertDialog(context)
                .setTitleText(message)
                .show();*/
        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText("好的")
                .show();
    }

    public static void showErrorMessage(Context context, String message) {

        /*
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("")
                .setContentText(message)
                .show();*/
        showMessage(context, message);
    }

    public static void showMessage(Context context, String message, DialogInterface.OnDismissListener listener) {
        SweetAlertDialog dialog = new SweetAlertDialog(context)
                .setTitleText(message);
        dialog.setOnDismissListener(listener);
        dialog.show();
    }


    public static void showMessage(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("确定", listener);
        dlgAlert.create().show();
    }


    public static void showConfirmMessage(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("确定", listener);
        dlgAlert.setNegativeButton("取消", null);
        dlgAlert.create().show();
    }


    public static void showServerErrorDialog(Context context){
        showMessage(context, "服务器返回出错!");
    }

    public static int compareDate(String dateStr1, String dateStr2) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = dt.parse(dateStr1);
            Date date2 = dt.parse(dateStr2);
            return date1.compareTo(date2);
        }
        catch (Exception ex){
            Log.e(TAG, ex.toString());
        }
        return 0;
    }

    public static int screenWidth = -1;
    public static int screenHeight = -1;

    public static int getScreenWidth(Context context) {
        if (screenWidth < 0) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        }
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        if (screenWidth < 0) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        }
        return screenHeight;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity != null && activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            View v = activity.getCurrentFocus();
            v.clearFocus();
           // LogHelper.d(TAG, "lose focus");
        }

    }


    public static void setupUI4HideKeybaord(View view, final Activity activity) {
        //Set up touch listener for non-text box views to hide keyboard.
        LogHelper.d(TAG, "id = " + view.getId() + ", bottom_comment.id = " + R.id.bottom_comment);
        if (view.getId() == R.id.bottom_comment) {
            return;
        }
        if(!(view instanceof EditText)) {
            LogHelper.d(TAG, "register onTouchListener for id = " + view.getId());
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                   // LogHelper.d(TAG, "onTouch called");
                    Utils.hideSoftKeyboard(activity);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI4HideKeybaord(innerView, activity);
            }
        }
    }

    /**
     * parse milisecons to hh:mm:ss的格式字符串, 例如3661000位 01:01:01
     * @param milliseconds
     * @return
     */
    public static String convertTimeString(long milliseconds) {
        long sec = milliseconds/1000;
        long hour = sec / 60 / 60;
        long reminder = sec - hour * 3600;
        long minutes = reminder / 60;
        reminder = reminder - minutes * 60;
        if (hour == 0) {
            return String.format("%2d:%2d", minutes, reminder).replace(" ", "0");
        } else {
            return String.format("%2d:%2d:%2d", hour, minutes, reminder).replace(" ", "0");
        }
    }

    /*
    public static LoginUser getLoginUser() {
        Iterator<LoginUser> users = LoginUser.findAll(LoginUser.class);
        if (users.hasNext()) {
            return users.next();
        }
        return null;
    }

    public static void removeLoginUser() {
        LoginUser.delete(LoginUser.class);
    } */

}
