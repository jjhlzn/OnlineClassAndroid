package com.jinjunhang.onlineclass.ui.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

/**
 * Created by jinjunhang on 2018/4/7.
 */

public class ShareImageFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.share_image_item, null);

        return v;
    }
}
