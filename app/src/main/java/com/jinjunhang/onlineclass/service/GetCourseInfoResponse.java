package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.model.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/20.
 */
public class GetCourseInfoResponse extends ServerResponse {

    private Course mCourse;

    public Course getCourse() {
        return mCourse;
    }

    public GetCourseInfoResponse() {

    }

    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        JSONObject json = jsonObject.getJSONObject("course");
        mCourse = new Course();
        mCourse.setId(json.getString("id"));
        mCourse.setTime(json.getString("time"));
        mCourse.setTitle(json.getString("title"));
        mCourse.setIntroduction(json.getString("introduction"));
        List<Course> beforeCourses = new ArrayList<>();
        JSONArray jsons = json.getJSONArray("beforeCourses");
        for(int i =0; i < jsons.length(); i++) {
            JSONObject item = jsons.getJSONObject(i);
            Course child = new Course();
            child.setSequence(i+1);
            child.setId(item.getString("id"));
            child.setTitle(item.getString("title"));
            child.setTime(item.getString("time"));
            beforeCourses.add(child);
        }
        mCourse.setBeforeCourses(beforeCourses);
    }
}
