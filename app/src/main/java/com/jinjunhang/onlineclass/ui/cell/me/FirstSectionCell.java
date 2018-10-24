package com.jinjunhang.onlineclass.ui.cell.me;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
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
import com.jinjunhang.onlineclass.ui.fragment.user.MeFragment;
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

    private Activity mActivity;
    private MeFragment mFragment;
    private UserImageDao mUserImageDao;

    @Override
    public int getItemViewType() {
        return BaseListViewCell.USER_FIRST_SECTION_CELL;
    }

    public FirstSectionCell(Activity activity, MeFragment fragment) {
        super(activity);
        this.mFragment = fragment;
        mLoginUserDao = LoginUserDao.getInstance(activity);
        mActivity = activity;
        mUserImageDao = UserImageDao.getInstance(activity.getApplicationContext());
    }

    @Override
    public ViewGroup getView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_me_first_section, null);

        mUserImage =  v.findViewById(R.id.user_image);
        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, UserProfilePhotoActivity.class);
                mFragment.startActivityForResult(i, MainActivity.REQUEST_ME_UPDATE_USER_IAMGE);
            }
        });
        mUserImage.setBorderColor(0x000000);
        mUserImage.setBorderWidth(2.0f);
        mUserImage.setOval(true);

        mNameLabel = (TextView) v.findViewById(R.id.name_label);
        mLevelLabel = (TextView) v.findViewById(R.id.level_label);

        update();
        return (LinearLayout)v.findViewById(R.id.root_container);
    }



    public void update() {
        LoginUser loginUser = mLoginUserDao.get();
        if (loginUser != null) {
            mNameLabel.setText(loginUser.getName());
            mLevelLabel.setText(loginUser.getLevel());

            String url = ServiceConfiguration.GetUserProfileImage(loginUser.getUserName());

            LogHelper.d(TAG, url);

            if (mUserImageDao.get() != null) {
                mUserImage.setImageBitmap(mUserImageDao.get());
            } else {
                Glide.with(mActivity).load(url).asBitmap().into(new BitmapImageViewTarget(mUserImage) {
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> animation) {
                        mUserImage.setImageBitmap(resource);
                        //mUserImageDao.saveOrUpdate(resource);
                        LogHelper.d(TAG, "image is ready");
                        mFragment.notifyListViewUpdate();
                    }
                });
            }
        }
    }
}
