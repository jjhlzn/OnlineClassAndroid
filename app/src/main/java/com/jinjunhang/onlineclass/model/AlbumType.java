package com.jinjunhang.onlineclass.model;

import com.jinjunhang.onlineclass.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/10.
 */
public class AlbumType extends BaseModelObject {

    private String mName;
    private String mTypeCode;
    private int image;

    private AlbumType(String mName, String mTypeCode, int image ) {
        this.mName = mName;
        this.mTypeCode = mTypeCode;
        this.image = image;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTypeCode() {
        return mTypeCode;
    }

    public int getImage() {
        return image;
    }

    public final static AlbumType DummyAlbumType = new AlbumType("Dummy", "Dummy", R.drawable.placeholder);
    public final static AlbumType LiveAlbumType = new AlbumType("直播课程", "Live", R.drawable.placeholder);
    public final static AlbumType VipAlbumType = new AlbumType("VIP课程", "Vip", R.drawable.placeholder);
    public final static AlbumType AgentEducationAlbumType = new AlbumType("代理培训课程", "Agent", R.drawable.placeholder);
    public final static AlbumType CommonAlbumType = new AlbumType("往期直播课程", "Common", R.drawable.placeholder);


    public static AlbumType getAlbumTypeByCode(String code) {
        switch (code) {
            case "Live":
                return LiveAlbumType;
            case "Vip":
                return VipAlbumType;
            case "Agent":
                return AgentEducationAlbumType;
            default:
                return CommonAlbumType;
        }
    }
}
