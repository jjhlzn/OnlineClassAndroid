package com.jinjunhang.onlineclass.ui.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.MainPageFragment;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;
import com.jinjunhang.player.utils.StatusHelper;
import com.makeramen.roundedimageview.RoundedImageView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements ExoPlayer.Listener {

    private static final String TAG = "MainActivity";

    private Animation mRotation;
    private RoundedImageView mSongImage;
    private ImageView mPlayImage;
    private MusicPlayer mMusicPlayer = MusicPlayer.getInstance(this);

    private BottomBar mBottomBar;
    private Map<Class, Fragment> fragmentMap;

    public final static String EXTRA_TAB = "selecttab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentMap = new HashMap();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setMaxFixedTabs(5);
        mBottomBar.setItemsFromMenu(R.menu.bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                String title = "";
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = null;
                switch (menuItemId) {
                    case R.id.bottomBarHome:
                        title = "主页";
                        fragment = getFragment(MainPageFragment.class);

                        break;
                    case R.id.bottomBarSearch:
                        title = "搜索";


                        break;
                    case R.id.bottomBarMe:
                        title = "我";

                        break;
                }

                if (fragment != null) {
                    android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText(title);
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        mBottomBar.noTopOffset();
        //mBottomBar.hideShadow();

        int selectTab = getIntent().getIntExtra(EXTRA_TAB, 0);
        Log.d(TAG, "selectTab = " + selectTab);
        mBottomBar.selectTabAtPosition(selectTab, true);


        ViewGroup layout = (ViewGroup) findViewById(R.id.fragmentContainer);
        View bottomPlayer = getLayoutInflater().inflate(R.layout.bottom_player, layout, false);
        mPlayImage = (ImageView) bottomPlayer.findViewById(R.id.bottomPlayer_playImage);
        mSongImage = (RoundedImageView) bottomPlayer.findViewById(R.id.bottomPlayer_imageButton);
        mSongImage.setOval(true);
        mBottomBar.addView(bottomPlayer);
        updateBottomPlayer();
    }

    @Override
    protected void onResume() {
        LogHelper.d(TAG, "onResume called");
        super.onResume();
        mMusicPlayer.addListener(this);
        updateBottomPlayer();
    }

    @Override
    protected void onStop() {
        LogHelper.d(TAG, "onStop called");
        super.onStop();
        mMusicPlayer.removeListener(this );
        updateBottomPlayer();
    }

    private <T extends Fragment> Fragment getFragment(Class<T> fragmentClass) {
        Fragment fragment = fragmentMap.get(fragmentClass);
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
            } catch (Exception ex) {

            }
            fragmentMap.put(fragmentClass, fragment);
        }
        return fragment;
    }

    void spinSongImage()
    {
        if (mRotation == null)
            mRotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        //rotation.setAnimationListener(listener);
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
}
