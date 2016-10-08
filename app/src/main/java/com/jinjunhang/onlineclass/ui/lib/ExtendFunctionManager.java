package com.jinjunhang.onlineclass.ui.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.other.ExtendFunctionActivity;
import com.jinjunhang.onlineclass.ui.cell.ExtendFunctionCell;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/20.
 */
public class ExtendFunctionManager {
    private final String TAG = LogHelper.makeLogTag(ExtendFunctionManager.class);

    private final int itemCountEachRow = 4;
    private int mShowMaxRow = 100;
    private Context mContext;
    private ClickListener mWebListener;
    private ClickListener mNotSupportListener;

    private List<ExtendFunction> functions = new ArrayList<>();

    public ExtendFunctionManager(Context context) {
        this(100, context, true);
    }

    public ExtendFunctionManager(Context context, boolean isNeedMore) {
        this(100, context, isNeedMore);
    }

    private ExtendFunction moreFunction;

    public ExtendFunctionManager(int showMaxRow, final Context context, boolean isNeedMore) {
        mShowMaxRow = showMaxRow;
        mContext = context;
        mWebListener = new WebClickListener();
        mNotSupportListener = new NotSupportClickListener();
        functions.add(new ExtendFunction(R.drawable.commoncard, "去刷卡", "", new ClickListener() {
            @Override
            public void onClick(ExtendFunction function) {
                LogHelper.d(TAG, "shua ka clicked");
                PackageManager packageManager = mContext.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.jfzf.oem");
                if(intent == null){
                    LogHelper.d(TAG, "APP not found!");
                    /*
                    Intent i = new Intent(mContext, WebBrowserActivity.class)
                            .putExtra(WebBrowserActivity.EXTRA_TITLE, "巨方支付下载")
                            .putExtra(WebBrowserActivity.EXTRA_URL, "https://uenpay.com/downloadcopy/jfjr/down-jfjr.html#rd"); */

                    Intent downloadIntent = new Intent();
                    downloadIntent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://uenpay.com/downloadcopy/jfjr/down-jfjr.html#rd");
                    downloadIntent.setData(content_url);
                    mContext.startActivity(downloadIntent);
                } else {
                    mContext.startActivity(intent);
                }
            }
        }));
        functions.add(new ExtendFunction(R.drawable.upicon, "提额秘诀", ServiceLinkManager.FunctionUpUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.visacard, "一键办卡", ServiceLinkManager.FunctionFastCardUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.cardmanager, "卡片管理", ServiceLinkManager.FunctionCardManagerUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.creditsearch, "信用查询", ServiceLinkManager.FunctionCreditSearchUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.mmcsearch, "mcc查询", ServiceLinkManager.FunctionMccSearchUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.shopcart, "商城", ServiceLinkManager.FunctionShopUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.rmb, "缴费", ServiceLinkManager.FunctionJiaoFeiUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.dollar, "贷款", ServiceLinkManager.FunctionLoanUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.car, " 汽车分期", ServiceLinkManager.FunctionCarLoanUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.customerservice, "客服", ServiceLinkManager.FunctionCustomerServiceUrl(), mWebListener));

        moreFunction =  new ExtendFunction(R.drawable.morefunction, "更多", "", new ClickListener() {
            @Override
            public void onClick(ExtendFunction function) {
                Intent i = new Intent(context, ExtendFunctionActivity.class);
                context.startActivity(i);
            }
        });
        if (isNeedMore) {
            int lastIndex = showMaxRow * itemCountEachRow - 1;
            if (lastIndex >= functions.size() - 1) {
                functions.add(moreFunction);
            } else {
                functions.set(lastIndex, moreFunction);
            }
        }
    }

    public int getRowCount() {
        int rows = (getFunctionCount() + (itemCountEachRow - 1)) / itemCountEachRow;
        return rows > mShowMaxRow ? mShowMaxRow : rows;
    }

    private int getFunctionCount() {
        return functions.size();
    }


    public int getHeight() {
        int screenWidth =   Utils.getScreenWidth(mContext);
        int screenHeight =   Utils.getScreenHeight(mContext);
        //LogHelper.d(TAG, "width = " + screenWidth + ", heigth = " + screenHeight);
        int height = 200;
        if (screenWidth >= 1440) {
            height = 250;
        }else  if (screenWidth <= 480) {
            LogHelper.d(TAG, "height is 100");
            height = 95;
        }
        else if (screenWidth <= 720) {
            height = 140;
        }
        LogHelper.d(TAG, "each row height in pixel = " + height);
        return height;
    }

    public ExtendFunctionCell getCell(int row) {
        ExtendFunctionCell cell = new ExtendFunctionCell((Activity) mContext);

        int startIndex = row * itemCountEachRow;

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ViewGroup.LayoutParams params =  new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // Changes the height and width to the specified *pixels*
        params.height = getHeight();
        layout.setLayoutParams(params);
        layout.setBackgroundColor(Color.WHITE );

        for(int i = 0; i < itemCountEachRow; i++) {
            int index = startIndex + i;
            if (index >= functions.size() ) {
                break;
            }
            ExtendFunction function = functions.get(index);

            layout.addView(createSubView(function));
        }
        layout.setEnabled(false);
        layout.setOnClickListener(null);
        cell.setView(layout);
        return cell;
    }

    private ViewGroup createSubView(final ExtendFunction function) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        int width = Utils.getScreenWidth(mContext);
        ViewGroup.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // Changes the height and width to the specified *pixels*
        params.width = width / 4;
        layout.setLayoutParams(params);

        ViewGroup.LayoutParams imageParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageParams.width = (int)(width / 4 * 0.45);
        imageParams.height = (int)(width / 4 * 0.45);
        ImageView imageView = new ImageView(mContext);
        imageView.setPadding(0, 20, 0, 0);
        imageView.setLayoutParams(imageParams);

        imageView.setImageResource(function.image);
        layout.addView(imageView);

        ViewGroup.LayoutParams textParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        textParams.width = width / 4;
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(textParams);
        textView.setText(function.name);
        textView.setWidth(width / 4);
        textView.setTextSize(12);
        textView.setGravity(Gravity.CENTER);

        layout.addView(textView);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function.listener.onClick(function);
            }
        });

        return layout;
    }

    interface ClickListener {
        void onClick(ExtendFunction function);
    }

    class WebClickListener implements ClickListener {
        @Override
        public void onClick(ExtendFunction function) {
            Intent i = new Intent(mContext, WebBrowserActivity.class)
                    .putExtra(WebBrowserActivity.EXTRA_TITLE, function.name)
                    .putExtra(WebBrowserActivity.EXTRA_URL, function.url);
            mContext.startActivity(i);
        }
    }

    class NotSupportClickListener implements ClickListener {
        @Override
        public void onClick(ExtendFunction function) {
            Utils.showMessage(mContext, "敬请期待");
        }
    }


    class ExtendFunction {
        private int image;
        private String name;
        private String url;
        private ClickListener listener;

        ExtendFunction() {}
        ExtendFunction(int image, String name, String url, ClickListener listener) {
            this.image = image;
            this.name = name;
            this.url = url;
            this.listener = listener;
        }
    }

}
