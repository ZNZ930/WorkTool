package com.znz.worktool.WKListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.znz.worktool.R;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends BaseAdapter implements Filterable {

    private List<RecordInfo> recordInfoList;
    private Context mContext;
    private List<RecordInfo> newlist;
    private Object mlock = new Object();
    private ArrayList<RecordInfo> arrayList;
    MyFilter myFilter;
    public RecordAdapter(Context mContext,List<RecordInfo> recordInfoList)
    {
        this.mContext = mContext;
        this.recordInfoList = recordInfoList;
    }
    @Override
    public int getCount() {
        return recordInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.record_listview,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.record_dizhi = (TextView)view.findViewById(R.id.record_dizhi);
            viewHolder.record_fuwulx =(TextView)view.findViewById(R.id.record_fuwulx);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.record_dizhi.setText(recordInfoList.get(i).getRdizhi());
        viewHolder.record_fuwulx.setText(recordInfoList.get(i).getRfuwulx());
        return view;
    }

    @Override
    public Filter getFilter() {
        if (myFilter == null)
        {
            myFilter = new MyFilter();
        }
        return myFilter;
    }

    class ViewHolder{
        private TextView record_dizhi;
        private TextView record_fuwulx;
    }
    public void clear() {
        recordInfoList.clear();
        notifyDataSetChanged();
    }

    class MyFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if (arrayList == null)
            {
               synchronized (mlock)
                {
                    arrayList = new ArrayList<RecordInfo>(recordInfoList);
                }
            }
            if (charSequence == null || charSequence.length() == 0)
            {
                results.count = arrayList.size();
                results.values = arrayList;
            }
            else
            {
                List<RecordInfo> recordInfos1 = new ArrayList<RecordInfo>();
                for (RecordInfo r : arrayList)
                {
                    if (r.getRdizhi().toUpperCase().startsWith(charSequence.toString().toUpperCase())||
                            r.getRfuwulx().toUpperCase().startsWith(charSequence.toString().toUpperCase())||
                            r.getRfuwulx().contains(charSequence.toString())||
                    r.getRdizhi().contains(charSequence.toString()))
                    {
                        recordInfos1.add(r);
                    }
                }
                results.values = recordInfos1;
                //newlist = recordInfos1;
                results.count = recordInfos1.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                recordInfoList = (List<RecordInfo>)filterResults.values;
                notifyDataSetChanged();
        }

    }

}
