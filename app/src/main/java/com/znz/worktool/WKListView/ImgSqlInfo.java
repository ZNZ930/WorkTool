package com.znz.worktool.WKListView;

import android.graphics.drawable.Drawable;

import com.github.chrisbanes.photoview.PhotoView;

public class ImgSqlInfo {

    private String phid;
    private String time;
    private Drawable db;
    public ImgSqlInfo(Drawable db,String time) {
        this.db = db;
        this.time = time;
    }

    public Drawable getDb() {
        return db;
    }

    public void setDb(Drawable db) {
        this.db = db;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhid() {
        return phid;
    }

    public void setPhid(String phid) {
        this.phid = phid;
    }
}
