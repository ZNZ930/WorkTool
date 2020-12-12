package com.znz.worktool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

public class WkTabActivity extends TabActivity {

    private TabHost tabHost;
    private TabHost.TabSpec spec1;
    private Context mContext = WkTabActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wk_tab);
        initView();
    }

    private void initView()
    {
        tabHost = getTabHost();
        tabHost.setup();
        spec1 = tabHost.newTabSpec("t1");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.gn_on));
        spec1.setContent(new Intent().setClass(this,MainActivity.class));
        tabHost.addTab(spec1);
        //tabHost.addTab(tabHost.newTabSpec("t1").setIndicator("", getResources().getDrawable(R.drawable.gn_off)).setContent(new Intent().setClass(this,MainActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("t2").setIndicator("",getResources().getDrawable(R.drawable.jl_on)).setContent(new Intent().setClass(mContext,DataRecord.class)));
        tabHost.addTab(tabHost.newTabSpec("t3").setIndicator("",getResources().getDrawable(R.drawable.ph_on)).setContent(new Intent().setClass(mContext,ImageSqlActivity.class)));

    }

}
