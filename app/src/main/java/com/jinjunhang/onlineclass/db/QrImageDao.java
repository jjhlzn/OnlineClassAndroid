package com.jinjunhang.onlineclass.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by jjh on 2016-6-30.
 */
public class QrImageDao {

    private static QrImageDao instance = null;
    private Context mContext;

    private QrImageDao(Context context) {
        mContext = context;
    }

    public synchronized static QrImageDao getInstance(Context ctx) {
        if (null == instance) {
            instance = new QrImageDao(ctx);
        }
        return instance;
    }

    public Bitmap get() {
        String name = "userqrcode.png";
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
        String name = "userqrcode.png";
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
