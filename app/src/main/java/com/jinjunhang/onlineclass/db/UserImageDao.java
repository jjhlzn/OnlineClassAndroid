package com.jinjunhang.onlineclass.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jjh on 2016-6-30.
 */
public class UserImageDao {

    private static UserImageDao instance = null;
    private Context mContext;

    private UserImageDao(Context context) {
        mContext = context;
    }

    public synchronized static UserImageDao getInstance(Context ctx) {
        if (null == instance) {
            instance = new UserImageDao(ctx);
        }
        return instance;
    }


    public Bitmap get() {
        String name = "userprofile.png";
        try{
            FileInputStream fis = mContext.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        return null;
    }

    public void saveOrUpdate(Bitmap image) {
        String name = "userprofile.png";
        FileOutputStream out;
        try {
            out = mContext.openFileOutput(name, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
