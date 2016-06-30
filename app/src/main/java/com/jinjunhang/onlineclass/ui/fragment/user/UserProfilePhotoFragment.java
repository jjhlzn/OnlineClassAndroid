package com.jinjunhang.onlineclass.ui.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.UserImageDao;
import com.jinjunhang.onlineclass.ui.activity.user.UserProfilePhotoActivity;
import com.jinjunhang.onlineclass.ui.cell.CellClickListener;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.player.utils.LogHelper;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * Created by jjh on 2016-6-30.
 */
public class UserProfilePhotoFragment extends BaseFragment {

    private final static String TAG = LogHelper.makeLogTag(UserProfilePhotoFragment.class);

    private UserImageDao mUserImageDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_profileimage, container, false);
        mUserImageDao = UserImageDao.getInstance(getActivity().getApplicationContext());

        ImageView userImage = (ImageView) v.findViewById(R.id.user_image);
        userImage.setImageBitmap(mUserImageDao.get());

        ImageButton rightButton  = (ImageButton) ((UserProfilePhotoActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionbar_right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                            }
                        })
                        //.setContentHeight(100)
                        .setContentHolder(new ViewHolder(R.layout.select_image_from))

                        .create();

                /*
                dialog.findViewById(R.id.take_photo_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseListViewOnItemClickListener.onItemClickEffect(null, v, 0, 0);
                        LogHelper.d(TAG, "take photo");
                    }
                }); */

                dialog.findViewById(R.id.get_from_album_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseListViewOnItemClickListener.onItemClickEffect(null, v, 0, 0);
                        LogHelper.d(TAG, "get from album");
                    }
                });

                dialog.show();
            }
        });

        return v;

    }
}
