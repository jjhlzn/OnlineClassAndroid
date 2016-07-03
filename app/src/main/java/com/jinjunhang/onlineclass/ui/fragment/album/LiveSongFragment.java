package com.jinjunhang.onlineclass.ui.fragment.album;

import android.view.View;

import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.player.LivePlayerCell;

/**
 * Created by jjh on 2016-7-2.
 */
public class LiveSongFragment extends BaseSongFragment {

    @Override
    protected WebBrowserActivity.PlayerCell createPlayerCell() {
        return new LivePlayerCell(getActivity());
    }

    @Override
    protected View.OnClickListener createSendOnClickListener() {
        return null;
    }
}
