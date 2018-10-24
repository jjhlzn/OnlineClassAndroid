package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzn on 16/6/27.
 */
public class LoginResponse extends ServerResponse {

    private String mUserId;
    private String mName;
    private String mToken;
    private String mNickName;
    private String mLevel;
    private String mBoss;
    private String mSex;
    private String mCodeImageUrl;

    public String getName() {
        return mName;
    }

    public String getToken() {
        return mToken;
    }

    public String getNickName() {
        return mNickName;
    }

    public String getLevel() {
        return mLevel;
    }

    public String getBoss() {
        return mBoss;
    }

    public String getSex() {
        return mSex;
    }

    public String getCodeImageUrl() {
        return mCodeImageUrl;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }

    public void setLevel(String level) {
        mLevel = level;
    }

    public void setBoss(String boss) {
        mBoss = boss;
    }

    public void setSex(String sex) {
        mSex = sex;
    }

    public void setCodeImageUrl(String codeImageUrl) {
        mCodeImageUrl = codeImageUrl;
    }

    @Override
    public void parse(ServerRequest request, JSONObject json) throws JSONException {
        if (json.has("userid")) {
            mUserId = json.getString("userid");
        }
        mName = json.getString("name");
        mToken = json.getString("token");
        mNickName = json.getString("nickname");
        mLevel = json.getString("level");
        mBoss = json.getString("boss");
        mSex = json.getString("sex");
        mCodeImageUrl = json.getString("codeImageUrl");

    }
}
