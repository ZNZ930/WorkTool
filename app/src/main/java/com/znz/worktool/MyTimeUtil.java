package com.znz.worktool;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PropertyResourceBundle;

public class MyTimeUtil {
   /*
    计算时间差值  简单的
    思路:
    获取当前日期中的  分 秒
    判断是否为59或者是0
    如果要减去的时间大于一个小时先加一个小时在把不足一个小时的时间算出来
    然后用59或者60 去减去当前时间 得到不足一个小时的时间
    计算秒同上
    秒/3600 得到分
    */
   private void Time()
   {
       System.out.println("hello");
   }

    private  void NowTime()
    {
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("ss");
        Date date2 = new Date();
        Date date3 = new Date();
        String minute = simpleDateFormat2.format(date2);
        String second = simpleDateFormat3.format(date3);
    }
    private String DeleteZero(String time)
    {
        if (time.indexOf("1")==0)
        {
            char a = time.charAt(0);
            char b = time.charAt(1);
            String nozero = new String(new char[]{a,b});
            Log.i("TTTT",""+nozero);
            return nozero;
        }else
        {
            return time;
        }
    }

    public int ReHour(String hour)
    {
        int h = Integer.valueOf(DeleteZero(hour));
        if (h>1)
        {
            return h;
        }
        return 0;
    }

    public int getHour()
    {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH");
        Date date  = new Date();
        String hour = simpleDateFormat1.format(date);
        int h = Integer.valueOf(DeleteZero(hour));
        return h;
    }
    public int getMinute()
    {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("mm");
        Date date = new Date();
        String minute = simpleDateFormat1.format(date);
        int m = Integer.valueOf(DeleteZero(minute));
        return m;
    }

    public int getSecond()
    {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("ss");
        Date date = new Date();
        String second = simpleDateFormat1.format(date);
        int s = Integer.valueOf(DeleteZero(second));
        return s;
    }

    public String getSHour()
    {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH");
        Date date  = new Date();
        return DeleteZero(simpleDateFormat1.format(date));
    }

    public String getSMinute()
    {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("mm");
        Date date  = new Date();
        return DeleteZero(simpleDateFormat1.format(date));
    }

    public String getSSecond()
    {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("ss");
        Date date  = new Date();
        return DeleteZero(simpleDateFormat1.format(date));
    }

    //计算小时差
    //10点有事情要做  现在是8点
    public int  MarginTime(int mytime)
    {
        int nowtime = getHour();
        int newtime = nowtime - mytime;
        Log.i("TTTT",""+mytime+""+nowtime);
        if (mytime > nowtime)
        {
            Log.i("TTTT",""+(mytime-newtime));
            return mytime - nowtime;
        }
        else
        {
            return newtime;
        }
    }

    //当前时间
    public String GetNowTime()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        String second = simpleDateFormat.format(date);
        return second;
    }
}
