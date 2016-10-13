package com.jinjunhang.onlineclass.model;

import com.jinjunhang.onlineclass.service.LoginResponse;


import java.io.Serializable;

/**
 * Created by lzn on 16/6/27.
 */

public class LoginUser implements Serializable {

    private String mUserName;

    private String mPassword;
    private String mName;
    private String mNickName;
    private String mSex;
    private String mCodeImageUrl;
    private String mToken;
    private String mLevel;
    private String mBoss;

    public LoginUser() {}
    public LoginUser(LoginResponse resp) {
        mName = resp.getName();
        mNickName =resp.getNickName();
        mSex = resp.getSex();
        mCodeImageUrl = resp.getCodeImageUrl();
        mToken = resp.getToken();
        mLevel = resp.getLevel();
        mBoss = resp.getBoss();
    }

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getName() {
        return mName;
    }

    public String getNickName() {
        return mNickName;
    }

    public String getSex() {
        return mSex;
    }

    public String getCodeImageUrl() {
        return mCodeImageUrl;
    }

    public String getToken() {
        return mToken;
    }

    public String getLevel() {
        return mLevel;
    }

    public String getBoss() {
        return mBoss;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }

    public void setSex(String sex) {
        mSex = sex;
    }

    public void setCodeImageUrl(String codeImageUrl) {
        mCodeImageUrl = codeImageUrl;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public void setLevel(String level) {
        mLevel = level;
    }

    public void setBoss(String boss) {
        mBoss = boss;
    }
}
