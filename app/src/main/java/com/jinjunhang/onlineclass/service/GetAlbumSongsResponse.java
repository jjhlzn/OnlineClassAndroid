package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.model.SongSetting;
import com.jinjunhang.framework.lib.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lzn on 16/6/12.
 */
public class GetAlbumSongsResponse extends PagedServerResponse<Song> {

    private static final String TAG = LogHelper.makeLogTag(GetAlbumSongsResponse.class);

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

            if (req.getAlbum().isLive()) {
                LiveSong liveSong = new LiveSong();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                if (json.has("startTime")) {
                    liveSong.setStartDateTime(json.getString("startTime"));
                } else {
                    liveSong.setStartDateTime(sdf.format(new Date()));
                }
                if (json.has("endTime")) {
                    liveSong.setEndDateTime(json.getString("endTime"));
                } else {
                    Date now = new Date();
                    now.setTime( now.getTime() + 4 * 60 * 60 * 1000 );
                    liveSong.setEndDateTime(sdf.format(now));
                }

                liveSong.setImageUrl(json.getString("image"));
                liveSong.setListenPeople(json.getString("listenPeople"));
                liveSong.setHasAdvImage(json.getBoolean("hasAdvImage"));
                if (liveSong.hasAdvImage()) {
                    liveSong.setAdvImageUrl(json.getString("advImageUrl"));
                    liveSong.setAdvUrl(json.getString("advUrl"));
                }

                liveSong.setPlaying(json.getBoolean("playing"));
                liveSong.setAdvText(json.getString("advText"));
                liveSong.setScrollRate(json.getInt("advScrollRate"));
                JSONArray imgAdvArray = json.getJSONArray("advImages");
                for(int j = 0; j < imgAdvArray.length(); j++) {
                    JSONObject jsonItem = imgAdvArray.getJSONObject(j);
                    Advertise advertise = new Advertise();
                    advertise.setImageUrl(jsonItem.getString("imageurl"));
                    advertise.setClickUrl(jsonItem.getString("link"));
                    advertise.setTitle(jsonItem.getString("title"));
                    liveSong.getImageAdvs().add(advertise);
                }

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
            song.setImageUrl(json.getString("image"));
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
