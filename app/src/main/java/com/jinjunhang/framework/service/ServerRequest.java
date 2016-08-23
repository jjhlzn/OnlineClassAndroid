package com.jinjunhang.framework.service;

import android.os.Build;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.jinjunhang.onlineclass.BuildConfig;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lzn on 16/6/10.
 */
public abstract class ServerRequest {

    private boolean isResendForInvalidToken = false;
    private String mTest;
    private int  connectionTimeout = 5; //in seconds
    private int  writeTimeout = 5; //in seconds
    private int  readTimeout = 5; //in seconds

    public String getTest() {
        return mTest;
    }

    public void setTest(String test) {
        mTest = test;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isResendForInvalidToken() {
        return isResendForInvalidToken;
    }

    public void setResendForInvalidToken(boolean resendForInvalidToken) {
        isResendForInvalidToken = resendForInvalidToken;
    }

    public abstract String getServiceUrl();
    public Map<String, Object> getParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("test", mTest);
        return params;
    }
    public abstract Class getServerResponseClass();


    public String getRequestJson() {
        Map<String, Object> params = addUserAndDeviceInfo(getParams());
        Gson gson = new Gson();
        String paramsString = gson.toJson(params);
        return paramsString;
    }


    private Map<String, Object> addUserAndDeviceInfo(Map<String, Object> params) {
        Map<String, Object> newParams = new LinkedHashMap<>();
        newParams.put("request", params);
        newParams.put("client", getDeviceInfo());
        newParams.put("userInfo", getUserInfo());
        return newParams;
    }

    private Map<String ,Object> getUserInfo() {
        Map<String, Object> userInfo = new LinkedHashMap<>();
        LoginUser loginUser = LoginUserDao.getInstance(CustomApplication.get()).get();
        if (loginUser == null) {
            loginUser = new LoginUser();
        }
        userInfo.put("userid", loginUser.getUserName());
        userInfo.put("token", loginUser.getPassword());
        return userInfo;
    }

    private Map<String, Object> getDeviceInfo() {
        Map<String, Object> deviceInfo = new LinkedHashMap<>();
        deviceInfo.put("platform", "android");
        deviceInfo.put("model",  getDeviceName());
        deviceInfo.put("osversion", getAndroidVersion());

        DisplayMetrics metrics = CustomApplication.get().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        deviceInfo.put("screensize", width+"*"+height);
        deviceInfo.put("appversion",  BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE);
        return deviceInfo;
    }

    private String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }

    private static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        return manufacturer + " " + model;
    }

}
