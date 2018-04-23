package com.jinjunhang.onlineclass.ui.cell.me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.db.UserImageDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.service.ServiceConfiguration;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.activity.user.UserProfilePhotoActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.framework.lib.LogHelper;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by jjh on 2016-6-29.
 */
public class FirstSectionCell extends BaseListViewCell {
    private final static String TAG = LogHelper.makeLogTag(FirstSectionCell.class);

    private LoginUserDao mLoginUserDao;
    private RoundedImageView mUserImage;
    private TextView mNameLabel;
    private TextView mLevelLabel;
    private TextView mBossLabel;

    private Activity mActivity;

    private UserImageDao mUserImageDao;

    public FirstSectionCell(Activity activity) {
        super(activity);
        mLoginUserDao = LoginUserDao.getInstance(activity);
        mActivity = activity;
        mUserImageDao = UserImageDao.getInstance(activity.getApplicationContext());
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_me_first_section, null);

        mUserImage = (RoundedImageView) v.findViewById(R.id.user_image);
        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, UserProfilePhotoActivity.class);
                mActivity.startActivityForResult(i, MainActivity.REQUEST_ME_UPDATE_USER_IAMGE);
            }
        });
        mUserImage.setBorderColor(0x000000);
        mUserImage.setBorderWidth(2.0f);

        mNameLabel = (TextView) v.findViewById(R.id.name_label);
        mLevelLabel = (TextView) v.findViewById(R.id.level_label);
        mBossLabel = (TextView) v.findViewById(R.id.boss_label);

        update();
        return (LinearLayout)v.findViewById(R.id.root_container);
    }

    public void update() {
        LoginUser loginUser = mLoginUserDao.get();
        if (loginUser != null) {
            mNameLabel.setText(loginUser.getName());
            mLevelLabel.setText(loginUser.getLevel());
            mBossLabel.setText("我的上级: "+loginUser.getBoss());
            String url = ServiceConfiguration.GetUserProfileImage(loginUser.getUserName());
            //Glide.with(mActivity).load(url).asBitmap().into(mUserImage);
            //mUserImage.setImageResource(R.drawable.log);

            Bitmap userImage = mUserImageDao.get();
            if (userImage != null) {
                mUserImage.setImageBitmap(userImage);
            } else {
                Glide.with(mActivity).load(url).asBitmap().into(new BitmapImageViewTarget(mUserImage) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> animation) {
                        // here it's similar to RequestListener, but with less information (e.g. no model available)
                        //super.onResourceReady(resource, animation);
                        // here you can be sure it's already set
                        // mUserImage.set
                        // mUserImage.setImageResource(r);
                        mUserImage.setImageBitmap(resource);
                        mUserImageDao.saveOrUpdate( resource);
                        //mUserImage.setAlpha(0);
                        //mUserImage.setImageResource(R.drawable.avril);
                        LogHelper.d(TAG, "image is ready");
                    }

                    // +++++ OR +++++
                    @Override
                    protected void setResource(Bitmap resource) {
                    }
                });
            }
        }
    }
}
