package com.jinjunhang.onlineclass.ui.fragment.search;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.service.GetHotSearchWordsRequest;
import com.jinjunhang.onlineclass.service.GetHotSearchWordsResponse;
import com.jinjunhang.onlineclass.service.GetMainPageAdsRequest;
import com.jinjunhang.onlineclass.service.GetMainPageAdsResponse;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 2018/4/4.
 */

public class SearchPage {

    private final static String TAG = LogHelper.makeLogTag(SearchPage.class);

    public View view;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private ViewGroup keywordsContainer;
    private List<String> mkeywords;
    private SearchFragment mSearchFragment;

    public SearchPage(SearchFragment searchFragment, FragmentActivity activity, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mSearchFragment = searchFragment;
        this.mActivity = activity;
        this.mInflater = inflater;
        view = inflater.inflate(R.layout.activity_fragment_search_frag1, container, false);
        keywordsContainer = (ViewGroup)view.findViewById(R.id.keywords_container);
        mkeywords = new ArrayList<>();
        //makeKeywordButtons();
        new GetSearchWordsTask().execute();
    }


    private  boolean isNeedNewRow = false;
    private  int rowLength = 0;

    private void makeKeywordButtons() {
        isNeedNewRow = false;
        rowLength = 0;

        int screenWidth = Utils.getScreenWidth(mActivity);

        LinearLayout row = makeRow();

        keywordsContainer.addView(row);

        for (String keyword : mkeywords) {

            if (isNeedNewRow) {
                row = makeRow();
                keywordsContainer.addView(row);
                isNeedNewRow = false;
            }

            View buttonView = createButton(keyword);
            row.addView(buttonView);

            if (screenWidth - rowLength < 144) {
                isNeedNewRow = true;
                rowLength = 0;
            }
        }
    }

    private LinearLayout makeRow() {
        ViewGroup.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout row = new LinearLayout(mActivity);
        row.setLayoutParams(params);
        row.setOrientation(LinearLayout.HORIZONTAL);
        //row.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        row.getLayoutParams().height = Utils.dip2px(mActivity, 50);
        return row;
    }

    public View createButton(String keyword) {
        ViewGroup.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LogHelper.d(TAG, keyword + ".length = " + keyword.length());

        if (keyword.length() <= 2)
            params.width = Utils.dip2px(mActivity, 55);
        else
            params.width = Utils.dip2px(mActivity, 62);

        params.height = Utils.dip2px(mActivity, 30);

        //keyword_button.xml的高度和宽度控制没有用，需要params设置
        View v = mInflater.inflate(R.layout.keyword_button, null);
        final Button button = (Button)v.findViewById(R.id.keyword_btn);
        button.setText(keyword);
        button.setLayoutParams(params);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchFragment.keywordButtonClick(button.getText().toString());
            }
        });

        ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        //if (rowLength != 0)
        margins.leftMargin = Utils.dip2px(mActivity, 16);

        Log.d(TAG, "button.width = " + button.getLayoutParams().width);

        rowLength += button.getLayoutParams().width + Utils.dip2px(mActivity, 16);
        return v;
    }

    private class GetSearchWordsTask extends AsyncTask<Void, Void, GetHotSearchWordsResponse> {

        @Override
        protected GetHotSearchWordsResponse doInBackground(Void... voids) {
            GetHotSearchWordsRequest request = new GetHotSearchWordsRequest();
            return new BasicService().sendRequest(request);
        }


        @Override
        protected void onPostExecute(GetHotSearchWordsResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()) {
                LogHelper.e(TAG, response.getErrorMessage());
                return;
            }

            mkeywords = response.getKeywords();
            makeKeywordButtons();
        }
    }
}
