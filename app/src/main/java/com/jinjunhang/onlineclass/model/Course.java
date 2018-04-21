package com.jinjunhang.onlineclass.model;
import java.util.ArrayList;
import java.util.List;

public class Course {

    private String mId = "";
    private int mSequence;
    private String mTitle = "";
    private String mTime = "";
    private String mIntroduction = "";
    private List<Course> mBeforeCourses = new ArrayList<>();

    public int getSequence() {
        return mSequence;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTime() {
        return mTime;
    }

    public void setSequence(int sequence) {
        mSequence = sequence;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getIntroduction() {
        return mIntroduction;
    }

    public void setIntroduction(String introduction) {
        mIntroduction = introduction;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public List<Course> getBeforeCourses() {
        return mBeforeCourses;
    }

    public void setBeforeCourses(List<Course> beforeCourses) {
        mBeforeCourses = beforeCourses;
    }
}
