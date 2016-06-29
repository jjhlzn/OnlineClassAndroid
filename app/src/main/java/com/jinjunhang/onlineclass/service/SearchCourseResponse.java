package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzn on 16/6/29.
 */
public class SearchCourseResponse extends PagedServerResponse<Album> {

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        super.parse(request, jsonObject);

        JSONArray array = jsonObject.getJSONArray("albums");
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            Album album = new Album();
            album.setId(json.getString("id"));
            album.setName(json.getString("name"));
            album.setAuthor(json.getString("author"));
            album.setCount(json.getInt("count"));
            album.setImage(json.getString("image"));
            album.setListenCount(json.getString("listenCount"));
            album.setAlbumType(AlbumType.getAlbumTypeByCode(json.getString("type")));
            mResultSet.add(album);
        }

    }
}
