package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/29.
 */
public class SearchResponse extends ServerResponse {

    private List<SearchResult> mSearchResults = new ArrayList<>();

    public List<SearchResult> getSearchResults() {
        return mSearchResults;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        //super.parse(request, jsonObject);


        JSONArray array = jsonObject.getJSONArray("results");
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            SearchResult result = new SearchResult();
            result.setTitle(json.getString("title"));
            result.setContent(json.getString("content"));
            result.setClickUrl(json.getString("clickUrl"));
            result.setImage(json.getString("image"));
            result.setDate(json.getString("date"));
            result.setAuthor(json.getString("author"));
            result.setDesc(json.getString("desc"));
            mSearchResults.add(result);
        }

    }

    public class SearchResult {
        private String mTitle;
        private String mContent;
        private String mClickUrl;
        private String mImage;
        private String mDate;
        private String mAuthor;
        private String mDesc;

        public String getTitle() {
            return mTitle;
        }

        public String getClickUrl() {
            return mClickUrl;
        }

        public String getImage() {
            return mImage;
        }

        public String getDate() {
            return mDate;
        }

        public String getAuthor() {
            return mAuthor;
        }

        public String getDesc() {
            return mDesc;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public void setClickUrl(String clickUrl) {
            mClickUrl = clickUrl;
        }

        public void setImage(String image) {
            mImage = image;
        }

        public void setDate(String date) {
            mDate = date;
        }

        public void setAuthor(String author) {
            mAuthor = author;
        }

        public void setDesc(String desc) {
            mDesc = desc;
        }

        public String getContent() {
            return mContent;
        }

        public void setContent(String content) {
            mContent = content;
        }
    }
}
