package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.service.GetAdsRequest;
import com.jinjunhang.onlineclass.service.GetAdsResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;
import com.jinjunhang.framework.lib.LogHelper;

import java.util.List;

/**
 * Created by lzn on 16/6/20.
 */
public class AdvImageCell extends BaseListViewCell {
    private final static String TAG = LogHelper.makeLogTag(AdvImageCell.class);

    private SliderLayout sliderShow;
    private List<Advertise> mAdvertiseList;

    public AdvImageCell(Activity activity) {
        super(activity);
    }

    public SliderLayout getSliderShow() {
        return sliderShow;
    }

    private int getHeight() {
        int actionBarHeight = Utils.px2dip( mActivity, Utils.getActionBarHeight(mActivity) );
        int statusBarHeight = Utils.px2dip(mActivity, Utils.getStatusBarHeight(mActivity));
        int albumTypeListHeight = 76 * 2; //152
        int separatorHeight =  4;

        ExtendFunctionManager manager = new ExtendFunctionManager(mActivity);
        LogHelper.d(TAG, "manager.getRowCount() * manager.getHeight() = " + manager.getRowCount() * manager.getHeight());
        int functionCellListHeight = Utils.px2dip(mActivity, manager.getRowCount() * manager.getHeight());

        int bottomHeight =  Utils.px2dip(mActivity, Utils.BOTTOM_BAR_HEIGHT );

        int screenWith =   Utils.px2dip(mActivity, Utils.getScreenWidth(mActivity));
        int screenHeight =  Utils.px2dip(mActivity,Utils.getScreenHeight(mActivity));
        LogHelper.d(TAG, "screenWidth = " +Utils.getScreenWidth(mActivity) + ", screenHeight = " +Utils.getScreenHeight(mActivity));

        LogHelper.d(TAG, "actionBarHeight = " + actionBarHeight + ", albumTypeListHeight = " + albumTypeListHeight + ", separtorHeight = " + separatorHeight + ", functionCellListHeight = "
           + functionCellListHeight + ", bottomHeight = " + bottomHeight + ", statusBarHeight = " +statusBarHeight);

        int result = screenHeight - actionBarHeight - albumTypeListHeight - separatorHeight - functionCellListHeight - bottomHeight - statusBarHeight;

        int screenWidthInPixcel = Utils.getScreenWidth(mActivity);
        int screenHeightInPixcel = Utils.getScreenHeight(mActivity);

        int delta = 5;
        boolean needConvert = true;
        LogHelper.d(TAG, "10pixcel = "+ Utils.dip2px(mActivity, 10));
        if (screenWidthInPixcel == 1080 && screenHeightInPixcel == 1920) {
            result = 420 + Utils.dip2px(mActivity, 10) + 8;
            needConvert = false;

        } else if (screenWidthInPixcel == 720) {
            result = 280 + Utils.dip2px(mActivity, 10) + 6;
            needConvert = false;
        } else if (screenWidthInPixcel == 1440) {
            result = 920 + Utils.dip2px(mActivity, 10) + 8;
            needConvert = false;
        } else {
            if (screenWidthInPixcel < 720) {
                delta = 10;
            }
            result += delta;
        }

        if (needConvert) {
            LogHelper.d(TAG, "adv height should be " + result + ", " + Utils.dip2px(mActivity, result));
            return Utils.dip2px(mActivity, result);
        } else {
            LogHelper.d(TAG, "adv height should be " + result + ", " + result);
            return result;
        }

    }

    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.adv_image_slider, null);
        sliderShow = (SliderLayout)view.findViewById(R.id.adv_slider);


        ViewGroup.LayoutParams params = sliderShow.getLayoutParams();
        params.width = Utils.getScreenWidth(mActivity);
        params.height = (int)(params.width * 0.3);
        LogHelper.d(TAG, "advHeight = " + params.height);

        if (mAdvertiseList == null) {
            new GetAdvImageTask().execute();
        } else {
            for (final Advertise adv : mAdvertiseList) {
                DefaultSliderView textSliderView = new DefaultSliderView(mActivity);
                textSliderView.image(adv.getImageUrl()).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        Intent i = new Intent(mActivity, WebBrowserActivity.class)
                                .putExtra(WebBrowserActivity.EXTRA_TITLE, adv.getTitle())
                                .putExtra(WebBrowserActivity.EXTRA_URL, adv.getClickUrl());
                        mActivity.startActivity(i);
                    }
                });
                sliderShow.addSlider(textSliderView);
            }
        }

        LinearLayout group = (LinearLayout)view.findViewById(R.id.list_item_albumtype_viewgroup);
        AbsListView.LayoutParams groupParams = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        groupParams.width = Utils.getScreenWidth(mActivity);
        int height = getHeight();
        if (height < params.height) {
            height = params.height + 15;
        }
        groupParams.height = height;

        group.setLayoutParams(groupParams);
        group.setGravity(Gravity.BOTTOM);
        return group;
    }

    @Override
    public void release() {
        super.release();
        if (sliderShow != null) {
            sliderShow.stopAutoCycle();
        }
    }

    private class GetAdvImageTask extends AsyncTask<Void, Void, GetAdsResponse> {
        @Override
        protected GetAdsResponse doInBackground(Void... params) {
            GetAdsRequest req = new GetAdsRequest();
            return new BasicService().sendRequest(req);
        }

        @Override
        protected void onPostExecute(GetAdsResponse getAdsResponse) {
            super.onPostExecute(getAdsResponse);
            SliderLayout sliderShow = getSliderShow();

            mAdvertiseList = getAdsResponse.getAdvertises();
            for (final Advertise adv : getAdsResponse.getAdvertises()) {
                DefaultSliderView textSliderView = new DefaultSliderView(mActivity);
                textSliderView.image(adv.getImageUrl()).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        Intent i = new Intent(mActivity, WebBrowserActivity.class)
                                .putExtra(WebBrowserActivity.EXTRA_TITLE, adv.getTitle())
                                .putExtra(WebBrowserActivity.EXTRA_URL, adv.getClickUrl());
                        mActivity.startActivity(i);
                    }
                });
                sliderShow.addSlider(textSliderView);
            }

        }
    }


}
