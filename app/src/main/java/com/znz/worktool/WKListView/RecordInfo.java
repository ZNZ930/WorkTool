package com.znz.worktool.WKListView;

import android.content.Context;

public class RecordInfo {

    private String Rdizhi;
    private String Rfuwulx;

    public RecordInfo(String Rdizhi,String Rfuwulx)
    {
        this.Rdizhi =Rdizhi;
        this.Rfuwulx = Rfuwulx;
    }

    public String getRdizhi() {
        return Rdizhi;
    }

    public void setRdizhi(String rdizhi) {
        Rdizhi = rdizhi;
    }

    public String getRfuwulx() {
        return Rfuwulx;
    }

    public void setRfuwulx(String rfuwulx) {
        Rfuwulx = rfuwulx;
    }
}
