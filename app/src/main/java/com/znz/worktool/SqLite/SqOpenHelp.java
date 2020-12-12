package com.znz.worktool.SqLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SqOpenHelp extends SQLiteOpenHelper {
    private static final String SQl = "create table worktool(" +
            "id integer primary key," +
            "time text," +
            "dizhi text," +
            "fuwulx text," +
            "fuwumx text," +
            "zhifufs text," +
            "name text," +
            "phone text)";
    public SqOpenHelp( Context context,  String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
