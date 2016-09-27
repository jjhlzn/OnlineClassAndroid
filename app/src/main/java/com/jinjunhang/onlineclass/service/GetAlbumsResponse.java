package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerRequest;
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
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        super.parse(request, jsonObject);

        JSONArray jsonArray = jsonObject.getJSONArray("albums");

        for(int i = 0 ; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Album album = new Album();
            album.setId(json.getString("id"));
            album.setName(json.getString("name"));
            album.setAuthor(json.getString("author"));
            album.setDesc(json.getString("desc"));
            album.setCount(json.getInt("count"));
            album.setImage(json.getString("image"));
            album.setListenCount(json.getString("listenCount"));
            album.setAlbumType(AlbumType.getAlbumTypeByCode(json.getString("type")));
            if (json.has("playinig")) {
                album.setPlaying(json.getBoolean("playing"));
            }

            if (json.has("isReady")) {
                album.setReady(json.getBoolean("isReady"));
            } else {
                album.setReady(true);
            }
            //TODO:
            album.setReady(true);

            mResultSet.add(album);
        }

    }
}
