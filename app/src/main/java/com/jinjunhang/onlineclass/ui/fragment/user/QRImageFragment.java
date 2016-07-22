package com.jinjunhang.onlineclass.ui.fragment.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.db.QrImageDao;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;
import com.jinjunhang.onlineclass.ui.lib.WeixinShareManager;
import com.jinjunhang.onlineclass.ui.lib.WeixinShareManager2;
import com.jinjunhang.player.utils.BitmapHelper;
import com.jinjunhang.player.utils.LogHelper;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Created by jjh on 2016-7-1.
 */
public class QRImageFragment extends BaseFragment {

    private static final String TAG = LogHelper.makeLogTag(QRImageFragment.class);

    private QrImageDao qrImageDao;
    private WeixinShareManager mWeixinShareManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_qrimage, container, false);
        mWeixinShareManager = new WeixinShareManager2((AppCompatActivity) getActivity(), v);
        qrImageDao = QrImageDao.getInstance(getActivity());

        final ImageView qrImage = (ImageView) v.findViewById(R.id.qr_image);
        if (qrImageDao.get() != null) {
            qrImage.setImageBitmap(qrImageDao.get());
        }

        final String qrImageUrl = LoginUserDao.getInstance(getActivity()).get().getCodeImageUrl();


        (new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    Bitmap bitmap =  BitmapHelper.fetchAndRescaleBitmap(qrImageUrl+"?"+ new Date().getTime(), 300, 300);
                    return  bitmap;
                } catch (IOException ex) {
                    LogHelper.e(TAG, ex);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                qrImageDao.saveOrUpdate(bitmap);
                qrImage.setImageBitmap(bitmap);
            }
        }).execute();


        /*
        Glide.with(getActivity()).load(LoginUserDao.getInstance(getActivity()).get().getCodeImageUrl()).asBitmap().into(new BitmapImageViewTarget(qrImage) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> animation) {
                qrImage.setImageBitmap(resource);
                if (qrImageDao.get() == null) {
                    qrImageDao.saveOrUpdate(resource);
                }
                LogHelper.d(TAG, "image is ready");
            }
            @Override
            protected void setResource(Bitmap resource) {
            }
        }); */

        return v;
    }




}
