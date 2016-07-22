package com.jinjunhang.onlineclass.ui.lib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.db.QrImageDao;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.player.utils.LogHelper;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by jjh on 2016-7-15.
 */
public class WeixinShareManager {
    private final static String TAG = LogHelper.makeLogTag(WeixinShareManager.class);

    protected AppCompatActivity mActivity;
    protected View v;
    protected IWXAPI api;

    protected QrImageDao qrImageDao;

    public WeixinShareManager(AppCompatActivity activity, View v) {
        this.mActivity = activity;
        this.v = v;
        api = WXAPIFactory.createWXAPI(mActivity, Utils.WEIXIN_SHERE_APP_ID, true);
        api.registerApp(Utils.WEIXIN_SHERE_APP_ID);
        qrImageDao = QrImageDao.getInstance(activity);
        setup();
    }

    protected void  setup() {
        View customView = mActivity.getSupportActionBar().getCustomView();
        ImageButton rightButton = (ImageButton) customView.findViewById(R.id.actionbar_right_button);
        rightButton.setImageResource(R.drawable.share);
        rightButton.setVisibility(View.VISIBLE);

        final ImageButton shareButton = (ImageButton) mActivity.getSupportActionBar().getCustomView().findViewById(R.id.actionbar_right_button);
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
    }

    protected void shareUrl(boolean isPengyouquan) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = ServiceLinkManager.ShareQrImageUrl() + "?userid=" + LoginUserDao.getInstance(CustomApplication.get()).get().getUserName();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "扫一扫下载安装【巨方助手】，即可免费在线学习、提额、办卡、贷款！";
        msg.description = "description";
        Bitmap thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.me_qrcode);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isPengyouquan ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    protected String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    // 文本分享
    protected void shareText(boolean isPengyouquan) {
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

    protected static final int THUMB_SIZE = 150;

    // 文本分享
    protected void shareImage(boolean isPengyouquan) {
        Bitmap bmp = qrImageDao.get();
        if (bmp == null) {
            Toast.makeText(mActivity, "bmp is null", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(mActivity, "bmp is  not null", Toast.LENGTH_SHORT).show();
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

        Toast.makeText(mActivity, "after send api", Toast.LENGTH_SHORT).show();

    }


}
