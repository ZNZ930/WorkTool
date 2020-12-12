package com.znz.worktool.WKListView;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.znz.worktool.R;

import java.util.List;

public class ImgSqlAdapter extends BaseAdapter {

    private List<ImgSqlInfo> imgSqlInfoList;
    private Context mContext;
    Dialog dialog ;
    public ImgSqlAdapter(List<ImgSqlInfo> imgSqlInfoList,Context mContext)
    {
        this.imgSqlInfoList = imgSqlInfoList;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return imgSqlInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return imgSqlInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        dialog = new Dialog(mContext,R.style.Theme_AppCompat_NoActionBar);
        ViewHolder viewHolder = null;
        if (view == null)
        {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.imgsql_item,viewGroup,false);
            viewHolder.photoView = (PhotoView)view.findViewById(R.id.imgsql_ph);
            viewHolder.time = (TextView)view.findViewById(R.id.imgsql_time);
            view.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder)view.getTag();
        }
        final int a = i;
        viewHolder.photoView.setImageDrawable(imgSqlInfoList.get(i).getDb());
        viewHolder.time.setText(imgSqlInfoList.get(i).getTime());
        viewHolder.photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                dialog.setContentView(getView(a));
            }
        });
        return view;
    }

    class ViewHolder{
        private PhotoView photoView;
        private TextView time;
    }
    public void clear() {
        imgSqlInfoList.clear();
        notifyDataSetChanged();
    }
    public PhotoView getView(int i)
    {
        PhotoView photoView = new PhotoView(mContext);
        photoView.setImageDrawable(imgSqlInfoList.get(i).getDb());
        photoView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return photoView;
    }
}
