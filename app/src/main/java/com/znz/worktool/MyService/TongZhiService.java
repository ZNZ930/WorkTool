package com.znz.worktool.MyService;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.znz.worktool.WkNotification.WorkNtn;

public class TongZhiService extends Service {
    private String TAG ="ZNZ";
    private MyBinder myBinder = new MyBinder();
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        Log.i(TAG,".....onCreate()......");
        WorkNtn workNtn = new WorkNtn(getApplication());
        workNtn.setChannelldName("服务通知");
        workNtn.SetChannelld("fuwutz");
        workNtn.creatNitification(NotificationManager.IMPORTANCE_HIGH);
        workNtn.SetNotification(1);
        Log.i("TTT",".....onStartCommand....");
        startForeground(1,workNtn.getNotification());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        WorkNtn workNtn = new WorkNtn(getApplication());
//        workNtn.setChannelldName("服务通知");
//        workNtn.SetChannelld("fuwutz");
//        workNtn.creatNitification(NotificationManager.IMPORTANCE_HIGH);
//        workNtn.SetNotification(1);
//        Log.i("TTT",".....onStartCommand....");
//        startForeground(1,workNtn.getNotification());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG,".....onUnbind()......");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,".....onDestroy()......");
        super.onDestroy();
    }

    public  class MyBinder extends Binder
    {

    }
}
