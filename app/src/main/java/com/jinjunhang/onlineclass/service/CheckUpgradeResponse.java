package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jjh on 2016-7-6.
 */
public class CheckUpgradeResponse extends ServerResponse {

    /*
    newestVersion: '1.0.1',   //最新版本号
    isNeedUpgrade: true,  //是否需要更新
    upgradeType: 'optional', //升级类型 optional表示可选升级，force表示强制升级
    upgradeUrl: 'http://www.baidu.com'   //升级url
    */

    private String mNewestVersion;
    private boolean mIsNeedUpgrade;
    private  String mUpgradeType;
    private String mUpgradeUrl;
    private String mUpgradeFileUrl;

    public String getNewestVersion() {
        return mNewestVersion;
    }

    public boolean isNeedUpgrade() {
        return mIsNeedUpgrade;
    }

    public String getUpgradeType() {
        return mUpgradeType;
    }

    public String getUpgradeUrl() {
        return mUpgradeUrl;
    }

    public void setNewestVersion(String newestVersion) {
        mNewestVersion = newestVersion;
    }

    public void setNeedUpgrade(boolean needUpgrade) {
        mIsNeedUpgrade = needUpgrade;
    }

    public void setUpgradeType(String upgradeType) {
        mUpgradeType = upgradeType;
    }

    public void setUpgradeUrl(String upgradeUrl) {
        mUpgradeUrl = upgradeUrl;
    }

    public String getUpgradeFileUrl() {
        return mUpgradeFileUrl;
    }

    public void setUpgradeFileUrl(String upgradeFileUrl) {
        mUpgradeFileUrl = upgradeFileUrl;
    }

    @Override
    public void parse(ServerRequest request, JSONObject json) throws JSONException {
        mNewestVersion = json.getString("newestVersion");
        mIsNeedUpgrade = json.getBoolean("isNeedUpgrade");
        mUpgradeType = json.getString("upgradeType");
        mUpgradeUrl = json.getString("upgradeUrl");
        mUpgradeFileUrl = json.getString("upgradeFileUrl");
    }
}
