package com.jinjunhang.framework.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by lzn on 16/4/3.
 */
public class Utils {
    private final static String TAG = "Utils";

    public static int BOTTOM_BAR_HEIGHT = 153; //in dp

    public static final String WEIXIN_SHERE_APP_ID = "wx73653b5260b24787";

    public static void showMessage(Context context, String message) {
        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText("好的")
                .show();
    }

    public static void showErrorMessage(Context context, String message) {

        showMessage(context, message);
    }

    public static void showMessage(Context context, String message, DialogInterface.OnClickListener listener) {
        new AlertDialogWrapper.Builder(context)
                .setMessage(message)
                .setNegativeButton("好的", listener)
                .show();
    }

    public static void showVipBuyMessage(final Context context, String message) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("购买", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(context, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "课程购买")
                        .putExtra(WebBrowserActivity.EXTRA_URL, ServiceLinkManager.MyAgentUrl());
                context.startActivity(i);
            }
        });
        dlgAlert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dlgAlert.setCancelable(false);

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
            DisplayMetrics displaymetrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(displaymetrics);
            screenHeight = displaymetrics.heightPixels;
            screenWidth = displaymetrics.widthPixels;
            //LogHelper.d(TAG, "compute: width = " + screenWidth + ", height = " + screenHeight);
        }
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        //LogHelper.d(TAG, "getScreenHeight called: width = " + screenWidth + ", height = " + screenHeight);
        if (screenWidth < 0) {
            getScreenWidth(context);
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
        //LogHelper.d(TAG, "id = " + view.getId() + ", bottom_comment.id = " + R.id.bottom_comment);
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

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,context.getResources().getDisplayMetrics());
        }
        LogHelper.d(TAG, "action bar height = " + actionBarHeight);
        return actionBarHeight;
    }

    public static int getStatusBarHeight(Activity context) {
        Rect rectangle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop =
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight= contentViewTop - statusBarHeight;
        return titleBarHeight;
    }

    public static boolean handleWechatPay(IWXAPI wxApi, String url, LoadingAnimation loadingAnimation) {
        if (url.startsWith("wechatpay://")) {

            boolean isPaySupported = wxApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
            if (!isPaySupported){
                if (loadingAnimation != null) {
                    Utils.showErrorMessage(loadingAnimation.getActivity(), "");
                }
                return false;
            }

            /*
            if (loadingAnimation != null) {
                loadingAnimation.show("");
            }*/

            String jsonString = url.substring(12);
            LogHelper.d(TAG, "jsonString = " + jsonString);
            try {
                JSONObject json = new JSONObject(jsonString);
                PayReq request = new PayReq();
                request.appId = json.getString("appid");
                request.partnerId = json.getString("partnerid");
                request.prepayId= json.getString("prepayid");
                request.packageValue = json.getString("package");
                request.nonceStr= json.getString("noncestr");
                request.timeStamp= json.getString("timestamp");
                request.sign= json.getString("sign");
                wxApi.sendReq(request);
            } catch (JSONException ex) {
                LogHelper.e(TAG, ex);
            }
            return true;
        }
        return false;
    }

    public static boolean isAndroid4() {
        return android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isAndroid5() {
        return android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M
               &&  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasNavigationBar(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

}
