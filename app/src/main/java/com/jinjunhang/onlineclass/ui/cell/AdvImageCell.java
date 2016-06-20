package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.jinjunhang.onlineclass.R;

/**
 * Created by lzn on 16/6/20.
 */
public class AdvImageCell extends BaseListViewCell {

    private SliderLayout sliderShow;

    public AdvImageCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.adv_image_slider, null);

        sliderShow = (SliderLayout)view.findViewById(R.id.adv_slider);

        DefaultSliderView textSliderView = new DefaultSliderView(mActivity);
        textSliderView.image("http://image.tianjimedia.com/uploadImages/2015/129/56/J63MI042Z4P8.jpg");

        sliderShow.addSlider(textSliderView);

        return (LinearLayout)view.findViewById(R.id.list_item_albumtype_viewgroup);
    }

    @Override
    public void release() {
        super.release();
        if (sliderShow != null) {
            sliderShow.stopAutoCycle();
        }
    }
}
