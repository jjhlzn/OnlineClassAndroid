package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.model.SongSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lzn on 16/6/12.
 */
public class GetTuijianCoursesResponse extends ServerResponse {

    private static final String TAG = LogHelper.makeLogTag(GetTuijianCoursesResponse.class);

    private List<GetTuijianCoursesResponse.TuijianCourse> mCourses = new ArrayList<>();

    public GetTuijianCoursesResponse() {

    }

    public List<TuijianCourse> getCourses() {
        return mCourses;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        //super.parse(request, jsonObject);

        List<TuijianCourse> courses = new ArrayList<>();

        JSONArray jsonArray = jsonObject.getJSONArray("courses");
        for( int i = 0; i < jsonArray.length(); i++ ) {
            JSONObject json = jsonArray.getJSONObject(i);

            TuijianCourse course = new TuijianCourse();

            course.setId(json.getString("id"));
            course.setName(json.getString("name"));
            course.setDesc(json.getString("desc"));
            course.setDate(json.getString("date"));
            course.setImageUrl(json.getString("image"));
            course.setStatus(json.getString("status"));
            course.setStars(json.getDouble("stars"));
            course.setUrl(json.getString("url"));
            mCourses.add(course);
        }
    }

    public class TuijianCourse {
        private String mId;
        private String mName;
        private String mDesc;
        private String mDate;
        private String mImageUrl;
        private String mStatus;
        private double  mStars;
        private String mUrl;

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
    }
}
