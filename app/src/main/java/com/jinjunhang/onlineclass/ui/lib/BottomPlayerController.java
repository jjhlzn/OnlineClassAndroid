package com.jinjunhang.onlineclass.ui.lib;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.activity.album.SongActivity;
import com.jinjunhang.onlineclass.ui.fragment.album.BaseSongFragment;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.player.utils.StatusHelper;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by lzn on 16/6/21.
 */
public class BottomPlayerController implements ExoPlayer.Listener   {
    private static final String TAG = LogHelper.makeLogTag(BottomPlayerController.class);

    private Activity mActivity;
    private ImageView mPlayImage;
    private RoundedImageView mSongImage;
    private RotateAnimation mRotation;

    private MusicPlayer mMusicPlayer;

    private View mView;

    private static long fromDegree = 0;
    private static int toDegress = 0;

    //private static BottomPlayerController instance;

    public static BottomPlayerController getInstance(Activity activity) {
        return new BottomPlayerController(activity);
    }


    private BottomPlayerController(Activity activity) {
        mActivity = activity;
        mMusicPlayer = MusicPlayer.getInstance(activity);
        mView = activity.getLayoutInflater().inflate(R.layout.bottom_player, null);

        mPlayImage = (ImageView) mView.findViewById(R.id.bottomPlayer_playImage);
        updateAlbumImage();

        mSongImage = (RoundedImageView) mView.findViewById(R.id.bottomPlayer_imageButton);
        mSongImage.setOval(true);
        updateBottomPlayer();

        mSongImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song song = mMusicPlayer.getCurrentPlaySong();
                if (song != null) {
                    if (!mMusicPlayer.isPlay(song)) {
                        mMusicPlayer.resume();
                    }
                    Intent i = new Intent(mActivity, SongActivity.class)
                            .putExtra(BaseSongFragment.EXTRA_SONG, song);
                    mActivity.startActivity(i);
                }
            }
        });
    }

    private View getView() {
        return mView;
    }

    public void updateView() {
        updateBottomPlayer();
        updateAlbumImage();
    }

    private void updateAlbumImage() {
        //LogHelper.d(TAG, "updateAlbumImage called");

        if (mMusicPlayer.getCurrentPlaySong() != null && mSongImage != null) {
            //LogHelper.d(TAG, "image =  " + mMusicPlayer.getCurrentPlaySong().getAlbum().getImage());
            //LogHelper.d(TAG , "mSongImage = " + mSongImage);
            Glide.with(mActivity).load(mMusicPlayer.getCurrentPlaySong().getAlbum().getImage()).asBitmap().into(mSongImage);
        }
    }

    public void attachToView(ViewGroup parent) {
        //LogHelper.d(TAG, "attachToView called");
        if (getView().getParent() == parent) {
            return;
        }
        if (getView().getParent() != null) {
            ((ViewGroup)getView().getParent()).removeView(getView());
        }
        parent.addView(getView());
        updateView();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        updateBottomPlayer();
        updateAlbumImage();
    }

    private void updateBottomPlayer() {
        if (StatusHelper.isPlayingForUI(mMusicPlayer)) {
            spinSongImage();
        } else {
            stopSpinSongImage();
        }
    }

    @Override
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    void spinSongImage()
    {
        if (mRotation == null)
            mRotation = (RotateAnimation)AnimationUtils.loadAnimation(mActivity, R.anim.rotate);
        mRotation.setDuration(10000);
        mSongImage.startAnimation(mRotation);
        mPlayImage.setVisibility(View.INVISIBLE);
    }

    void stopSpinSongImage() {
        if (mRotation == null) {
            return;
        }
        mRotation.cancel();
        mSongImage.setAnimation(null);
        mPlayImage.setVisibility(View.VISIBLE);
    }



}
