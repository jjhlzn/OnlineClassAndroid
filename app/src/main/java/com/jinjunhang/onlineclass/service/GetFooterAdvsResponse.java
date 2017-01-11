package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jinjunhang on 17/1/8.
 */

public class GetFooterAdvsResponse extends ServerResponse {
    private List<FooterAdv> mImageAdvs = new ArrayList<>();

    public List<FooterAdv> getImageAdvs() {
        return mImageAdvs;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        JSONArray array = jsonObject.getJSONArray("result");
        for(int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            GetFooterAdvsResponse.FooterAdv adv = new GetFooterAdvsResponse.FooterAdv();
            mImageAdvs.add(adv);
            adv.mImageUrl = json.getString("imageUrl");
            adv.mTitle = json.getString("title");
            adv.mUrl = json.getString("url");
        }
    }

    public static class FooterAdv {
        private String mImageUrl = "";
        private String mTitle = "";
        private String mUrl = "";

        public String getImageUrl() {
            return mImageUrl;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setImageUrl(String imageUrl) {
            mImageUrl = imageUrl;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public void setUrl(String url) {
            mUrl = url;
        }
    }
}
