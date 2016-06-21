package com.jinjunhang.onlineclass.ui.lib;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.StatusHelper;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by lzn on 16/6/21.
 */
public class BottomPlayerController implements ExoPlayer.Listener   {
    private Activity mActivity;
    private ImageView mPlayImage;
    private RoundedImageView mSongImage;
    private Animation mRotation;

    private MusicPlayer mMusicPlayer;

    private View mView;


    public BottomPlayerController(Activity activity) {
        mActivity = activity;
        mMusicPlayer = MusicPlayer.getInstance(activity);
        //ViewGroup layout = (ViewGroup) activity.findViewById(R.id.fragmentContainer);
        mView = activity.getLayoutInflater().inflate(R.layout.bottom_player, null);
        mPlayImage = (ImageView) mView.findViewById(R.id.bottomPlayer_playImage);
        mSongImage = (RoundedImageView) mView.findViewById(R.id.bottomPlayer_imageButton);
        mSongImage.setOval(true);
        updateBottomPlayer();
    }

    public View getView() {
        return mView;
    }

    public void updateView() {
        updateBottomPlayer();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        updateBottomPlayer();
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
            mRotation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate);
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
