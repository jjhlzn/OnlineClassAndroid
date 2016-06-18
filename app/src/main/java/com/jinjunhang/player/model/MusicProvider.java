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

package com.jinjunhang.player.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;


import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Simple data provider for music tracks. The actual metadata source is delegated to a
 * MusicProviderSource defined by a constructor argument of this class.
 */
public class MusicProvider {

    private static final String TAG = LogHelper.makeLogTag(MusicProvider.class);

    private MusicProviderSource mSource;

    // Categorized caches for music track data:
    private final ConcurrentMap<String, MutableMediaMetadata> mMusicListById;


    public MusicProvider() {
        this(new OnlineClassSource());
    }
    public MusicProvider(MusicProviderSource source) {
        mSource = source;
        mMusicListById = new ConcurrentHashMap<>();
        buildMusicList();
    }

    private synchronized void buildMusicList() {
        Iterator<MediaMetadataCompat> tracks = mSource.iterator();
        while (tracks.hasNext()) {
            MediaMetadataCompat item = tracks.next();
            String musicId = item.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
            mMusicListById.put(musicId, new MutableMediaMetadata(musicId, item));
        }
    }

    public void updateMusicSource(List<MediaMetadataCompat> songs) {
        mSource.setSource(songs);
        buildMusicList();
    }

    public Iterable<MediaMetadataCompat> getAllMusics() {

        List<MediaMetadataCompat> result = new ArrayList<>();
        Iterator<MediaMetadataCompat> iterator = mSource.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    /**
     * Return the MediaMetadataCompat for the given musicID.
     *
     * @param musicId The unique, non-hierarchical music ID.
     */
    public MediaMetadataCompat getMusic(String musicId) {
        //LogHelper.d(TAG, "mMusicListById = " + mMusicListById);
        Iterator<MediaMetadataCompat> songs = mSource.iterator();
        while(songs.hasNext()) {
            MediaMetadataCompat song = songs.next();
            if (song.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID) == musicId) {
                return song;
            }
        }
        return null;
        //return mMusicListById.containsKey(musicId) ? mMusicListById.get(musicId).metadata : null;
    }

    public synchronized void updateMusicArt(String musicId, Bitmap albumArt, Bitmap icon) {
        MediaMetadataCompat metadata = getMusic(musicId);
        metadata = new MediaMetadataCompat.Builder(metadata)

                // set high resolution bitmap in METADATA_KEY_ALBUM_ART. This is used, for
                // example, on the lockscreen background when the media session is active.
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)

                // set small version of the album art in the DISPLAY_ICON. This is used on
                // the MediaDescription and thus it should be small to be serialized if
                // necessary
                .putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, icon)

                .build();

        MutableMediaMetadata mutableMetadata = mMusicListById.get(musicId);
        if (mutableMetadata == null) {
            throw new IllegalStateException("Unexpected error: Inconsistent data structures in " +
                    "MusicProvider");
        }

        mutableMetadata.metadata = metadata;
    }

    public List<MediaBrowserCompat.MediaItem> getChildren() {
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        Iterator<MediaMetadataCompat> iterator = getAllMusics().iterator();
        while (iterator.hasNext()) {
            mediaItems.add(createMediaItem(iterator.next()));
        }
        return mediaItems;
    }



    private MediaBrowserCompat.MediaItem createMediaItem(MediaMetadataCompat metadata) {
        // Since mediaMetadata fields are immutable, we need to create a copy, so we
        // can set a hierarchy-aware mediaID. We will need to know the media hierarchy
        // when we get a onPlayFromMusicID call, so we can create the proper queue based
        // on where the music was selected from (by artist, by genre, random, etc)
        String genre = metadata.getString(MediaMetadataCompat.METADATA_KEY_GENRE);
        MediaMetadataCompat copy = new MediaMetadataCompat.Builder(metadata)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, metadata.getDescription().getMediaId())
                .build();
        return new MediaBrowserCompat.MediaItem(copy.getDescription(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);

    }

}
