package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jjh on 2016-6-30.
 */
public class GetUserStatDataResponse extends ServerResponse {

    private String mJifen;
    private String mChaiFu;
    private String mTeamPeople;
    private String mTuiJianPeople;
    private String mOrderCount;
    private double mZhidian;

    private String mName;
    private String mNickName;
    private String mLevel;
    private String mBoss;
    private String mSex;
    private String mCodeImageUrl;
    private String mVipEndDate = "";
    private Boolean mIsBindWeixin = false;
    private boolean mHasNewMessage = false;
    private boolean mHasBindPhone = false;

    public String getJifen() {
        return mJifen;
    }

    public String getChaiFu() {
        return mChaiFu;
    }

    public String getTeamPeople() {
        return mTeamPeople;
    }

    public String getTuiJianPeople() {
        return mTuiJianPeople;
    }

    public String getOrderCount() {
        return mOrderCount;
    }

    public String getName() {
        return mName;
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

    public String getVipEndDate() {
        return mVipEndDate;
    }

    public void setVipEndDate(String vipEndDate) {
        mVipEndDate = vipEndDate;
    }

    public Boolean isBindWeixin() {
        return mIsBindWeixin;
    }

    public boolean hasNewMessage() {
        return mHasNewMessage;
    }

    public boolean hasBindPhone() {
        return mHasBindPhone;
    }

    public double getZhidian() {
        return mZhidian;
    }



    @Override
    public void parse(ServerRequest request, JSONObject json) throws JSONException {
        mJifen = json.getString("jifen");
        mChaiFu = json.getString("chaifu");
        mTeamPeople = json.getString("teamPeople");
        mTuiJianPeople = json.getString("tuijianPeople");
        mOrderCount = json.getString("orderCount");

        mName = json.getString("name");
        mNickName = json.getString("nickname");
        mLevel = json.getString("level");
        mBoss = json.getString("boss");
        mSex = json.getString("sex");
        mCodeImageUrl = json.getString("codeImageUrl");
        mVipEndDate = json.getString("vipEndDate");
        mZhidian = json.getDouble("zhidian");

        if (json.has("isBindWeixin")) {
            mIsBindWeixin = json.getBoolean("isBindWeixin");
        }

        if (json.has("hasNewMessage")) {
            mHasNewMessage = json.getBoolean("hasNewMessage");
        }

        if (json.has("hasBindPhone")) {
            mHasBindPhone = json.getBoolean("hasBindPhone");
        }
    }
}
