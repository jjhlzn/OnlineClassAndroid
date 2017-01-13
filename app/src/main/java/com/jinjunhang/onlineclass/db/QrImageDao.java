package com.jinjunhang.onlineclass.db;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by jjh on 2016-6-30.
 */
public class QrImageDao {
    private static final String TAG = LogHelper.makeLogTag(QrImageDao.class);
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

    private String qrImageName = "qrimage.png";
    public String getCodeImageUrl() {
        try {
            File imageFile = getQrImageFilePath();
            if (!imageFile.exists()) {
                if (saveCodeImage()) {
                    return imageFile.getAbsolutePath();
                } else {
                    return "";
                }
            }
            return imageFile.getAbsolutePath();
        }catch (Exception ex) {
            LogHelper.e(TAG, ex);
            return "";
        }
    }

    private boolean saveCodeImage() {
        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.me_qrcode);
        return saveBitmapToFile(qrImageName, bm, Bitmap.CompressFormat.PNG, 100);

    }

    private File getQrImageFilePath() {
        File dir = Environment.getExternalStorageDirectory();
        File imageFile = new File(dir, qrImageName);
        return imageFile;
    }

    private boolean saveBitmapToFile( String fileName, Bitmap bm,
                                     Bitmap.CompressFormat format, int quality) {
        File dir = Environment.getExternalStorageDirectory();

        File imageFile = new File(dir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);

            bm.compress(format,quality,fos);

            fos.close();

            return true;
        }
        catch (IOException e) {
            LogHelper.e("app",e.getMessage());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }


}
