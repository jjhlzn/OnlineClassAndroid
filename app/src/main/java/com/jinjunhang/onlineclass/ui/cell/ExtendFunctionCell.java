package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/20.
 */
public class ExtendFunctionCell extends BaseListViewCell {

    private static final String TAG = LogHelper.makeLogTag(ExtendFunctionCell.class);

    private ViewGroup mView;
    private int mHeight;
    private List<ExtendFunctionManager.ExtendFunction> functionList = new ArrayList<>();
    private ExtendFunctionManager mFunctionManager;

    public void addFunction(ExtendFunctionManager.ExtendFunction func) {
        functionList.add(func);
    }



    public ExtendFunctionCell(Activity activity) {
        super(activity);
    }

    public void setView(ViewGroup view) {
        mView = view;
    }

    @Override
    public boolean isSection() {
        return false;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public void setFunctionManager(ExtendFunctionManager functionManager) {
        this.mFunctionManager = functionManager;
    }

    @Override
    public ViewGroup getView() {
        //LogHelper.d(TAG, "ExtendFunctionCell.getView() called");
        LinearLayout layout = new LinearLayout(mActivity);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ViewGroup.LayoutParams params =  new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        // Changes the height and width to the specified *pixels*
        params.height = mHeight;
        layout.setLayoutParams(params);
        layout.setBackgroundColor(Color.WHITE );

        for (ExtendFunctionManager.ExtendFunction func : functionList) {
            layout.addView(mFunctionManager.createSubView(func));
        }
        layout.setEnabled(false);
        layout.setOnClickListener(null);
        return layout;
    }
}
