package com.jinjunhang.onlineclass.ui.fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.UserImageDao;
import com.jinjunhang.onlineclass.service.ServiceConfiguration;
import com.jinjunhang.onlineclass.ui.activity.user.UserProfilePhotoActivity;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.jinjunhang.framework.lib.LogHelper;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.soundcloud.android.crop.Crop;

import java.io.File;

/**
 * Created by jjh on 2016-6-30.
 */
public class UserProfilePhotoFragment extends BaseFragment {

    private final static String TAG = LogHelper.makeLogTag(UserProfilePhotoFragment.class);

    private UserImageDao mUserImageDao;
    private ImageView mImageView;
    private LoadingAnimation mLoading;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        LogHelper.d(TAG, "on Activity Result called");
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            beginCrop(result.getData());

        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
            //上传到服务器，并且保存到本地

            try {
                Uri imageUri = Crop.getOutput(result);
                Bitmap bitmap =  MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                UserImageDao userImageDao = UserImageDao.getInstance(getActivity());
                userImageDao.saveOrUpdate(bitmap);

                File file = new File(imageUri.getPath());
                new UploadUserImageTask().execute(file);
            }catch (Exception ex) {
                LogHelper.e(TAG, ex);
            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getActivity());
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            mImageView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_profileimage, container, false);
        mUserImageDao = UserImageDao.getInstance(getActivity().getApplicationContext());
        mLoading = new LoadingAnimation(getActivity());

        mImageView = (ImageView) v.findViewById(R.id.user_image);
        mImageView.setImageBitmap(mUserImageDao.get());

        ImageButton rightButton  = (ImageButton) ((UserProfilePhotoActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionbar_right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                            }
                        })
                        .setContentHolder(new ViewHolder(R.layout.select_image_from))
                        .create();

                dialog.findViewById(R.id.get_from_album_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseListViewOnItemClickListener.onItemClickEffect(null, v, 0, 0);
                        LogHelper.d(TAG, "get from album");
                        Crop.pickImage(getActivity());
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });



        return v;

    }

    private class UploadUserImageTask extends AsyncTask<File, Void, ServerResponse> {
        @Override
        protected ServerResponse doInBackground(File... params) {
            return new BasicService().upload(ServiceConfiguration.UploadProfileImageUrl(), params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("正在上传头像");
        }

        @Override
        protected void onPostExecute(ServerResponse serverResponse) {
            super.onPostExecute(serverResponse);
            mLoading.hide();
            if (serverResponse.getStatus() == ServerResponse.SUCCESS) {
                Utils.showMessage(getActivity(), "头像更新成功");
            } else {
                Utils.showMessage(getActivity(), serverResponse.getErrorMessage());
            }
        }
    }


}
