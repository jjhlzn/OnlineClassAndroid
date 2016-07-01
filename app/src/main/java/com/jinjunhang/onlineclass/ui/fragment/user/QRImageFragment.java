package com.jinjunhang.onlineclass.ui.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

/**
 * Created by jjh on 2016-7-1.
 */
public class QRImageFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_qrimage, container, false);

        ImageView qrImage = (ImageView) v.findViewById(R.id.qr_image);
        Glide.with(getActivity()).load(LoginUserDao.getInstance(getActivity()).get().getCodeImageUrl()).into(qrImage);

        return v;
    }
}
