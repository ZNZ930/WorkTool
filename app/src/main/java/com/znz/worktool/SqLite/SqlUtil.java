package com.znz.worktool.SqLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SqlUtil {
    private SqOpenHelp sqOpenHelp;
    public  SqlUtil(Context mContext)
    {
        sqOpenHelp = new SqOpenHelp(mContext, "work.db", null, 1);
    }

    public synchronized SqOpenHelp getSqOpenHelp() {
        return sqOpenHelp;
    }

    public synchronized SQLiteDatabase getReadOpenhelp()
    {
        return sqOpenHelp.getReadableDatabase();
    }
    public synchronized SQLiteDatabase getWriteOpenhelp()
    {
        return sqOpenHelp.getWritableDatabase();
    }
}
