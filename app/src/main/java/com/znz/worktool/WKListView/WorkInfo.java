package com.znz.worktool.WKListView;

public class WorkInfo {
    private int id;
    private String time;
    private String dizhi;
//    private String fuwulx;
    private String fuwumx;
//    private String zhifufs;
//    private String name;
//    private String phone;

    public WorkInfo(int id,String time,String dizhi,String fuwumx)
    {
        this.id = id;
        this.time = time;
        this.dizhi = dizhi;
        this.fuwumx = fuwumx;
    }

    public String getFuwumx() {
        return fuwumx;
    }

    public void setFuwumx(String fuwumx) {
        this.fuwumx = fuwumx;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDizhi() {
        return dizhi;
    }

    public void setDizhi(String dizhi) {
        this.dizhi = dizhi;
    }

//    public String getFuwulx() {
//        return fuwulx;
//    }
//
//    public void setFuwulx(String fuwulx) {
//        this.fuwulx = fuwulx;
//    }
//
//    public String getFuwumx() {
//        return fuwumx;
//    }
//
//    public void setFuwumx(String fuwumx) {
//        this.fuwumx = fuwumx;
//    }
//
//    public String getZhifufs() {
//        return zhifufs;
//    }
//
//    public void setZhifufs(String zhifufs) {
//        this.zhifufs = zhifufs;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
}
