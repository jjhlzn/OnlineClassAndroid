package com.jinjunhang.framework.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;


//import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.album.NewLiveSongActivity;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.StatusHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
//import qiu.niorgai.StatusBarCompat;


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

    public static void setLightStatusBar(View view, Activity activity) {

        /*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.WHITE);
        } */
    }

    public static void showErrorMessage(Context context, String message) {

        showMessage(context, message);
    }

    public static void showMessage(Context context, String message, MaterialDialog.SingleButtonCallback listener) {

        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText("好的")
                .positiveColor(context.getResources().getColor(R.color.price_color))
                .contentColor(context.getResources().getColor(R.color.black))
                .backgroundColor(context.getResources().getColor(R.color.white))
                .onPositive(listener)

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

    public static int getListViewHeightBasedOnChildren2(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

            TextView textView = (TextView) listItem.findViewById(R.id.comment_content);
            TextView nameTV = (TextView) listItem.findViewById(R.id.comment_username);
            if (textView != null) {
                int h = getTextViewHeight(textView, listView.getContext());
                LogHelper.d(TAG, "h = " + h);
                totalHeight += h - getTextViewHeight(nameTV, listView.getContext());
            }
        }
        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

    public static int getListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        int screedWidth = getScreenWidth(listView.getContext());
        int listViewWidth = screedWidth; // - dip2px(listView.getContext(), 10);//listView在布局时的宽度
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(widthSpec, 0);

            int itemHeight = listItem.getMeasuredHeight();
            totalHeight += itemHeight;

            TextView textView = (TextView) listItem.findViewById(R.id.comment_content);
            TextView nameTV = (TextView) listItem.findViewById(R.id.comment_username);
            if (textView != null) {
                int h = getTextViewHeight(textView, listView.getContext());
                LogHelper.d(TAG, "h = " + h);
                totalHeight += h - getTextViewHeight(nameTV, listView.getContext());
            }
        }
        // 减掉底部分割线的高度
        int historyHeight = totalHeight + (listView.getDividerHeight() * listAdapter.getCount() - 1);


        return historyHeight;
    }

    public static int getTextViewHeight(TextView textView, Context context) {
        int screedWidth = getScreenWidth(context);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(screedWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }


    public static void setRefreshHeader(Activity activity, SmartRefreshLayout refreshLayout) {
        ClassicsHeader header = new ClassicsHeader(activity);
        header.setEnableLastTime(false);

        refreshLayout.setRefreshHeader(header);
    }

    public static void updateNavigationBarButton(Activity activity) {
        LogHelper.d(TAG, "updateNavigationBarButton called1");
        updateNavigationBarButton(activity, 1);
    }

    public static void updateNavigationBarButton(Activity activity, float alpha) {
        if (activity == null)
            return;
        LogHelper.d(TAG, "updateNavigationBarButton called2, alpha = " + alpha);
        MusicPlayer musicPlayer = MusicPlayer.getInstance(activity.getApplicationContext());
        Boolean isPlay = StatusHelper.isPlayingForUI(musicPlayer);
        setImage(isPlay, activity, alpha);
    }


    private static void setImage(boolean isPlay, final  Activity activity, float alpha) {
        if (activity != null && activity instanceof AppCompatActivity) {
            GifImageView musicBtn = null;
            if (((AppCompatActivity)activity).getSupportActionBar() != null) {
                //LogHelper.d(TAG, "actionBar not null");
                View v = ((AppCompatActivity) activity).getSupportActionBar().getCustomView();
                musicBtn =  v.findViewById(R.id.musicBtn);
            }

            if (activity instanceof BottomTabLayoutActivity) {
                Fragment frag = ((BottomTabLayoutActivity)activity).getCurrentFragment();
                //LogHelper.d(TAG, "frag = " + frag);
                Toolbar toolbar = ((BaseFragment)frag).getToolBar();
                if (toolbar != null)
                    musicBtn = toolbar.findViewById(R.id.musicBtn);

            } else if (activity.findViewById(R.id.toolbar) != null) {
               // LogHelper.d(TAG, "toolbar not null");
                musicBtn = activity.findViewById(R.id.toolbar).findViewById(R.id.musicBtn);
            } else if (activity instanceof SingleFragmentActivity) {
                Fragment frag = ((SingleFragmentActivity)activity).getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                if (frag != null) {
                    Toolbar toolbar = ((BaseFragment) frag).getToolBar();
                    if (toolbar != null)
                        musicBtn = toolbar.findViewById(R.id.musicBtn);
                }
            }

            //LogHelper.d(TAG, "musicBtn = " + musicBtn);

            if (musicBtn != null) {

                musicBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MusicPlayer musicPlayer = MusicPlayer.getInstance(activity.getApplicationContext());
                        Song song = musicPlayer.getCurrentPlaySong();
                        if (song != null) {
                            Intent i = new Intent(activity, NewLiveSongActivity.class);
                            activity.startActivity(i);
                        }
                    }
                });


                if (isPlay) {
                    if (isWhiteImage(alpha))
                        musicBtn.setImageResource(R.drawable.demo);
                    else
                        musicBtn.setImageResource(R.drawable.demo);


                } else {
                    if (isWhiteImage(alpha)) {
                        LogHelper.d(TAG, "use white version");
                        musicBtn.setImageResource(R.drawable.music_static_white);
                    } else {
                        //LogHelper.d(TAG, "use black version");
                        musicBtn.setImageResource(R.drawable.music_static);
                    }


                }
            }
        }
    }

    public static boolean isWhiteImage(float alpha) {
        if (alpha > 0.7) {
            return false;
        }
        return true;
    }

}
