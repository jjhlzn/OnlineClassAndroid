package com.jinjunhang.framework.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lzn on 16/4/3.
 */
public class Utils {
    private final static String TAG = "Utils";

    public final static int PAGESIZE_APPROVAL = 25;
    public final static int PAGESIZE_ORDER = 25;

    public final static long UPDATE_TIME_DELTA = 1000 * 60 * 5;

    public static void showMessage(Context context, String message) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("确定", null);
        dlgAlert.create().show();
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



}
