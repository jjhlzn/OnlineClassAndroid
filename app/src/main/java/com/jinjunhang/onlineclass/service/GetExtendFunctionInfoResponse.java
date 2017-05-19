package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 17/5/15.
 */

public class GetExtendFunctionInfoResponse extends ServerResponse {

    public class ExtendFunctionInfo {
        private String mName = "";
        private String mCode = "";
        private String mImageUrl = "";
        private boolean mHasMessage = false;

        public ExtendFunctionInfo() {
        }

        public ExtendFunctionInfo(String code, String name, boolean hasMessage, String imageUrl) {
            mName = name;
            mCode = code;
            mHasMessage = hasMessage;
            mImageUrl = imageUrl;
        }

        public String getName() {
            return mName;
        }

        public String getCode() {
            return mCode;
        }

        public boolean hasMessage() {
            return mHasMessage;
        }

        public void setName(String name) {
            mName = name;
        }

        public void setCode(String code) {
            mCode = code;
        }

        public String getImageUrl() {
            return mImageUrl;
        }

        public void setImageUrl(String imageUrl) {
            mImageUrl = imageUrl;
        }
    }

    private List<ExtendFunctionInfo> mFunctions = new ArrayList<>();

    public List<ExtendFunctionInfo> getFunctions() {
        return mFunctions;
    }

    @Override
    public void parse(ServerRequest request, JSONObject json) throws JSONException {
        JSONArray array = json.getJSONArray("result");
        for (int i = 0; i < array.length(); i++) {
            JSONObject functionJson = array.getJSONObject(i);
            ExtendFunctionInfo function = new ExtendFunctionInfo(functionJson.getString("code"), functionJson.getString("name"),
                    functionJson.getInt("message") > 0, functionJson.getString("imageUrl"));

            mFunctions.add(function);
        }
    }
}
