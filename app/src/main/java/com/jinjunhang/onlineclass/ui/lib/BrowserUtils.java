package com.jinjunhang.onlineclass.ui.lib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class BrowserUtils {

    public static boolean handleAlipayProtocol(String url, Context context) {
        // ------  对alipays:相关的scheme处理 -------
        if(url.startsWith("alipays:") || url.startsWith("alipay")) {
            try {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            } catch (Exception e) {
                new AlertDialog.Builder(context)
                        .setMessage("未检测到支付宝客户端，请安装后重试。")
                        .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                context.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                            }
                        }).setNegativeButton("取消", null).show();
            }
            return true;
        }
        // ------- 处理结束 -------

        return false;
    }

    public static  boolean handlePhoneCallProtocol(String url, Context context) {
        if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse(url));
            context.startActivity(intent);
            return true;
        }

        return false;
    }

}
