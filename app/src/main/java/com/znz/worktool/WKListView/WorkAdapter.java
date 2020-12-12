package com.znz.worktool.WKListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.znz.worktool.R;

import java.util.List;

public class WorkAdapter extends BaseAdapter {
    private List<WorkInfo> workInfoList;
    private Context mContext;

    public WorkAdapter(List<WorkInfo> workInfoList,Context mContext)
    {
        this.workInfoList = workInfoList;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return workInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return workInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       ViewHolder viewHolder = null;
       if(view == null)
       {
            view = LayoutInflater.from(mContext).inflate(R.layout.work_item,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_dizhi = (TextView)view.findViewById(R.id.look_dizhi);
            viewHolder.tv_id = (TextView)view.findViewById(R.id.look_id);
            viewHolder.tv_time = (TextView)view.findViewById(R.id.look_time);
            viewHolder.tv_fuwumx = (TextView)view.findViewById(R.id.look_fuwumx);
            view.setTag(viewHolder);
       }else
       {
           viewHolder = (ViewHolder)view.getTag();
       }
        viewHolder.tv_time.setText(workInfoList.get(i).getTime());
       viewHolder.tv_id.setText(""+workInfoList.get(i).getId());
       viewHolder.tv_dizhi.setText(workInfoList.get(i).getDizhi());
       viewHolder.tv_fuwumx.setText(workInfoList.get(i).getFuwumx());
        return view;
    }
  static class ViewHolder
    {
        private TextView tv_id;
        private TextView tv_dizhi;
        private TextView tv_time;
        private TextView tv_fuwumx;
    }
    public void clear() {
        workInfoList.clear();
        notifyDataSetChanged();
    }
}
