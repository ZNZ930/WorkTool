package com.znz.worktool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.TimeUtils;

import com.znz.worktool.SqLite.SqlUtil;
import com.znz.worktool.WKListView.WorkAdapter;
import com.znz.worktool.WKListView.WorkInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class LookMoreActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView wk_list;
    private List<WorkInfo> workInfos;
    private Context mContext;
    private WorkAdapter workAdapter;
    private SqlUtil sqlUtil;
    private SQLiteDatabase sqLiteDatabase;
    private int lkm_id;
    private String lkm_dizhi;
    private String lkm_time;
    private String lkm_fuwumx;
    private Button lkm_bt;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    /*
    添加 ListView单击功能
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_more);
        initView();
        DianJi();
    }

    private void initView() {
        mContext = LookMoreActivity.this;
        sqlUtil = new SqlUtil(mContext);
        wk_list = (ListView) findViewById(R.id.wk_list);
        lkm_bt = (Button) findViewById(R.id.lkm_bt);
        lkm_bt.setOnClickListener(this);
        workInfos = new ArrayList<WorkInfo>();
    }

    //查询数据 -ID-时间-地址-服务明细
    private void QuerySql() {
        sqLiteDatabase = sqlUtil.getReadOpenhelp();
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery("select time,id,dizhi,fuwumx from worktool", new String[]{});
            if (cursor.moveToFirst()) {
                do {
                    lkm_time = cursor.getString(cursor.getColumnIndex("time"));
                    lkm_id = cursor.getInt(cursor.getColumnIndex("id"));
                    lkm_dizhi = cursor.getString(cursor.getColumnIndex("dizhi"));
                    lkm_fuwumx = cursor.getString(cursor.getColumnIndex("fuwumx"));
                    workInfos.add(new WorkInfo(lkm_id, lkm_time, lkm_dizhi,lkm_fuwumx));
                } while (cursor.moveToNext());
            }
            workAdapter = new WorkAdapter(workInfos, mContext);
            wk_list.setAdapter(workAdapter);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lkm_bt:
                if (workAdapter != null)
                {
                    workAdapter.clear();
                }
                QuerySql();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        workAdapter = null;
        if (sqLiteDatabase != null)
        {
            sqLiteDatabase.close();
        }
    }
    private void ShwoTomast(String msg)
    {
        Toast toast = Toast.makeText(mContext,msg,Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    //点击事件
    private void DianJi()
    {
        wk_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int a, long l) {
                alertDialog = new AlertDialog.Builder(mContext).
                        setTitle("确定要删除吗？")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DelSql(String.valueOf(workInfos.get(a).getId()));
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                            }
                        }).show();
            }
        });
    }
    private void DelSql(String sql) {
        sqLiteDatabase = sqlUtil.getSqOpenHelp().getWritableDatabase();
        sqLiteDatabase.execSQL("delete from worktool where id=?",new String[]{sql});

    }

}
