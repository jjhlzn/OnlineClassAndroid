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

    public LineRecord(int mImage, String mTitle, CellClickListener listener, String url, String otherInfo) {
        this.mImage = mImage;
        this.mTitle = mTitle;
        this.mListener = listener;
        this.mUrl = url;
        this.mOtherInfo = otherInfo;
    }

    public int getImage() {
        return mImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setImage(int mImage) {
        this.mImage = mImage;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getOtherInfo() {
        return mOtherInfo;
    }

    public void setOtherInfo(String mOtherInfo) {
        this.mOtherInfo = mOtherInfo;
    }

    public String getUrl() {
        return mUrl;
    }


    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public CellClickListener getListener() {
        return mListener;
    }

    public void setListener(CellClickListener mListener) {
        this.mListener = mListener;
    }
}
