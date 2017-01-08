package com.jinjunhang.onlineclass.ui.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.service.ClearFunctionMessageRequest;
import com.jinjunhang.onlineclass.service.ClearFunctionMessageResponse;
import com.jinjunhang.onlineclass.service.GetFunctionMessageResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumListActivity;
import com.jinjunhang.onlineclass.ui.activity.other.ExtendFunctionActivity;
import com.jinjunhang.onlineclass.ui.cell.ExtendFunctionCell;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.ui.fragment.album.AlbumListFragment;

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
    private ExtendFunctoinMessageManager  mMessageManager;

    private List<ExtendFunction> functions = new ArrayList<>();

    public ExtendFunctionManager(ExtendFunctoinMessageManager messageManager, Context context) {
        this(messageManager, 100, context, true);
    }

    public ExtendFunctionManager(ExtendFunctoinMessageManager messageManager, Context context, boolean isNeedMore) {
        this(messageManager, 100, context, isNeedMore);
    }

    private ExtendFunction moreFunction;

    public ExtendFunctionManager(ExtendFunctoinMessageManager messageManager, int showMaxRow, final Context context, boolean isNeedMore) {
        this.mMessageManager = messageManager;
        mShowMaxRow = showMaxRow;
        mContext = context;
        mWebListener = new WebClickListener();
        mNotSupportListener = new NotSupportClickListener();
        functions.add(new ExtendFunction(R.drawable.f_paybycard, "刷卡", "f_paybycard", "", new BaseClickListener() {
            @Override
            public void onClick(ExtendFunction function) {
                super.onClick(function);
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
        functions.add(new ExtendFunction(R.drawable.f_live, "直播课堂", "f_class", ServiceLinkManager.FunctionUpUrl(), new BaseClickListener() {
            @Override
            public void onClick(ExtendFunction function) {
                super.onClick(function);
                Intent i = new Intent(mContext, AlbumListActivity.class);
                mContext.startActivity(i);
            }
        }));
        functions.add(new ExtendFunction(R.drawable.f_makecard, "快速办卡", "f_makecard", ServiceLinkManager.FunctionFastCardUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.f_loan, "快速贷款", "f_loan", ServiceLinkManager.FunctionLoanUrl(),  mWebListener));

        functions.add(new ExtendFunction(R.drawable.f_market, "商城", "f_market", ServiceLinkManager.FunctionShopUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.f_car, " 汽车分期", "f_car", ServiceLinkManager.FunctionCarLoanUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.f_cardmanager, "卡片管理", "f_cardmanager",ServiceLinkManager.FunctionCardManagerUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.f_chongzhi, "我要充值", "f_chongzhi", ServiceLinkManager.FunctionJiaoFeiUrl(), mWebListener));

        functions.add(new ExtendFunction(R.drawable.f_share, "提额秘诀", "f_share", ServiceLinkManager.FunctionUpUrl(), mWebListener));
        //functions.add(new ExtendFunction(R.drawable.creditsearch, "信用查询", ServiceLinkManager.FunctionCreditSearchUrl(), mWebListener));
        //functions.add(new ExtendFunction(R.drawable.mmcsearch, "mcc查询", ServiceLinkManager.FunctionMccSearchUrl(), mWebListener));
        functions.add(new ExtendFunction(R.drawable.f_user, "客服", "f_user", ServiceLinkManager.FunctionCustomerServiceUrl(), mWebListener));

        moreFunction =  new ExtendFunction(R.drawable.f_more, "更多", "f_more", "", new BaseClickListener() {
            @Override
            public void onClick(ExtendFunction function) {
                super.onClick(function);
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

    public List<ExtendFunction> getFunctions() {
        return functions;
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
        int height = 250;
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

        //LinearLayout layout = new LinearLayout(mContext);
        //layout.setOrientation(LinearLayout.HORIZONTAL);

        //ViewGroup.LayoutParams params =  new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // Changes the height and width to the specified *pixels*
        //params.height = getHeight();
        //layout.setLayoutParams(params);
        //layout.setBackgroundColor(Color.WHITE );

        for(int i = 0; i < itemCountEachRow; i++) {
            int index = startIndex + i;
            if (index >= functions.size() ) {
                break;
            }
            ExtendFunction function = functions.get(index);
            //layout.addView(createSubView(function));
            cell.addFunction(function);
        }
        cell.setHeight(getHeight());
        cell.setFunctionManager(this);
        //layout.setEnabled(false);
        //layout.setOnClickListener(null);
        //cell.setView(layout);
        return cell;
    }


    public void makeImage(ImageView image, ExtendFunction func) {
        //LogHelper.d(TAG, func.name, " has message: ", func.hasMessage());
        // 防止出现Immutable bitmap passed to Canvas constructor错误
        Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), func.image).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bitmap2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.f_1).copy(Bitmap.Config.ARGB_8888, true);

        Bitmap newBitmap = Bitmap.createBitmap(bitmap1);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();

        int w = bitmap1.getWidth();
        int h = bitmap1.getHeight();

        int w_2 = bitmap2.getWidth();
        int h_2 = bitmap2.getHeight();

        paint.setColor(Color.GRAY);
        paint.setAlpha(0);
        canvas.drawRect(0, 0, w + 20, h + 20, paint);

        if (func.hasMessage()) {
            Paint paint2 = new Paint();
            canvas.drawBitmap(bitmap2, Math.abs(w - w_2) / 2 * 2 + 6, -6, paint2);
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        // 存储新合成的图片
        canvas.restore();

        image.setImageBitmap(newBitmap);
    }


    public ViewGroup createSubView(final ExtendFunction function) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        int width = Utils.getScreenWidth(mContext);
        ViewGroup.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // Changes the height and width to the specified *pixels*
        params.width = width / 4;
        layout.setLayoutParams(params);

        ViewGroup.LayoutParams imageParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageParams.width = (int)(width / 4 * 0.7);
        imageParams.height = (int)(width / 4 * 0.7);
        ImageView imageView = new ImageView(mContext);
        imageView.setPadding(0, 20, 0, 0);
        imageView.setLayoutParams(imageParams);
        //imageView.setImageResource(function.image);
        makeImage(imageView, function);
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

    class BaseClickListener implements  ClickListener {
        @Override
        public void onClick(ExtendFunction function) {
            //取消这个Cell的消息，并把这个信息告诉服务器
            new ClearFunctionMessageRequestTask().execute(function);
        }
    }

    private class ClearFunctionMessageRequestTask extends AsyncTask<ExtendFunctionManager.ExtendFunction, Void, ClearFunctionMessageResponse> {
        @Override
        protected ClearFunctionMessageResponse doInBackground(ExtendFunctionManager.ExtendFunction... functions) {
            mMessageManager.clearMessage(functions[0].getCode());
            ClearFunctionMessageRequest request = new ClearFunctionMessageRequest();
            request.setCodes(new String[]{functions[0].getCode()});
            return new BasicService().sendRequest(request);
        }
    }

    class WebClickListener extends BaseClickListener {
        @Override
        public void onClick(ExtendFunction function) {
            super.onClick(function);
            Intent i = new Intent(mContext, WebBrowserActivity.class)
                    .putExtra(WebBrowserActivity.EXTRA_TITLE, function.name)
                    .putExtra(WebBrowserActivity.EXTRA_URL, function.url);
            mContext.startActivity(i);
        }
    }

    class NotSupportClickListener extends BaseClickListener {
        @Override
        public void onClick(ExtendFunction function) {
            super.onClick(function);
            Utils.showMessage(mContext, "敬请期待");
        }
    }


    public class ExtendFunction {
        private int image;
        private String name;
        private String code;
        private String url;
        private ClickListener listener;

        /*
        ExtendFunction(int image, String name, String url, ClickListener listener) {
            this(image, name, url, false, listener);
        }*/

        ExtendFunction(int image, String name, String code, String url, ClickListener listener) {
            this.image = image;
            this.name = name;
            this.code = code;
            this.url = url;
            this.listener = listener;
        }


        public boolean hasMessage() {
            return mMessageManager.hasMessage(this.code);
        }

        public String getCode() {
            return code;
        }
    }

}
