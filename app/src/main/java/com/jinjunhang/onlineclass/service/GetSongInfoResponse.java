package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.model.SongSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lzn on 16/7/24.
 */
public class GetSongInfoResponse extends ServerResponse {

    private Song song;

    public Song getSong() {
        return song;
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {

        GetSongInfoRequest req = (GetSongInfoRequest)request;
        JSONObject json = jsonObject.getJSONObject("song");

        if (req.getSong().isLive()) {
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

        JSONObject albumJson = json.getJSONObject("album");
        song.setAlbum(parseAlbum(albumJson));


        song.setName(json.getString("name"));
        song.setDesc(json.getString("desc"));
        song.setDate(json.getString("date"));
        song.setUrl(json.getString("url"));
        song.setId(json.getString("id"));
        song.setImageUrl(json.getString("image"));

        if (json.has("shareTitle")) {
            song.setShareTitle(json.getString("shareTitle"));
        }
        if (json.has("shareUrl")) {
            song.setShareUrl(json.getString("shareUrl"));
        }

        SongSetting songSetting = new SongSetting();
        JSONObject settingJson =  json.getJSONObject("settings");
        songSetting.setCanComment(settingJson.getBoolean("canComment"));
        songSetting.setMaxCommentWord(settingJson.getInt("maxCommentWord"));

        song.setSettings(songSetting);
    }

    private Album parseAlbum(JSONObject json) throws JSONException {
        Album album = new Album();
        album.setId(json.getString("id"));
        album.setName(json.getString("name"));
        album.setAuthor(json.getString("author"));
        album.setDesc(json.getString("desc"));
        album.setCount(json.getInt("count"));
        album.setImage(json.getString("image"));
        album.setListenCount(json.getString("listenCount"));
        album.setAlbumType(AlbumType.getAlbumTypeByCode(json.getString("type")));
        if (json.has("playing")) {
            album.setPlaying(json.getBoolean("playing"));
        }

        if (json.has("isReady")) {
            album.setReady(json.getBoolean("isReady"));
        } else {
            album.setReady(true);
        }
        return album;
    }
}
