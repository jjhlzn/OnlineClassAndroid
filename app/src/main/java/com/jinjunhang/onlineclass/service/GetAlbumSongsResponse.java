package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.model.SongSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lzn on 16/6/12.
 */
public class GetAlbumSongsResponse extends PagedServerResponse<Song> {

    public GetAlbumSongsResponse() {
        mResultSet = new ArrayList<>();
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        super.parse(request, jsonObject);

        GetAlbumSongsRequest req = (GetAlbumSongsRequest)request;

        JSONArray jsonArray = jsonObject.getJSONArray("songs");
        for( int i = 0; i < jsonArray.length(); i++ ) {
            JSONObject json = jsonArray.getJSONObject(i);

            Song song;
            if (req.getAlbum().getAlbumType().equals(AlbumType.LiveAlbumType)) {
                LiveSong liveSong = new LiveSong();
                liveSong.setStartDateTime(json.getString("startTime"));
                liveSong.setEndDateTime(json.getString("endTime"));
                song = liveSong;
            } else {
                song = new Song();
            }
            song.setAlbum(req.getAlbum());
            song.setName(json.getString("name"));
            song.setDesc(json.getString("desc"));
            song.setDate(json.getString("date"));
            song.setUrl(json.getString("url"));
            song.setId(json.getString("id"));
            SongSetting songSetting = new SongSetting();
            JSONObject settingJson =  json.getJSONObject("settings");
            songSetting.setCanComment(settingJson.getBoolean("canComment"));
            songSetting.setMaxCommentWord(settingJson.getInt("maxCommentWord"));
            song.setSettings(songSetting);



            mResultSet.add(song);
        }
        req.getAlbum().setSongs(mResultSet);

    }
}
