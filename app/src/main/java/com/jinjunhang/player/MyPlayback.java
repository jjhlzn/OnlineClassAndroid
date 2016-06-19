package com.jinjunhang.player;

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

import android.support.v4.media.session.MediaSessionCompat;


import com.jinjunhang.player.MusicService;

import static android.support.v4.media.session.MediaSessionCompat.QueueItem;

/**
 * Interface representing either Local or Remote Playback. The {@link MusicService} works
 * directly with an instance of the Playback object to make the various calls such as
 * play, pause etc.
 */
public interface MyPlayback {
    /**
     * Start/setup the playback.
     * Resources/listeners would be allocated by implementations.
     */
    void start();

    /**
     * Stop the playback. All resources can be de-allocated by implementations here.
     */
    void stop();

    /**
     * Set the latest playback state as determined by the caller.
     */
    void setState(int state);

    /**
     * Get the current {@link android.media.session.PlaybackState#getState()}
     */
    int getState();

    /**
     * @return boolean indicating whether the player is playing or is supposed to be
     * playing when we gain audio focus.
     */
    boolean isPlaying();

    /**
     * @param item to play
     */
    void play(MediaSessionCompat.QueueItem item);

    /**
     * Pause the current playing item
     */
    void pause();

    /**
     * Seek to the given position
     */
    void seekTo(int position);

    /**
     *
     * @return the current media Id being processed in any state or null.
     */
    String getCurrentMediaId();

}
