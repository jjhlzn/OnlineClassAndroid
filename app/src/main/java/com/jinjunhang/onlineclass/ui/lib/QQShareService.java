package com.jinjunhang.onlineclass.ui.lib;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.db.QrImageDao;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jinjunhang on 17/1/13.
 */

public class QQShareService {
    private static final String TAG = LogHelper.makeLogTag(QQShareService.class);

    private Tencent mTencent;
    private Activity mActivity;

    public static final String QQ_APP_ID = "1105870136";

    public QQShareService(Activity activity) {
        mTencent = Tencent.createInstance(QQ_APP_ID, CustomApplication.get());
        this.mActivity = activity;
    }

    public void shareToFriends() {
        share(false);
    }

    public void shareToQzone() {
        share(true);
    }

    private void share(boolean isQzone) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "扫一扫下载安装【巨方助手】，即可免费在线学习、提额、办卡、贷款！");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "扫一扫下载安装【巨方助手】");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
                ServiceLinkManager.ShareQrImageUrl() + "?userid=" + LoginUserDao.getInstance(CustomApplication.get()).get().getUserName());
        ArrayList<String> imageUrls = new ArrayList<>();

        String imageUrl = QrImageDao.getInstance(mActivity).getCodeImageUrl();
        imageUrls.add(imageUrl);
        if (isQzone) {
            params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL,
                    imageUrls );
        } else {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl );
        }

        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "巨方助手");

        if (isQzone) {
            mTencent.shareToQzone(mActivity, params, new BaseUiListener());
        } else {
            mTencent.shareToQQ(mActivity, params, new BaseUiListener());
        }
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            //V2.0版本，参数类型由JSONObject 改成了Object,具体类型参考api文档
            //mBaseMessageText.setText("onComplete:");
            //doComplete(response);
        }
        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            LogHelper.e(TAG, "onError:", "code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }
        @Override
        public void onCancel() {
            //showResult("onCancel", "");
        }
    }

}
