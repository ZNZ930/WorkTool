package com.znz.worktool.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.FloatProperty;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;


@SuppressLint("AppCompatCustomView")
public class ImgSqlView extends ImageView implements View.OnTouchListener {

    private PointF pointF = new PointF();
    private PointF newpointF = new PointF();
    private Matrix defaultm = new Matrix();
    private  int nomove = 0;
    private static final int ONEMOVE = 1;
    private static final int TWOMOVE = 2;
    private Matrix nowm = new Matrix();
    private float odis = 1f;

    public ImgSqlView(Context context) {
        super(context);
    }

    public ImgSqlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public int getSize(int defaultsize,int measurespec)
    {
        int mysize = defaultsize;
        int size = MeasureSpec.getSize(measurespec);
        int mode = MeasureSpec.getSize(measurespec);

        switch (mode)
        {
            case MeasureSpec.AT_MOST:
                mysize = size;
                break;
                case MeasureSpec.EXACTLY:
                    mysize = size;
                    break;
                    case MeasureSpec.UNSPECIFIED:
                        mysize = defaultsize;
                        break;
        }
        return  mysize;
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK)
    {
        case MotionEvent.ACTION_DOWN:
            nowm.set(getMatrix());
            defaultm.set(nowm);
            pointF.set(event.getX(),event.getY());
            Log.i("TTT","第一根手指");
                nomove = ONEMOVE;
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            Log.i("TTT","第二根手指");
            float olddis = MoveJuli(event);
            if (olddis>10f)
            {
                defaultm.set(nowm);
                newpointF = CenterWz(event);
                nomove = TWOMOVE;
            }
            break;
        case MotionEvent.ACTION_POINTER_UP:
            Log.i("TTT","离开了一根");
            break;
        case MotionEvent.ACTION_UP:
            Log.i("TTT","全部离开");
            break;
        case MotionEvent.ACTION_MOVE:
            if (nomove == ONEMOVE)
            {
                nowm.set(defaultm);
                nowm.postTranslate(event.getX()-pointF.x,event.getY()-pointF.y);
            }else if(nomove == TWOMOVE)
            {
                float dis = MoveJuli(event);
                if (dis > 1f)
                {
                    nowm.set(defaultm);
                    float scale = dis/odis;
                    nowm.postScale(scale,scale,newpointF.x,newpointF.y);
                }
            }
            break;
    }
        setImageMatrix(nowm);
        return true;
    }

    //移动的距离
    private float MoveJuli(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        float xy = (float)Math.sqrt(x*x+y*y)/100;
        Log.i("dasfasfasf",""+Math.sqrt(x*x+y*y)/100);
        return xy;
    }
    //中心位置
    private PointF CenterWz(MotionEvent event)
    {
        float x = event.getX(0)+event.getX(1);
        float y = event.getY(0)+event.getY(1);
        return new PointF(x/2,y/2);
    }
}
