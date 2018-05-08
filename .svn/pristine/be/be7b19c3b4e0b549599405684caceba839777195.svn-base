package com.zplh.zplh_android_yk.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/15.
 */

public class MyListDB extends SQLiteOpenHelper {
    private static final String DATEBASE_NAME="list_data.db";
    private static final int VERSION=5;
    public MyListDB(Context context) {
        super(context, DATEBASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table list_mydata(login_id integer,times TEXT,ali_add_num_s integer,ali_add_num_e integer,ali_version integer,interval_time TEXT,contact_verify_msg TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS list_mydata");
        onCreate(db);
    }
}
