package com.jinjunhang.onlineclass.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.media.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jjh on 2016-6-30.
 */
public class UserImageDao {

    public void get() {

    }

    public void saveOrUpdate(Context context, Image image) {

        /*
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath(); */
    }

}
