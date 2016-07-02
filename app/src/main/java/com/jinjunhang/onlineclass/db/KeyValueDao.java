package com.jinjunhang.onlineclass.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jinjunhang.onlineclass.model.LoginUser;

/**
 * Created by jjh on 2016-6-30.
 */
public class KeyValueDao {

    public static final String KEY_USER_JIFEN = "USER_JIFEN";
    public static final String KEY_USER_CAFIFU = "USER_CAIFU";
    public static final String KEY_USER_TEAMPEOPLE = "USER_TEAMPEOPLE";

    public static final String KEY_USER_MY_TUIJIAN = "USER_MY_TUIJIAN";
    public static final String KEY_USER_MY_ORDER = "USER_MY_ORDER";
    public static final String KEY_USER_MY_TEAM = "USER_MY_TEAM";

    public static final String SERVER_HTTP = "SERVER_HTTP";
    public static final String SERVER_HOST = "SERVER_HOST";
    public static final String SERVER_PORT = "SERVER_PORT";

    private DBOpenHelper dbOpenHelper;
    private static KeyValueDao instance = null;

    private KeyValueDao(Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    public synchronized static KeyValueDao getInstance(Context ctx) {
        if (null == instance) {
            instance = new KeyValueDao(ctx);
        }
        return instance;
    }

    public String getValue(String key, String defaultValue) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select M_KEY, M_VALUE from KEY_VALUE WHERE M_KEY = '" + key + "'" , null);
        if (cursor.getCount() == 0) {
            return defaultValue;
        }
        try {
            cursor.moveToFirst();
            return cursor.getString(1);
        } finally {
            cursor.close();;
        }
    }

    public void saveOrUpdate(String key, String value) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("M_KEY", key);
        values.put("M_VALUE", value);

        Cursor cursor = db.rawQuery("select M_KEY, M_VALUE from KEY_VALUE WHERE M_KEY = '" + key + "'" , null);

        try{
            if (cursor.getCount() == 0) {
                db.insert("KEY_VALUE", null, values);

            } else {
                db.update("KEY_VALUE", values, "", new String[]{});
            }
        } finally {
            cursor.close();
        }

    }
}
