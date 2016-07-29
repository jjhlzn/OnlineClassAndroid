package com.jinjunhang.onlineclass.model;

import android.util.Log;

import com.jinjunhang.player.utils.LogHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by lzn on 16/6/12.
 */
public class LiveSong extends Song {
    private static String TAG = LogHelper.makeLogTag(LiveSong.class);

    private String mImageUrl;
    private String mListenPeople;

    private String mStartDateTime;
    private String mEndDateTime;
    private boolean mHasAdvImage;
    private String mAdvImageUrl = "";
    private String mAdvUrl = "";

    private boolean mSongAdvInfoChanged = false;

    public String getStartDateTime() {
        return mStartDateTime;
    }

    public String getEndDateTime() {
        return mEndDateTime;
    }

    public String getEndTimeString() {
        return mEndDateTime.substring(11);
    }

    public void setStartDateTime(String startDateTime) {
        mStartDateTime = startDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        mEndDateTime = endDateTime;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getListenPeople() {
        return mListenPeople;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public void setListenPeople(String listenPeople) {
        mListenPeople = listenPeople;
    }

    public boolean hasAdvImage() {
        return mHasAdvImage;
    }

    public String getAdvImageUrl() {
        return mAdvImageUrl;
    }

    public String getAdvUrl() {
        return mAdvUrl;
    }

    public boolean isSongAdvInfoChanged() {
        return mSongAdvInfoChanged;
    }

    public void updateSongAdvInfo(boolean hasAdvImage, String advImageUrl, String advUrl) {
        //hasAdvImage mHasImage all are false, nothing to do
        if ((hasAdvImage == mHasAdvImage) && !hasAdvImage ) {
            LogHelper.d(TAG, "hasAdvImage = " + hasAdvImage + ", mHasAdvImage = " + mHasAdvImage);
            return;
        }

        //hasAdvImage and mHaImage are not the same
        if (hasAdvImage != mHasAdvImage) {
            setHasAdvImage(hasAdvImage);
            setAdvImageUrl(advImageUrl);
            setAdvUrl(advUrl);
            return;
        }

        //hasAdvImage and mHasImage all are true, need to check AdvImageUrl and AdvUrl
        if (!advImageUrl.equals(mAdvImageUrl) || !advUrl.equals(mAdvUrl)) {
            setHasAdvImage(hasAdvImage);
            setAdvImageUrl(advImageUrl);
            setAdvUrl(advUrl);
            return;
        }
    }

    public void setSongAdvInfoChanged(boolean songAdvInfoChanged) {
        mSongAdvInfoChanged = songAdvInfoChanged;
    }

    public void setHasAdvImage(boolean hasAdvImage) {
        mHasAdvImage = hasAdvImage;
        mSongAdvInfoChanged = true;
    }

    public void setAdvImageUrl(String advImageUrl) {
        mAdvImageUrl = advImageUrl;
        mSongAdvInfoChanged = true;
    }

    public void setAdvUrl(String advUrl) {
        mAdvUrl = advUrl;
        mSongAdvInfoChanged = true;
    }

    public long getTotalTimeInSec() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startTime = sdf.parse(mStartDateTime);
            Date endTime = sdf.parse(mEndDateTime);
            return (endTime.getTime() - startTime.getTime()) / 1000;
        }
        catch (Exception ex) {
            LogHelper.e(TAG, ex);
            return 1;
        }
    }

    public long getTimeLeftInSec() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date endTime = sdf.parse(mEndDateTime);
            Date nowTime = sdf.parse(sdf.format(new Date()));
            //LogHelper.d(TAG, "endTime = " + sdf.format(endTime) + ", nowTime = " + sdf.format(nowTime));
            //LogHelper.d(TAG, "endTime.getTime() = " + endTime.getTime() + ", nowTime.getTime() = " + nowTime.getTime());

            //LogHelper.d(TAG, "differ = " + (endTime.getTime() - nowTime.getTime()) / 1000);
            return (endTime.getTime() - nowTime.getTime()) / 1000;
        }
        catch (Exception ex) {
            LogHelper.e(TAG, ex);
            return 1;
        }
    }

    public long getPlayedTimeInSec() {
        return getTotalTimeInSec() - getTimeLeftInSec();
    }
}
