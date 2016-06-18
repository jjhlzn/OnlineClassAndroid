/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jinjunhang.uamp.model;

import android.support.v4.media.MediaMetadataCompat;

import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.uamp.utils.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class to get a list of MusicTrack's based on a server-side JSON
 * configuration.
 */
public class OnlineClassSource implements MusicProviderSource {

    private static final String TAG = LogHelper.makeLogTag(OnlineClassSource.class);

    private List<MediaMetadataCompat> mSongs;

    public void setSongs(List<MediaMetadataCompat> songs) {
        mSongs = songs;
    }

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        return mSongs.iterator();
    }



}
