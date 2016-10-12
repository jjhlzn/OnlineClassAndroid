package com.jinjunhang.onlineclass;

/**
 * Created by lzn on 2016/10/8.
 */

import com.jinjunhang.framework.lib.Utils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);

        // ����appע�ᵽ΢��
        msgApi.registerApp(Utils.WEIXIN_SHERE_APP_ID);
    }
}
