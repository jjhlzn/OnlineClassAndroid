package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.service.GetAdsRequest;
import com.jinjunhang.onlineclass.service.GetAdsResponse;

/**
 * Created by lzn on 16/6/20.
 */
public class AdvImageCell extends BaseListViewCell {

    private SliderLayout sliderShow;

    public AdvImageCell(Activity activity) {
        super(activity);
    }

    public SliderLayout getSliderShow() {
        return sliderShow;
    }

    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.adv_image_slider, null);
        sliderShow = (SliderLayout)view.findViewById(R.id.adv_slider);
        new GetAdvImageTask().execute();
        return (LinearLayout)view.findViewById(R.id.list_item_albumtype_viewgroup);
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

            for (Advertise adv : getAdsResponse.getAdvertises()) {
                DefaultSliderView textSliderView = new DefaultSliderView(mActivity);
                textSliderView.image(adv.getImageUrl());
                sliderShow.addSlider(textSliderView);
            }

        }
    }
}
