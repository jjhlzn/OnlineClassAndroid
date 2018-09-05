package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.ZhuanLan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetZhuanLanAndTuijianCourseResponse extends GetTuijianCoursesResponse{

    private List<ZhuanLan> mZhuanLans = new ArrayList<>();
    private List<ZhuanLan> mJpks = new ArrayList<>();

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

}
