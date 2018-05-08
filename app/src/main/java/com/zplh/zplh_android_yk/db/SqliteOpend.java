package com.zplh.zplh_android_yk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/6/30.
 */

public class SqliteOpend extends SQLiteOpenHelper{
    private static final String DATEBASE_NAME="state.db";
    private static final int VERSION=7;

    public SqliteOpend(Context context) {
        super(context, DATEBASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table states(id_task integer,login_id integer,result TEXT,times TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS states");
        onCreate(sqLiteDatabase);
    }
}
