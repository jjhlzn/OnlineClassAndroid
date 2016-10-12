package com.jinjunhang.onlineclass.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.player.utils.LogHelper;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lzn on 2016/10/8.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static String TAG = LogHelper.makeLogTag(WXPayEntryActivity.class);

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_view);
        api = WXAPIFactory.createWXAPI(this, "wx73653b5260b24787");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    public void onReq(BaseReq req) {
    }

    public void onResp(BaseResp resp) {
        LogHelper.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        Utils.showMessage(this, "onPayFinish, errCode = " + resp.errCode);

        String message = "";
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    message = "支付成功";
                    break;
                default:
                    message = "支付失败";
            }
            LogHelper.d(TAG, "message = " + message);
            Utils.showMessage(this, message);
        }
    }

}
