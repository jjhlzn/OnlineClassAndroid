package com.jinjunhang.onlineclass.ui.lib;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.wx.Util;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.db.QrImageDao;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.framework.lib.LogHelper;
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
public class ShareManager {
    private final static String TAG = LogHelper.makeLogTag(ShareManager.class);

    protected AppCompatActivity mActivity;
    protected View v;
    protected IWXAPI api;

    protected QrImageDao qrImageDao;

    protected WeiboShareService mWeiboShareService;
    protected QQShareService mQQShareService;

    private String mShareTitle;
    private String mShareUrl;
    private String mDescription;
    private boolean mIsUseQrCodeImage;

    private boolean mIsShareButtonVisible = true;

    public void setShareUrl(String shareUrl) {
        if (shareUrl == null || shareUrl.isEmpty())
            return;
        mShareUrl = shareUrl;
    }

    public void setShareTitle(String shareTitle) {
        if (shareTitle == null || shareTitle.isEmpty())
            return;
        mShareTitle = shareTitle;
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            return;
        }
        mDescription = description;
    }

    public void setUseQrCodeImage(boolean useQrCodeImage) {
        mIsUseQrCodeImage = useQrCodeImage;
    }



    public String getShareTitle() {
        return mShareTitle;
    }

    public String getShareUrl() {
        return mShareUrl;
    }

    public String getDescription() { return mDescription; }

    public boolean isUseQrCodeImage() {
        return mIsUseQrCodeImage;
    }

    public void resetSetting() {
        mShareTitle = "扫一扫下载安装【巨方助手】，即可免费在线学习、提额、办卡、贷款！";
        mShareUrl = ServiceLinkManager.ShareQrImageUrl() +
                "?userid=" + LoginUserDao.getInstance(CustomApplication.get()).get().getUserName();
        mDescription = "巨方助手";
    }

    public ShareManager(AppCompatActivity activity, View v) {
        mShareTitle = "扫一扫下载安装【巨方助手】，即可免费在线学习、提额、办卡、贷款！";
        mShareUrl = ServiceLinkManager.ShareQrImageUrl() +
                "?userid=" + LoginUserDao.getInstance(CustomApplication.get()).get().getUserName();
        mDescription = "巨方助手";
        this.mActivity = activity;
        this.v = v;
        api = WXAPIFactory.createWXAPI(mActivity, Utils.WEIXIN_SHERE_APP_ID, true);
        api.registerApp(Utils.WEIXIN_SHERE_APP_ID);
        qrImageDao = QrImageDao.getInstance(activity);
        this.mWeiboShareService = new WeiboShareService(activity, this);
        mQQShareService = new QQShareService(activity, this);
        setup();
    }

    private void setLowerLayer(final  boolean status) {
        View lowerLayer = v.findViewById(R.id.listView);
        if (lowerLayer != null) {
            lowerLayer.setEnabled(status);
        }
        WebView webViewLayer = (WebView)v.findViewById(R.id.webview);
        if (webViewLayer != null) {
            webViewLayer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return !status;
                }
            });
            LogHelper.d(TAG, "set webviewlayer to: ", status);
        }
        View bottom_comment_tip = v.findViewById(R.id.bottom_comment_tip);
        if (bottom_comment_tip != null) {
            bottom_comment_tip.setEnabled(status);
        }

    }

    public void setShareButtonVisible(boolean isVisible) {
        final ImageButton shareButton = (ImageButton) mActivity.getSupportActionBar().getCustomView().findViewById(R.id.actionbar_right_button);
        this.mIsShareButtonVisible = isVisible;

        if (!mIsShareButtonVisible)
            shareButton.setVisibility(View.INVISIBLE);
        else
            shareButton.setVisibility(View.VISIBLE);
    }

    protected void  setup() {
        View customView = mActivity.getSupportActionBar().getCustomView();
        ImageButton rightButton = (ImageButton) customView.findViewById(R.id.actionbar_right_button);
        rightButton.setImageResource(R.drawable.share);
        rightButton.setVisibility(View.VISIBLE);

        final ImageButton shareButton = (ImageButton) mActivity.getSupportActionBar().getCustomView().findViewById(R.id.actionbar_right_button);

        if (!mIsShareButtonVisible)
            shareButton.setVisibility(View.INVISIBLE);

        final View shareView = v.findViewById(R.id.share_view);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "shareButton clicked");
                shareView.setVisibility(View.VISIBLE);
                setLowerLayer(false);
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

        View weiboButton = shareView.findViewById(R.id.weibo_button);
        weiboButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "share weibo button Clicked");
                //mWeiboShareService.share();
                mWeiboShareService.share();
            }
        });

        View qqFriendsButton = shareView.findViewById(R.id.qqFriends_button);
        qqFriendsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mQQShareService.shareToFriends();
            }
        });

        View qzoneButton = shareView.findViewById(R.id.qzone_button);
        qzoneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mQQShareService.shareToQzone();
            }
        });

        View copyLinkButton = shareView.findViewById(R.id.copylink_button);
        copyLinkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                String url = mShareUrl;
                ClipData clip = ClipData.newPlainText("Text Label", url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });

        Button closeButton = (Button) shareView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareView.setVisibility(View.INVISIBLE);
                setLowerLayer(true);
            }
        });
    }

    protected void shareUrl(boolean isPengyouquan) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = mShareUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = mShareTitle;
        msg.description = mDescription;

        if (mIsUseQrCodeImage) {
            Bitmap thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.me_qrcode);
            msg.thumbData = Util.bmpToByteArray(thumb, true);
        } else {
            Bitmap thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.log);
            msg.thumbData = Util.bmpToByteArray(thumb, true);
        }

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

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = isPengyouquan ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

        Toast.makeText(mActivity, "after send api", Toast.LENGTH_SHORT).show();

    }


}
