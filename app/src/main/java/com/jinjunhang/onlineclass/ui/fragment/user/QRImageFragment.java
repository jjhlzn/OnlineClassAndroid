package com.jinjunhang.onlineclass.ui.fragment.user;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.db.QrImageDao;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.ShareManager;
import com.jinjunhang.onlineclass.ui.lib.ShareManager2;
import com.jinjunhang.framework.lib.BitmapHelper;
import com.jinjunhang.framework.lib.LogHelper;

import java.io.IOException;
import java.util.Date;

/**
 * Created by jjh on 2016-7-1.
 */
public class QRImageFragment extends BaseFragment {

    private static final String TAG = LogHelper.makeLogTag(QRImageFragment.class);

    private QrImageDao qrImageDao;
    private ShareManager mWeixinShareManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_qrimage, container, false);
        mWeixinShareManager = new ShareManager2((AppCompatActivity) getActivity(), v);
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

        return v;
    }




}
