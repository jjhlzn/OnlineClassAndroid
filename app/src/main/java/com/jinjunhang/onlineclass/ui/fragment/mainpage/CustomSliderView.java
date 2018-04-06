package com.jinjunhang.onlineclass.ui.fragment.mainpage;

/**
 * Created by jinjunhang on 2018/4/6.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.slider.library.R.id;
import com.daimajia.slider.library.R.layout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

public class CustomSliderView extends BaseSliderView {

    public CustomSliderView(Context context) {
        super(context);
    }

    public View getView() {

        View v = LayoutInflater.from(this.getContext()).inflate(layout.render_type_text, null);
        ImageView target = (ImageView) v.findViewById(id.daimajia_slider_image);
        LinearLayout frame = (LinearLayout) v.findViewById(id.description_layout);
        frame.setBackgroundColor(Color.TRANSPARENT);

//      if you need description
//      description.setText(this.getDescription());

        this.bindEventAndShow(v, target);

        return v;
    }
}