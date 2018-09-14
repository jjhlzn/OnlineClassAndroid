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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.ExtendFunctionImageDao;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.service.ClearFunctionMessageRequest;
import com.jinjunhang.onlineclass.service.ClearFunctionMessageResponse;
import com.jinjunhang.onlineclass.service.GetHeaderAdvRequest;
import com.jinjunhang.onlineclass.service.GetHeaderAdvResponse;
import com.jinjunhang.onlineclass.ui.activity.QuestionsActivity;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.ZhuanLanListActivity;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumListActivity;
import com.jinjunhang.onlineclass.ui.activity.other.ExtendFunctionActivity;
import com.jinjunhang.onlineclass.ui.activity.user.QRImageActivity;
import com.jinjunhang.onlineclass.ui.cell.ExtendFunctionCell;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.ui.fragment.ZhuanLanListFragment;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.LogManager;

/**
 * Created by lzn on 16/6/20.
 */
public class ExtendFunctionManager {
    private final String TAG = LogHelper.makeLogTag(ExtendFunctionManager.class);

    public static final String RONGZI_TYPE = "rongzi";
    public static final String TOUZI_TYPE = "touzi";

    private final int itemCountEachRow = 5;
    private int mShowMaxRow = 100;
    private Context mContext;
    private ClickListener mWebListener;
    private ClickListener mNotSupportListener;
    private ExtendFunctoinVariableInfoManager mMessageManager;
    private boolean mIsNeedMore;
    private String type;

    private List<ExtendFunction> functions = new ArrayList<>();
    private ExtendFunction moreFunction;

    private static ExtendFunctionManager instance = null;

    public static ExtendFunctionManager getInstance(Context context) {
        if (instance == null) {
            instance = new ExtendFunctionManager(null, 100, context, true, RONGZI_TYPE);
        }
        return instance;
    }

    public void setFunctions(List<ExtendFunction> functions) {
        this.functions = functions;
    }

    public ExtendFunction makeExtendFunction(String imageUrl, String name, String code, String url, String action, boolean hasMessage) {
        ClickListener listener = mWebListener;
        if ("dingyueHandler".equals(action)) {
            listener = new ClickListener() {
                @Override
                public void onClick(ExtendFunction function) {
                    Intent i = new Intent(mContext, ZhuanLanListActivity.class);
                    mContext.startActivity(i);
                }
            };

        } else if ("moreHanlder".equals(action)) {
            listener = new BaseClickListener() {
                @Override
                public void onClick(ExtendFunction function) {
                    super.onClick(function);
                    Intent i = new Intent(mContext, ExtendFunctionActivity.class);
                    mContext.startActivity(i);
                }
            };
        } else if ("questionHandler".equals(action)) {
            listener = new BaseClickListener() {
                @Override
                public void onClick(ExtendFunction function) {
                    super.onClick(function);
                    Intent i = new Intent(mContext, QuestionsActivity.class);
                    mContext.startActivity(i);
                }
            };
        } else if ("jpkHandler".equals(action)) {
            listener = new BaseClickListener() {
                @Override
                public void onClick(ExtendFunction function) {
                    super.onClick(function);
                    Intent i = new Intent(mContext, ZhuanLanListActivity.class)
                            .putExtra(ZhuanLanListFragment.EXTRA_TYPE, ZhuanLanListFragment.TYPE_JPK);
                    mContext.startActivity(i);
                }
            };
        }

        ExtendFunction func = new ExtendFunction(imageUrl, name, code, url, listener, hasMessage);
        return func;
    }

    private ExtendFunctionManager(ExtendFunctoinVariableInfoManager messageManager, int showMaxRow, final Context context, boolean isNeedMore, String type) {
        this.mMessageManager = messageManager;
        mShowMaxRow = showMaxRow;
        mContext = context;
        mWebListener = new WebClickListener();
        mNotSupportListener = new NotSupportClickListener();

        this.mIsNeedMore = isNeedMore;
        this.mShowMaxRow = showMaxRow;
        this.type = type;

    }


    public List<ExtendFunction> getFunctions() {
        return functions;
    }

    public int getRowCount() {
        int rows = (getFunctionCount() + (itemCountEachRow - 1)) / itemCountEachRow;
        int result = rows > mShowMaxRow ? mShowMaxRow : rows;
        LogHelper.d(TAG, "rowCount = " + result);
        return result;
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
            height = 310;
        } else if (screenWidth <= 480) {
            LogHelper.d(TAG, "height is 100");
            height = 95;
        } else if (screenWidth <= 768) {
            height = 190;
        }
        //LogHelper.d(TAG, "each row height in pixel = " + height);
        return (int)(height * 0.79);
    }

    public ExtendFunctionCell getCell(int row, boolean isNeedMore) {
        ExtendFunctionCell cell = new ExtendFunctionCell((Activity) mContext);
        int startIndex = row * itemCountEachRow;

        for(int i = 0; i < itemCountEachRow; i++) {
            int index = startIndex + i;
            if (index >= functions.size() ) {
                break;
            }
            ExtendFunction function = functions.get(index);

            if (!isNeedMore && "f_more".equals(function.code) ) {
                continue;
            }
            cell.addFunction(function);
        }
        cell.setHeight(getHeight());
        cell.setFunctionManager(this);
        return cell;
    }


    public ViewGroup createSubView(final ExtendFunction function) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        int width = Utils.getScreenWidth(mContext);
        ViewGroup.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // Changes the height and width to the specified *pixels*
        int cellHeight = getHeight();
        params.width = width / itemCountEachRow;
        params.height = cellHeight;

        layout.setLayoutParams(params);

        ViewGroup.LayoutParams imageParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ImageView imageView = new ImageView(mContext);


        imageParams.width = (int)(width / itemCountEachRow * 0.7 * 0.70);
        imageParams.height = (int)(width / itemCountEachRow * 0.7  * 0.70);
        imageView.setPadding(0, 32, 0, 0);

        imageView.setLayoutParams(imageParams);

        makeImage(imageView, function);
        layout.addView(imageView);


        ViewGroup.LayoutParams textParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        textParams.width = width / itemCountEachRow;

        TextView textView = new TextView(mContext);
        textView.setLayoutParams(textParams);
        textView.setText(function.getName());
        textView.setWidth(width / itemCountEachRow);
        textView.setTextSize(11);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, -20, 0, 0);

        layout.addView(textView);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function.listener.onClick(function);
            }
        });

        return layout;
    }

    private class GetImageTask extends AsyncTask<Object, Void, Bitmap> {
        private ExtendFunction function = null;
        private ImageView imageView = null;

        @Override
        protected Bitmap doInBackground(Object...params) {
            this.function = (ExtendFunction) params[0];
            this.imageView = (ImageView) params[1];
            Bitmap myBitmap = null;
            String url = function.getImageUrl();
            try {

                myBitmap = Glide.with(mContext)
                        .load(url)
                        .asBitmap()
                        .centerCrop()
                        .into(180, 180)
                        .get();

                if (myBitmap != null) {
                    ExtendFunctionImageDao.getInstance(mContext).saveOrUpdate(url, myBitmap);
                }
            } catch (Exception ex) {
                LogHelper.d(TAG, ex);
                return null;
            }
            return myBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Bitmap bitmap2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.f_1).copy(Bitmap.Config.ARGB_8888, true);
            if (bitmap == null) {
                LogHelper.d(TAG, " image = " + function.image);
                Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), function.image).copy(Bitmap.Config.ARGB_8888, true);
                makeImage0(bitmap1, bitmap2, function, imageView);
                return;
            }

            makeImage0(bitmap, bitmap2, function, imageView);
        }
    }

    public void makeImage(ImageView image, ExtendFunction func) {
        //LogHelper.d(TAG, func.name, " has message: ", func.hasMessage());
        // 防止出现Immutable bitmap passed to Canvas constructor错误
        String imageUrl = func.getImageUrl();
        //LogHelper.d(TAG, func.code + " - imageUrl: " + imageUrl);

        Bitmap numberBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.f_1).copy(Bitmap.Config.ARGB_8888, true);
        //检查这个连接是否有效，并且这个链接的图片是否保存在磁盘中。如果在缓存中，直接中磁盘中获取图片，否则从网络中下载。

        if (imageUrl != null && !"".equals(imageUrl)) {
            Bitmap bitmap = ExtendFunctionImageDao.getInstance(mContext).get(imageUrl);
            if (bitmap == null) {
                new GetImageTask().execute(func, image);
            } else {
                makeImage0(bitmap, numberBitmap, func, image);
            }
            new GetImageTask().execute(func, image);
            return;
        }

        Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.placeholder).copy(Bitmap.Config.ARGB_8888, true);
        makeImage0(bitmap1, numberBitmap, func, image);
    }

    private void makeImage0(Bitmap bitmap1, Bitmap bitmap2, ExtendFunction func, ImageView image) {


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
            //mMessageManager.clearMessage(functions[0].getCode());
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
                    .putExtra(WebBrowserActivity.EXTRA_TITLE, function.getName())
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


    public static class ExtendFunction {
        private int image = R.drawable.placeholder;
        private String imageUrl = "";
        private String name = "";
        private String code = "";
        private String url = "";
        private ClickListener listener;
        private boolean hasMessage;
        private ExtendFunctoinVariableInfoManager mMessageManager;

        public ExtendFunction(int image, String name, String code, String url, ClickListener listener,
                              ExtendFunctoinVariableInfoManager messageManager ) {
            this.image = image;
            this.name = name;
            this.code = code;
            this.url = url;
            this.listener = listener;
            this.mMessageManager = messageManager;
        }

        public ExtendFunction(String imageUrl, String name, String code, String url, ClickListener listener,
                              boolean hasMessage ) {
            this.imageUrl = imageUrl;
            this.name = name;
            this.code = code;
            this.url = url;
            this.listener = listener;
            //this.mMessageManager = messageManager;
            this.hasMessage = hasMessage;
        }

        public boolean hasMessage() {
            return hasMessage;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return this.name;
        }

        public String getImageUrl() {
            return imageUrl;
        }



    }

}
