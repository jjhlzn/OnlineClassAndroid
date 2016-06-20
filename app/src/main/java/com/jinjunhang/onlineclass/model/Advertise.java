package com.jinjunhang.onlineclass.model;

/**
 * Created by lzn on 16/6/20.
 */
public class Advertise extends BaseModelObject {
     private String imageUrl = "";
     private String clickUrl = "";
     private String title = "";

    public String getImageUrl() {
        return imageUrl;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
