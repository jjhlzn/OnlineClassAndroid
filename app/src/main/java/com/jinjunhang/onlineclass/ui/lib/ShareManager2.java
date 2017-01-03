package com.jinjunhang.onlineclass.ui.lib;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.framework.lib.LogHelper;

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
                mWeiboShareService.share();
            }
        });
    }


}
