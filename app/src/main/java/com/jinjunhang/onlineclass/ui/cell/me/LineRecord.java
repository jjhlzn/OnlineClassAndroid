package com.jinjunhang.onlineclass.ui.cell.me;

import android.app.Activity;

import com.jinjunhang.onlineclass.ui.cell.CellClickListener;

/**
 * Created by jjh on 2016-6-29.
 */
public class LineRecord {
    private int mImage;
    private String mTitle;
    private String mOtherInfo;
    private String mUrl;
    private CellClickListener mListener;

    public LineRecord(int mImage, String mTitle, CellClickListener listener, String url) {
        this.mImage = mImage;
        this.mTitle = mTitle;
        this.mListener = listener;
        this.mUrl = url;
    }

    public int getmImage() {
        return mImage;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmOtherInfo() {
        return mOtherInfo;
    }

    public void setmOtherInfo(String mOtherInfo) {
        this.mOtherInfo = mOtherInfo;
    }

    public String getmUrl() {
        return mUrl;
    }


    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public CellClickListener getmListener() {
        return mListener;
    }

    public void setmListener(CellClickListener mListener) {
        this.mListener = mListener;
    }
}
