package com.jinjunhang.onlineclass.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.ui.activity.user.LoginActivity;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lzn on 16/6/19.
 */
public class LaunchActivity extends Activity {

    private LoginUserDao mLoginUserDao;

    private static final String APP_ID = "wx73653b5260b24787";
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_launch);

        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);

        mLoginUserDao = LoginUserDao.getInstance(this);


        Intent i;
        if (mLoginUserDao.get() == null) {
            i = new Intent(this, LoginActivity.class);
        } else {
            i = new Intent(this, MainActivity.class);
        }

        //shareText();
        startActivity(i);
    }

    // 文本分享
    private void shareText() {
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
        req.scene = true ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

        // 调用api接口发送数据到微信
        api.sendReq(req);
    }
}
