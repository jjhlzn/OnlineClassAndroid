package com.jinjunhang.onlineclass.ui.lib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

/**
 * Created by jinjunhang on 16/12/30.
 */

public class WeiboShareService implements IWeiboHandler.Response {
    private static final String TAG = LogHelper.makeLogTag(WeiboShareService.class);

    private static final String APP_KEY = "901768017";
    private static final String REDIRECT_URL = "";
    private static final String SCOPE = "";

    private Activity mActivity;

    /** 微博微博分享接口实例 */
    private IWeiboShareAPI mWeiboShareAPI = null;

    private ShareManager mShareManager;

    public WeiboShareService(Activity activity, ShareManager shareManager) {
        mShareManager = shareManager;
        this.mActivity = activity;
        this.mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mActivity, APP_KEY);

        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        boolean result = mWeiboShareAPI.registerApp();
        LogHelper.d(TAG, "webo.registerApp(): ", result);

        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        //if (savedInstanceState != null) {
        mWeiboShareAPI.handleWeiboResponse(mActivity.getIntent(), this);
        //}
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        LogHelper.d(TAG, baseResponse);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同
     * 时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     */
    public void share() {

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();


        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        weiboMessage.mediaObject = getWebpageObj();

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 如果安装了微博： 发送请求消息到微博，唤起微博分享界面；如果没有，调用网页进行分享
        boolean weiboInstalled = mWeiboShareAPI.isWeiboAppInstalled();
        LogHelper.d(TAG, "is weibo installed: ", weiboInstalled);
        if (weiboInstalled) {
            mWeiboShareAPI.sendRequest(mActivity, request);
        } else {
            AuthInfo authInfo = new AuthInfo(mActivity, APP_KEY, REDIRECT_URL, SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(mActivity.getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }

            mWeiboShareAPI.sendRequest(mActivity, request, authInfo, token, new WeiboAuthListener() {

                @Override
                public void onWeiboException( WeiboException arg0 ) {
                    LogHelper.d(TAG, arg0);
                }

                @Override
                public void onComplete( Bundle bundle ) {
                    // TODO Auto-generated method stub
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(mActivity.getApplicationContext(), newToken);
                    Toast.makeText(mActivity.getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }



    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = mShareManager.getShareTitle();
        mediaObject.description = "";

        if (mShareManager.isUseQrCodeImage()) {
            Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.me_qrcode);
            // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
            mediaObject.setThumbImage(bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.log);
            // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
            mediaObject.setThumbImage(bitmap);
        }

        mediaObject.actionUrl = mShareManager.getShareUrl();
        mediaObject.defaultText = "";
        return mediaObject;
    }




}
