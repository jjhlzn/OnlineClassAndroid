package com.jinjunhang.onlineclass.ui.fragment.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.db.QrImageDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;
import com.jinjunhang.player.utils.BitmapHelper;
import com.jinjunhang.player.utils.LogHelper;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * Created by jjh on 2016-7-1.
 */
public class QRImageFragment extends BaseFragment {

    private static final String TAG = LogHelper.makeLogTag(QRImageFragment.class);
    private static final String APP_ID = "wx73653b5260b24787";

    private QrImageDao qrImageDao;
    private IWXAPI api;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_qrimage, container, false);

        qrImageDao = QrImageDao.getInstance(getActivity());
        api = WXAPIFactory.createWXAPI(getActivity(), APP_ID, true);
        api.registerApp(APP_ID);

        final ImageButton shareButton = (ImageButton) ((SingleFragmentActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionbar_right_button);
        final View shareView = v.findViewById(R.id.share_view);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "shareButton clicked");
                shareView.setVisibility(View.VISIBLE);
            }
        });

        Button closeShareViewButton = (Button) v.findViewById(R.id.close_button);
        closeShareViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareView.setVisibility(View.GONE);
            }
        });

        View shareFriendButton = shareView.findViewById(R.id.weixinhaoyou_button);
        shareFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "share friends button Clicked");
                shareUrl(false);
            }
        });

        View pengyouquanButton = shareView.findViewById(R.id.pengyouquan_button);
        pengyouquanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "share pemgyouquan button Clicked");
                shareUrl(true);
            }
        });

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

    private static final int THUMB_SIZE = 150;
    // 文本分享
    private void shareImage(boolean isPengyouquan) {
        Bitmap bmp = qrImageDao.get();
        if (bmp == null) {
            Toast.makeText(getActivity(), "bmp is null", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getActivity(), "bmp is  not null", Toast.LENGTH_SHORT).show();
        WXImageObject imgObj =new WXImageObject(bmp);


        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.description = "二维码";
        /*
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true); */
       // msg.description = "二维码";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = isPengyouquan ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

        Toast.makeText(getActivity(), "after send api", Toast.LENGTH_SHORT).show();

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    private void shareUrl(boolean isPengyouquan) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = ServiceLinkManager.ShareQrImageUrl() + "?userid=" + LoginUserDao.getInstance(CustomApplication.get()).get().getUserName();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "扫一扫下载安装【巨方助手】，即可免费在线学习、提额、办卡、贷款！";
        msg.description = "description";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.me_qrcode);
        msg.thumbData =Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isPengyouquan ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }


    // 文本分享
    private void shareText(boolean isPengyouquan) {
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = "hallo";

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        // msg.title = "Will be ignored";
        msg.description = "hallo";

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "transaction"+System.currentTimeMillis(); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene =  isPengyouquan ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

        // 调用api接口发送数据到微信
        api.sendReq(req);
    }
}
