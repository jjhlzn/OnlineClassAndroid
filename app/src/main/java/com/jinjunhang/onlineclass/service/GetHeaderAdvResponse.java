package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jinjunhang on 17/1/8.
 */

public class GetHeaderAdvResponse extends ServerResponse {

    private List<HeaderAdvImage> mImageAdvs = new ArrayList<>();

    public List<HeaderAdvImage> getImageAdvs() {
        return mImageAdvs;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        JSONArray array = jsonObject.getJSONArray("result");
        for(int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            HeaderAdvImage adv = new HeaderAdvImage();
            mImageAdvs.add(adv);
            adv.mImageUrl = json.getString("imageUrl");
            adv.mType = json.getString("type");
            adv.params = new HashMap<>();
            JSONArray paramsArr = json.getJSONArray("params");
            for(int j = 0; j < paramsArr.length(); j++) {
                JSONObject paramJson = paramsArr.getJSONObject(j);
                adv.params.put(paramJson.getString("key"), paramJson.getString("value"));
            }
        }

    }

    public static class HeaderAdvImage {
        public static final String TYPE_SONG = "song";
        public static final String TYPE_ALBUMLIST = "albumlist";
        public static final String PARAM_KEY_SONG = "songid";

        private String mImageUrl = "";
        private String mType = TYPE_SONG;
        private Map<String, String> params = new HashMap<>();

        public String getImageUrl() {
            return mImageUrl;
        }

        public String getType() {
            return mType;
        }

        public Map<String, String> getParams() {
            return params;
        }

        public void setImageUrl(String imageUrl) {
            mImageUrl = imageUrl;
        }

        public void setType(String type) {
            mType = type;
        }

        public void setParams(Map<String, String> params) {
            this.params = params;
        }
    }
}
