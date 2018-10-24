package com.jinjunhang.onlineclass.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import com.jinjunhang.framework.lib.LogHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jjh on 2016-6-30.
 */
public class UserImageDao {

    private final String TAG = LogHelper.makeLogTag(UserImageDao.class);
    private static UserImageDao instance = null;
    private Context mContext;
    private Bitmap mBitmap;

    private UserImageDao(Context context) {
        mContext = context;
    }

    public synchronized static UserImageDao getInstance(Context ctx) {
        if (null == instance) {
            instance = new UserImageDao(ctx);
        }
        return instance;
    }


    private String name = "userprofile.png";
    public Bitmap get() {
        if (mBitmap != null)
            return mBitmap;
        try{
            FileInputStream fis = mContext.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            mBitmap = b;
            return b;
        }
        catch(Exception e){
            LogHelper.e(TAG, e);
        }
        return null;
    }

    public void saveOrUpdate(Bitmap image) {
        FileOutputStream out;
        try {
            out = mContext.openFileOutput(name, Context.MODE_PRIVATE);

            image.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();

            mBitmap = image;
        } catch (Exception e) {
            LogHelper.e(TAG, e);
        }
    }

}
