package com.znz.worktool.WkNotification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.znz.worktool.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkNtn {
    private Context mContext;
    private Notification notification ;
    private NotificationManager notificationManager;
    private String channelld;
    private String channelldName;

 //   public WorkNtn(Context mContext,NotificationManager  notificationManager)
    public WorkNtn(Context mContext)
    {
        this.mContext = mContext;
        notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    //设置通知内容
    @SuppressLint("WrongConstant")
    public void SetNotification(int id)
    {
        notification = new NotificationCompat.Builder(mContext,getChannelld())
                .setAutoCancel(true)
                .setContentTitle("收到服务通知")
                .setContentText("距离下个服务还有二十分钟")
                .setSmallIcon(R.drawable.fuwu)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.fuwu))
                .build();
        notificationManager.notify(id,notification);
    }
    //设置channelld
    public void SetChannelld(String channelld)
    {
        this.channelld = channelld;
    }
    //获取channelld
    public String getChannelld() {
        return channelld;
    }

    public String getChannelldName() {
        return channelldName;
    }

    public void setChannelldName(String channelldName) {
        this.channelldName = channelldName;
    }

    public void creatNitification(int grade)
    {
        NotificationManager manager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getChannelld(),getChannelldName(),grade);
            manager.createNotificationChannel(channel);
        }
    }
    public Notification getNotification()
    {
        return  notification;
    }
}
