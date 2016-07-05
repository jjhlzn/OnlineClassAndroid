package com.jinjunhang.onlineclass.ui.lib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
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
        functions.add(new ExtendFunction(R.drawable.commoncard, "去刷卡", "http://www.baidu.com", new ClickListener() {
            @Override
            public void onClick(ExtendFunction function) {
                LogHelper.d(TAG, "shua ka clicked");
                PackageManager packageManager = mContext.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.jinjunhang.contract");
                if(intent==null){
                    System.out.println("APP not found!");
                }
                mContext.startActivity(intent);
            }
        }));
        functions.add(new ExtendFunction(R.drawable.upicon, "提额秘诀", "http://114.215.236.171:6012/Service/CreditLines", mWebListener));
        functions.add(new ExtendFunction(R.drawable.visacard, "一键办卡", "http://114.215.236.171:6012/Service/FastCard", mWebListener));
        functions.add(new ExtendFunction(R.drawable.cardmanager, "卡片管理", "http://114.215.236.171:6012/Service/CardManage", mWebListener));
        functions.add(new ExtendFunction(R.drawable.creditsearch, "信用查询", "http://114.215.236.171:6012/Service/Ipcrs", mWebListener));
        functions.add(new ExtendFunction(R.drawable.mmcsearch, "mcc查询", "http://114.215.236.171:6012/Service/MccSearch", mWebListener));
        functions.add(new ExtendFunction(R.drawable.shopcart, "商城", "", mNotSupportListener));
        functions.add(new ExtendFunction(R.drawable.rmb, "缴费", "", mNotSupportListener));
        functions.add(new ExtendFunction(R.drawable.dollar, "贷款", "", mNotSupportListener));
        functions.add(new ExtendFunction(R.drawable.car, " 汽车分期", "", mNotSupportListener));
        functions.add(new ExtendFunction(R.drawable.customerservice, "客服", "", mNotSupportListener));

        moreFunction =  new ExtendFunction(R.drawable.morefunction, "更多", "http://www.baidu.com", new ClickListener() {
            @Override
            public void onClick(ExtendFunction function) {
                Intent i = new Intent(context, ExtendFunctionActivity.class);
                context.startActivity(i);
            }
        });
        if (isNeedMore) {
            functions.add(moreFunction);
        }
    }

    public int getRowCount() {
        int rows = (getFunctionCount() + (itemCountEachRow - 1)) / itemCountEachRow;
        return rows > mShowMaxRow ? mShowMaxRow : rows;
    }

    private int getFunctionCount() {
        return functions.size();
    }

    public ExtendFunctionCell getCell(int row) {
        ExtendFunctionCell cell = new ExtendFunctionCell((Activity) mContext);

        int startIndex = row * itemCountEachRow;

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ViewGroup.LayoutParams params =  new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        // Changes the height and width to the specified *pixels*
        params.height = 200;
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
