package com.jinjunhang.onlineclass.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context) {
        super(context, "onlineclassandroid.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE LOGIN_USER (ID integer primary key autoincrement," +
                "M_LEVEL nvarchar(1000)," +
                "M_CODE_IMAGE_URL nvarchar(1000), " +
                "M_BOSS nvarchar(1000)," +
                "M_SEX nvarchar(1000)," +
                "M_USER_NAME nvarchar(1000)," +
                "M_PASSWORD nvarchar(1000)," +
                "M_NAME nvarchar(1000)," +
                "M_NICK_NAME nvarchar(1000)," +
                "M_TOKEN nvarchar(1000))");

        db.execSQL("CREATE TABLE KEY_VALUE (ID integer primary key autoincrement," +
                "M_KEY nvarchar(1000)," +
                "M_VALUE nvarchar(5000))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
