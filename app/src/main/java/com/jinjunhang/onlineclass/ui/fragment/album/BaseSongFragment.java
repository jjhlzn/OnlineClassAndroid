package com.jinjunhang.onlineclass.ui.fragment.album;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCellAdapter;
import com.jinjunhang.onlineclass.ui.cell.WideSectionSeparatorCell;
import com.jinjunhang.onlineclass.ui.cell.player.PlayerCell;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.EmojiKeyboard;
import com.jinjunhang.player.MusicPlayer;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjh on 2016-7-2.
 */
public abstract class BaseSongFragment  extends BaseFragment {
    private final static String TAG = LogHelper.makeLogTag(BaseSongFragment.class);
    public final static String EXTRA_SONG = "SongFragement_song";

    protected Song mSong;
    protected MusicPlayer mMusicPlayer;


    protected ListViewCellAdapter mAdapter;

    protected PlayerCell mPlayerCell;

    protected View mCommentTip;
    protected View mCommentWindow;
    protected EditText mCommentEditText;

    abstract protected PlayerCell createPlayerCell();
    abstract protected  View.OnClickListener createSendOnClickListener();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        mMusicPlayer = MusicPlayer.getInstance(getActivity());
        mSong = mMusicPlayer.getCurrentPlaySong();
        setTitle(mSong.getName());

        View v = inflater.inflate(R.layout.activity_fragment_play_song, container, false);

        v.findViewById(R.id.swipe_refresh_layout).setEnabled(false);

        //control comment editor
        mCommentTip = v.findViewById(R.id.bottom_comment_tip);
        mCommentTip.setVisibility(View.VISIBLE);
        mCommentWindow = v.findViewById(R.id.bottom_comment);
        mCommentEditText = (EditText) v.findViewById(R.id.comment_edittext);
        mCommentTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentWindow.setVisibility(View.VISIBLE);
                mCommentEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mCommentEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        TextView sendButton = (TextView)v.findViewById(R.id.send_button);
        sendButton.setOnClickListener(createSendOnClickListener());

        final ViewGroup emojiKeyBoardView = (ViewGroup)  v.findViewById(R.id.emojikeyboard);
        final View emojiKeyboard  = new EmojiKeyboard(getActivity()).getView();
        emojiKeyBoardView.addView(emojiKeyboard);

        //设置emoji切换按钮
        ImageButton keyboardSwitchButton = (ImageButton) v.findViewById(R.id.emojikeyboard_switch_button);
        keyboardSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideSoftKeyboard(getActivity());
                LogHelper.d(TAG, "emojiboard will show");
                emojiKeyBoardView.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCommentWindow.getLayoutParams();
                params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                //params.
            }
        });

        ListView mListView = (ListView) v.findViewById(R.id.listView);
        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        createAdapter();
        mListView.setAdapter(mAdapter);



        setupUI4HideKeybaord(v, getActivity());
        return v;
    }


    protected void closeCommentWindow() {
        Utils.hideSoftKeyboard(getActivity());
        mCommentWindow.setVisibility(View.INVISIBLE);
    }

    public void setupUI4HideKeybaord(View view, final Activity activity) {
        //Set up touch listener for non-text box views to hide keyboard.
        LogHelper.d(TAG, "id = " + view.getId() + ", bottom_comment.id = " + R.id.bottom_comment);
        if (view.getId() == R.id.bottom_comment) {
            return;
        }
        if(!(view instanceof EditText)) {
            LogHelper.d(TAG, "register onTouchListener for id = " + view.getId());
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    LogHelper.d(TAG, "onTouch called");
                   closeCommentWindow();
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI4HideKeybaord(innerView, activity);
            }
        }
    }

    private void createAdapter() {
        ArrayList<ListViewCell> viewCells = new ArrayList<>();
        mPlayerCell = createPlayerCell();
        mPlayerCell.setSong(mMusicPlayer.getCurrentPlaySong());
        viewCells.add(mPlayerCell);

        viewCells.add(new WideSectionSeparatorCell(getActivity()));

        mAdapter = new ListViewCellAdapter(getActivity(), viewCells);
        mPlayerCell.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMusicPlayer.addListener(mPlayerCell);
    }

    @Override
    public void onStop() {
        super.onStop();
        mMusicPlayer.removeListener(mPlayerCell);
    }



}

