package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Pos;
import com.jinjunhang.onlineclass.model.ZhuanLan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetZhuanLanAndTuijianCourseResponse extends GetTuijianCoursesResponse{

    private static final String TAG = LogHelper.makeLogTag(GetZhuanLanAndTuijianCourseResponse.class);

    private List<ZhuanLan> mZhuanLans = new ArrayList<>();
    private List<ZhuanLan> mJpks = new ArrayList<>();
    private Pos mPos;

    public Pos getPos() {
        return mPos;
    }

    public List<ZhuanLan> getZhuanLans() {
        return mZhuanLans;
    }

    public List<ZhuanLan> getJpks() {
        return mJpks;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        super.parse(request, jsonObject);
        mZhuanLans = parseZhuanLans(jsonObject.getJSONArray("zhuanLans"));
        mJpks = parseZhuanLans(jsonObject.getJSONArray("jpks"));
        mPos = parsePos(jsonObject.getJSONObject("pos"));
    }

    private List<ZhuanLan> parseZhuanLans(JSONArray jsonArray) throws JSONException {
        List<ZhuanLan> zhuanLans = new ArrayList<>();
        for(int i = 0 ; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            ZhuanLan zhuanLan = new ZhuanLan();
            zhuanLan.setName(json.getString("name"));
            zhuanLan.setLatest(json.getString("latest"));
            zhuanLan.setPriceInfo(json.getString("priceInfo"));
            zhuanLan.setUpdateTime(json.getString("updateTime"));
            zhuanLan.setDescription(json.getString("description"));
            zhuanLan.setImageUrl(json.getString("imageUrl"));
            zhuanLan.setUrl(json.getString("url"));
            zhuanLan.setAuthor(json.getString("author"));
            zhuanLan.setAuthorTitle(json.getString("authorTitle"));
            zhuanLan.setDingYue(json.getInt("dingyue"));
            zhuanLans.add(zhuanLan);
        }
        return zhuanLans;
    }

    private Pos parsePos(JSONObject json) throws JSONException {
        Pos pos = new Pos();
        pos.setClickUrl(json.getString("clickUrl"));
        pos.setImageUrl(json.getString("imageUrl"));
        pos.setTitle(json.getString("title"));
        LogHelper.d(TAG, "imageUrl: " + pos.getImageUrl());
        LogHelper.d(TAG, "pos.getImageUrl().isEmpty(): " + pos.getImageUrl().isEmpty());
        if (pos.getImageUrl().isEmpty())
            return null;
        return pos;
    }


}
