package com.jinjunhang.onlineclass.ui.lib;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;

/**
 * Created by jjh on 2016-7-15.
 */
public class ShareManager2 extends ShareManager {
    private final static String TAG = LogHelper.makeLogTag(ShareManager2.class);



    public ShareManager2(AppCompatActivity activity, View v) {
        super(activity, v);
    }

    protected void  setup() {
        View customView = mActivity.getSupportActionBar().getCustomView();
        ImageButton rightButton = (ImageButton) customView.findViewById(R.id.actionbar_right_button);
        rightButton.setImageResource(R.drawable.share);

        final View shareView = v.findViewById(R.id.share_view);


        View shareFriendButton = shareView.findViewById(R.id.weixinhaoyou_button);
        shareFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "share friends button Clicked");
                shareUrl(false);
            }
        });

        View pengyouquanButton = shareView.findViewById(R.id.pengyouquan_button);
        pengyouquanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "share pemgyouquan button Clicked");
                shareUrl(true);
            }
        });

        View weiboButton = shareView.findViewById(R.id.weibo_button);
        weiboButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "share weibo button Clicked");
                //mWeiboShareService.share();
                mWeiboShareService.share();
            }
        });

        View qqFriendsButton = shareView.findViewById(R.id.qqFriends_button);
        qqFriendsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mQQShareService.shareToFriends();
            }
        });

        View qzoneButton = shareView.findViewById(R.id.qzone_button);
        qzoneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mQQShareService.shareToQzone();
            }
        });

        View copyLinkButton = shareView.findViewById(R.id.copylink_button);
        copyLinkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                String url = ServiceLinkManager.ShareQrImageUrl() + "?userid=" + LoginUserDao.getInstance(CustomApplication.get()).get().getUserName();
                ClipData clip = ClipData.newPlainText("Text Label", url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
