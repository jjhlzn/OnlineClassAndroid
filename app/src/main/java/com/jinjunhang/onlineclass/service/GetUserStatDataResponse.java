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

    @Override
    public void parse(ServerRequest request, JSONObject json) throws JSONException {
        mJifen = json.getString("jifen");
        mChaiFu = json.getString("chaifu");
        mTeamPeople = json.getString("teamPeople");
        mTuiJianPeople = json.getString("tuijianPeople");
        mOrderCount = json.getString("orderCount");
    }
}
