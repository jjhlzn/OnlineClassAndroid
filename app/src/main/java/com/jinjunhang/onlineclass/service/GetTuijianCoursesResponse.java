package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.lib.LogHelper;
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
 * Created by lzn on 16/6/12.
 */
public class GetTuijianCoursesResponse extends ServerResponse {

    private static final String TAG = LogHelper.makeLogTag(GetTuijianCoursesResponse.class);

    private List<Album> mCourses = new ArrayList<>();

    public GetTuijianCoursesResponse() {

    }

    public List<Album> getCourses() {
        return mCourses;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {

        JSONArray jsonArray = jsonObject.getJSONArray("albums");

        for(int i = 0 ; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Album album = new Album();
            album.setId(json.getString("id"));
            album.setName(json.getString("name"));
            album.setAuthor(json.getString("author"));
            album.setDesc(json.getString("desc"));
            album.setCount(json.getInt("count"));
            album.setImage(json.getString("image"));
            album.setListenCount(json.getString("listenCount"));
            album.setAlbumType(AlbumType.getAlbumTypeByCode(json.getString("type")));
            if (json.has("playing")) {
                album.setPlaying(json.getBoolean("playing"));
            }

            if (json.has("isReady")) {
                album.setReady(json.getBoolean("isReady"));
            } else {
                album.setReady(true);
            }

            if (json.has("playTimeDesc")) {
                album.setPlayTimeDesc(json.getString("playTimeDesc"));
            }

            if (json.has("isAgent")) {
                album.setAgent(json.getBoolean("isAgent"));
            }

            album.setStatus(json.getString("status"));
            album.setStars(json.getDouble("stars"));
            album.setListenerCount(json.getInt("listenerCount"));
            album.setLiveTime(json.getString("liveTime"));
            album.setDate(json.getString("date"));

            mCourses.add(album);
        }
    }

    public class Course {
        private String mId;
        private String mName;
        private String mDesc;
        private String mDate;
        private String mImageUrl;
        private String mStatus;
        private double  mStars;
        private String mUrl;
        private String mLiveTime;
        private int mListenerCount;
        private String mShareTitle;
        private String mShareUrl;

        public String getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }

        public String getDesc() {
            return mDesc;
        }

        public String getDate() {
            return mDate;
        }

        public String getImageUrl() {
            return mImageUrl;
        }

        public String getStatus() {
            return mStatus;
        }

        public double getStars() {
            return mStars;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setId(String id) {
            mId = id;
        }

        public void setName(String name) {
            mName = name;
        }

        public void setDesc(String desc) {
            mDesc = desc;
        }

        public void setDate(String date) {
            mDate = date;
        }

        public void setImageUrl(String imageUrl) {
            mImageUrl = imageUrl;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

        public void setStars(double stars) {
            mStars = stars;
        }

        public void setUrl(String url) {
            mUrl = url;
        }

        public String getLiveTime() {
            return mLiveTime;
        }

        public int getListenerCount() {
            return mListenerCount;
        }

        public void setLiveTime(String liveTime) {
            mLiveTime = liveTime;
        }

        public void setListenerCount(int listenerCount) {
            mListenerCount = listenerCount;
        }

        public String getShareTitle() {
            return mShareTitle;
        }

        public String getShareUrl() {
            return mShareUrl;
        }

        public void setShareTitle(String shareTitle) {
            mShareTitle = shareTitle;
        }

        public void setShareUrl(String shareUrl) {
            mShareUrl = shareUrl;
        }
    }
}
