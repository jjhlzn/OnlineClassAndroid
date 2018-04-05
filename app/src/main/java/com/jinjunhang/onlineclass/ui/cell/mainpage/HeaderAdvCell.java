package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.HeaderAdvManager;
import com.jinjunhang.onlineclass.model.LiveSong;
import com.jinjunhang.onlineclass.model.Song;
import com.jinjunhang.onlineclass.service.GetHeaderAdvResponse;
import com.jinjunhang.onlineclass.service.GetSongInfoRequest;
import com.jinjunhang.onlineclass.service.GetSongInfoResponse;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumListActivity;
import com.jinjunhang.onlineclass.ui.activity.album.SongActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.fragment.album.BaseSongFragment;
import com.jinjunhang.player.ExoPlayerNotificationManager;
import com.jinjunhang.player.MusicPlayer;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 17/1/6.
 */

public class HeaderAdvCell extends BaseListViewCell implements XBanner.XBannerAdapter {
    private static final String TAG = LogHelper.makeLogTag(HeaderAdvCell.class);

    private ImageView mImageView;
    private LoadingAnimation mLoading;

    public HeaderAdvCell(Activity activity, LoadingAnimation loading) {
        super(activity);
        this.mLoading = loading;
    }

    List<String> imgesUrl = new ArrayList<>();
    public void loadBanner(XBanner var1, Object var2, View view, int position) {
        Glide.with(mActivity).load(imgesUrl.get(position)).into((ImageView) view);
    }

    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_header, null);

        XBanner mXBanner = (XBanner)view.findViewById(R.id.xbanner);

        imgesUrl = new ArrayList<>();
        imgesUrl.add("http://imageprocess.yitos.net/images/public/20160910/99381473502384338.jpg");
        imgesUrl.add("http://imageprocess.yitos.net/images/public/20160910/77991473496077677.jpg");
        imgesUrl.add("http://imageprocess.yitos.net/images/public/20160906/1291473163104906.jpg");

        //添加广告数据
        mXBanner.setData(imgesUrl,null);//第二个参数为提示文字资源集合

        mXBanner.startAutoPlay();

        LogHelper.d(TAG, "set xbanner");
        mXBanner.setmAdapter(this);


        /*
        HeaderAdvManager manager = HeaderAdvManager.getInstance();
        final GetHeaderAdvResponse.HeaderAdvImage adv = manager.getHeaderAdv();

        mImageView = (ImageView) view.findViewById(R.id.list_mainpage_header_image);
        if (!"".equals(adv.getImageUrl())) {
            Glide.with(mActivity).load(adv.getImageUrl()).into(mImageView);

        }
        */

        /*
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogHelper.d(TAG, "head adv cell clicked");
                if (GetHeaderAdvResponse.HeaderAdvImage.TYPE_SONG.equals(adv.getType())) {
                    //new GetSongInfoTask().execute();
                } else {
                    Intent i = new Intent(mActivity, AlbumListActivity.class);
                    mActivity.startActivity(i);
                }
            }
        }); */

        return (LinearLayout)view.findViewById(R.id.list_item_viewgroup);
    }

    /*
    private class GetSongInfoTask extends AsyncTask<Void, Void, GetSongInfoResponse> {
        @Override
        protected GetSongInfoResponse doInBackground(Void... params) {
            Song song = new Song();
            HeaderAdvManager manager = HeaderAdvManager.getInstance();
            final GetHeaderAdvResponse.HeaderAdvImage adv = manager.getHeaderAdv();
            if (GetHeaderAdvResponse.HeaderAdvImage.TYPE_SONG.equals(adv.getType())) {
                song.setId(adv.getParams().get(GetHeaderAdvResponse.HeaderAdvImage.PARAM_KEY_SONG));
                GetSongInfoRequest request = new GetSongInfoRequest(song);
                return new BasicService().sendRequest(request);
            } else {
                GetSongInfoResponse resp = new GetSongInfoResponse();
                resp.setStatus(ServerResponse.FAIL);
                resp.setErrorMessage("");
                return resp;
            }
        }

        @Override
        protected void onPostExecute(GetSongInfoResponse resp) {
            super.onPostExecute(resp);
            mLoading.hide();
            if (resp.getStatus() == ServerResponse.NO_PERMISSION) {
                Utils.showVipBuyMessage(mActivity, resp.getErrorMessage());
                return;
            }

            if (resp.isSuccess()) {
                MusicPlayer musicPlayer = MusicPlayer.getInstance(mActivity);
                ExoPlayerNotificationManager notificationManager = ExoPlayerNotificationManager.getInstance(mActivity);
                LiveSong song = (LiveSong) resp.getSong();
                List<Song> songs = new ArrayList<>();
                songs.add(song);
                if (!musicPlayer.isPlay(song)) {
                    musicPlayer.pause();
                    musicPlayer.play(songs, 0);
                    notificationManager.display();
                }
                Intent i = new Intent(mActivity, SongActivity.class)
                        .putExtra(BaseSongFragment.EXTRA_SONG, song);
                mActivity.startActivity(i);
            } else {
                Utils.showErrorMessage(mActivity, resp.getErrorMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("");
        }
    } */
}
