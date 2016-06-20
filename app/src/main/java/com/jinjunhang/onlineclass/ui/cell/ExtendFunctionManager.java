package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
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

    private List<ExtendFunction> functions = new ArrayList<>();

    public ExtendFunctionManager(Context context) {
        this(100, context);
    }

    public ExtendFunctionManager(int showMaxRow, Context context) {
        mShowMaxRow = showMaxRow;
        mContext = context;
        functions.add(new ExtendFunction(R.drawable.commoncard, "去刷卡1", "", true));
        functions.add(new ExtendFunction(R.drawable.upicon, "去刷卡2", "", true));
        functions.add(new ExtendFunction(R.drawable.visacard, "去刷卡3", "", true));
        functions.add(new ExtendFunction(R.drawable.cardmanager, "去刷卡4", "", true));
        functions.add(new ExtendFunction(R.drawable.mmcsearch, "去刷卡5", "", true));
        functions.add(new ExtendFunction(R.drawable.shopcart, "去刷卡6", "", true));
        functions.add(new ExtendFunction(R.drawable.rmb, "去刷卡7", "", true));
        functions.add(new ExtendFunction(R.drawable.dollar, "去刷卡8", "", true));
        functions.add(new ExtendFunction(R.drawable.creditsearch, "去刷卡9", "", true));
    }

    public int getRowCount() {
        int rows = (functions.size() + (itemCountEachRow - 1)) / itemCountEachRow;
        return rows > mShowMaxRow ? mShowMaxRow : rows;
    }


    public ExtendFunctionCell  getCell(int row) {
        ExtendFunctionCell cell = new ExtendFunctionCell((Activity) mContext);

        int startIndex = row * itemCountEachRow;

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ViewGroup.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        // Changes the height and width to the specified *pixels*
        params.height = 200;
        layout.setLayoutParams(params);

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

    private ViewGroup createSubView(ExtendFunction function) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ViewGroup.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // Changes the height and width to the specified *pixels*
        params.width = width / 4;
        layout.setLayoutParams(params);

        ViewGroup.LayoutParams imageParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageParams.width = (int)(width / 4 * 0.5);
        imageParams.height = (int)(width / 4 * 0.5);
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

        return layout;
    }


    class ExtendFunction {
        private int image;
        private String name;
        private String url;
        private boolean isSupport;

        ExtendFunction() {}
        ExtendFunction(int image, String name, String url, boolean isSupport) {
            this.image = image;
            this.name = name;
            this.url = url;
            this.isSupport = isSupport;
        }
        //var action : Selector
    }

}
