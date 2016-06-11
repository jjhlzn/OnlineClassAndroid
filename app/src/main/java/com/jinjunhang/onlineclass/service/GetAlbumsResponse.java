package com.jinjunhang.onlineclass.service;

import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lzn on 16/6/10.
 */
public class GetAlbumsResponse extends PagedServerResponse<Album> {


    public GetAlbumsResponse() {
        mResultSet = new ArrayList<>();
    }

    @Override
    void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        super.parse(request, jsonObject);

        JSONArray jsonArray = jsonObject.getJSONArray("albums");

        for(int i = 0 ; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
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
