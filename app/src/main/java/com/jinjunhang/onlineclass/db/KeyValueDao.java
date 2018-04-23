package com.jinjunhang.onlineclass.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jinjunhang.framework.lib.LogHelper;

/**
 * Created by jjh on 2016-6-30.
 */
public class KeyValueDao {
    private static final String TAG = LogHelper.makeLogTag(KeyValueDao.class);

    public static final String KEY_USER_JIFEN = "USER_JIFEN";
    public static final String KEY_USER_CAFIFU = "USER_CAIFU";
    public static final String KEY_USER_TEAMPEOPLE = "USER_TEAMPEOPLE";

    public static final String KEY_USER_VIP_END_DATE = "USER_VIP_END_DATE";
    public static final String KEY_USER_AGENT_LEVEL = "USER_AGENT_LEVEL";

    public static final String KEY_USER_MY_TUIJIAN = "USER_MY_TUIJIAN";
    public static final String KEY_USER_MY_ORDER = "USER_MY_ORDER";
    public static final String KEY_USER_MY_TEAM = "USER_MY_TEAM";

    public static final String SERVER_HTTP = "SERVER_HTTP";
    public static final String SERVER_HOST = "SERVER_HOST";
    public static final String SERVER_PORT = "SERVER_PORT";
    public static final String IS_GET_SERVICE_LOCATOR = "IS_GET_SERVICE_LOCATOR";

    public static final String BOTTOM_BAR_HEIGHT = "bottom_bar_height";

    private DBOpenHelper dbOpenHelper;

    private static KeyValueDao instance;

    private KeyValueDao(Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    public synchronized static KeyValueDao getInstance(Context ctx) {
        if (instance == null) {
            instance = new KeyValueDao(ctx);
        }
        return  instance;
    }

    public void deleteAll() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete("KEY_VALUE", "", null);
    }
    public void getAll() {

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select M_KEY, M_VALUE from KEY_VALUE" , null);

        try {
            if(cursor.moveToFirst()){
                do{
                    String column1 = cursor.getString(0);
                    String column2 = cursor.getString(1);
                    //LogHelper.d(TAG, "key = " + column1 + ", value = " + column2);

                }while(cursor.moveToNext());
            }
        } catch (Exception ex) {
            LogHelper.e(TAG, ex);

        } finally {
            cursor.close();;
        }
    }

    public String getValue(String key, String defaultValue) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select M_KEY, M_VALUE from KEY_VALUE WHERE M_KEY = '" + key + "'" , null);
        //LogHelper.d(TAG, "select M_KEY, M_VALUE from KEY_VALUE WHERE M_KEY = '" + key + "'");
        if (cursor.getCount() == 0) {
            //LogHelper.d(TAG, "count = " + cursor.getCount());
            return defaultValue;
        }
        try {
            cursor.moveToFirst();
            String value = cursor.getString(1);
            //LogHelper.i(TAG, "result = " + value);
            return value;
        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
            return defaultValue;
        } finally {
            cursor.close();;

        }
    }

    public void saveOrUpdate(String key, String value) {
        //LogHelper.i(TAG, "db.saveOrUpdate: key = " + key + ", value = " + value);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("M_KEY", key);
        values.put("M_VALUE", value);

        try{
            if (getCount("select count(*) from KEY_VALUE WHERE M_KEY = '" + key + "'") == 0) {
                db.insert("KEY_VALUE", null, values);
                //LogHelper.i(TAG, "insert key: " + key + ", value: " + value);
            } else {
                db.update("KEY_VALUE", values, "M_KEY = '"+key+"'", new String[]{});
                //LogHelper.i(TAG, "update key: " + key + ", value: " + value);
            }
           // getValue(key, value);

        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        } finally {
        }
    }

    private int getCount(String sql) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            cursor.close();

        }
    }

}
