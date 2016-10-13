package com.jinjunhang.onlineclass.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.framework.lib.LogHelper;

/**
 * Created by lzn on 16/6/27.
 */
public class LoginUserDao {
    private static final String TAG = LogHelper.makeLogTag(LoginUserDao.class);
    private DBOpenHelper dbOpenHelper;
    private static LoginUserDao instance = null;

    public LoginUserDao(Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    public synchronized static LoginUserDao getInstance(Context ctx) {
        if (null == instance) {
            instance = new LoginUserDao(ctx);
        }
        return instance;
    }

    public void save(LoginUser user) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("M_LEVEL", user.getLevel());
        values.put("M_CODE_IMAGE_URL", user.getCodeImageUrl());
        values.put("M_BOSS", user.getBoss());
        values.put("M_SEX", user.getSex());
        values.put("M_USER_NAME", user.getUserName());
        values.put("M_PASSWORD", user.getPassword());
        values.put("M_NAME", user.getName());
        values.put("M_NICK_NAME", user.getNickName());
        values.put("M_TOKEN", user.getToken());

        if (getCount() > 0) {
            db.update("LOGIN_USER", values, "", new String[]{});
        } else {
            db.insert("LOGIN_USER", null, values);
        }
    }

    public LoginUser get() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select M_USER_NAME, M_PASSWORD, M_LEVEL, M_CODE_IMAGE_URL, M_BOSS, M_SEX, M_NAME, M_NICK_NAME, M_TOKEN from LOGIN_USER", null);
        if (cursor.getCount() == 0) {
            LogHelper.d(TAG, "no login user found");
            return null;
        }
        try {
            cursor.moveToFirst();
            LoginUser user = new LoginUser();
            user.setUserName(cursor.getString(0));
            user.setPassword(cursor.getString(1));
            user.setLevel(cursor.getString(2));
            user.setCodeImageUrl(cursor.getString(3));
            user.setBoss(cursor.getString(4));
            user.setSex(cursor.getString(5));
            user.setName(cursor.getString(6));
            user.setNickName(cursor.getString(7));
            user.setToken(cursor.getString(8));
            LogHelper.d(TAG, user.getUserName() + " user found");
            return user;
        } finally {
            cursor.close();

        }
    }

    public void deleteAll() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete("LOGIN_USER", "", null);

    }


    private int getCount() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from LOGIN_USER", null);
        try {
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            cursor.close();

        }
    }
}
