package com.jinjunhang.onlineclass.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jjh on 2016-6-30.
 */
public class ExtendFunctionImageDao {
    private static final String TAG = LogHelper.makeLogTag(ExtendFunctionImageDao.class);
    private static ExtendFunctionImageDao instance = null;
    private Context mContext;

    private ExtendFunctionImageDao(Context context) {
        mContext = context;
    }

    public synchronized static ExtendFunctionImageDao getInstance(Context ctx) {
        if (null == instance) {
            instance = new ExtendFunctionImageDao(ctx);
        }
        return instance;
    }

    private String MD5(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    private String getFileName(String url) {
        return MD5(url) + ".png";
    }

    public Bitmap get(String url) {
        try{
            String fileName = getFileName(url);
            //LogHelper.d(TAG, url + " -> " + fileName);
            FileInputStream fis = mContext.openFileInput(fileName);
            Bitmap b = BitmapFactory.decodeStream(fis);
            Bitmap mutableBitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            fis.close();
            return mutableBitmap;
        }
        catch(Exception e){
            LogHelper.d(TAG, e);
        }
        return null;
    }

    public void saveOrUpdate(String url, Bitmap image) {
        FileOutputStream out;
        try {
            out = mContext.openFileOutput(getFileName(url), Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            LogHelper.d(TAG, e);
        }
    }

}
