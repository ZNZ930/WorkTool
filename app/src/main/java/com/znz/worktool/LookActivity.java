package com.znz.worktool;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.znz.worktool.SqLite.SqlUtil;

import java.util.ArrayList;
import java.util.List;

public class LookActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner et_search;
    private Button search;
    private TextView sr_name;
    private TextView sr_phone;
    private TextView sr_dizhi;
    private SQLiteDatabase sqLiteDatabase;
    private Context mContext = LookActivity.this;
    private SqlUtil sqlUtil;
    private String tv_name;
    private String tv_dizhi;
    private String tv_phone;
    private TextView sr_time;
    private TextView sr_fuwulx;
    private TextView sr_fuwumx;
    private String tv_time;
    private String tv_fuwulx;
    private String tv_fuwumx;
    private TextView sr_zhifufs;
    private String tv_zhiufufs;
    private EditText show_fuwu;
    private Button bt_show;
    private Button bt_copy;
    private ClipboardManager clipboardManager = null;
    private StringBuffer stringBuffer = null;
    private ClipData clipData;
    private EditText show_wuliao;
    private int [] items = new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
    private int sqlcount;
    private  List<Integer> list;
    private int spid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        initView();
    }

    private void initView() {
        sqlUtil = new SqlUtil(mContext);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        et_search = (Spinner) findViewById(R.id.et_search);
        search = (Button) findViewById(R.id.search);
        sr_name = (TextView) findViewById(R.id.sr_name);
        sr_phone = (TextView) findViewById(R.id.sr_phone);
        sr_dizhi = (TextView) findViewById(R.id.sr_dizhi);
        search.setOnClickListener(this);
        sr_time = (TextView) findViewById(R.id.sr_time);
        sr_time.setOnClickListener(this);
        sr_fuwulx = (TextView) findViewById(R.id.sr_fuwulx);
        sr_fuwulx.setOnClickListener(this);
        sr_fuwumx = (TextView) findViewById(R.id.sr_fuwumx);
        sr_fuwumx.setOnClickListener(this);
        sr_zhifufs = (TextView) findViewById(R.id.sr_zhifufs);
        sr_zhifufs.setOnClickListener(this);
        show_fuwu = (EditText) findViewById(R.id.show_fuwu);
        show_fuwu.setOnClickListener(this);
        bt_show = (Button) findViewById(R.id.bt_show);
        bt_show.setOnClickListener(this);
        bt_copy = (Button) findViewById(R.id.bt_copy);
        bt_copy.setOnClickListener(this);
        stringBuffer = new StringBuffer();
        show_wuliao = (EditText) findViewById(R.id.show_wuliao);
        show_wuliao.setOnClickListener(this);
        ItemQuery();
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(mContext,android.R.layout.simple_expandable_list_item_1,list);
        et_search.setAdapter(arrayAdapter);
        et_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spid = i+1;
                QuerySql(spid);
                stringBuffer.append(sr_dizhi.getText().toString() + sr_fuwulx.getText().toString() + sr_zhifufs.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                break;
            case R.id.bt_show:
                show_fuwu.setText(stringBuffer);
                break;
            case R.id.bt_copy:
                clipData = ClipData.newPlainText("Label", show_fuwu.getText().toString().trim() + "\n" + SplitWuliao(show_wuliao.getText().toString()));
                clipboardManager.setPrimaryClip(clipData);
                break;
        }
    }


    //查询数据
    private void QuerySql(int spid) {
        sqLiteDatabase = sqlUtil.getReadOpenhelp();
        Cursor cursor = null;
        cursor = sqLiteDatabase.rawQuery("select time,fuwulx,fuwumx,name,phone,dizhi,zhifufs from worktool where id = ? ", new String[]{
                ""+spid});
        if (cursor.moveToFirst()) {
            do {
                tv_time = cursor.getString(cursor.getColumnIndex("time"));
                tv_fuwulx = cursor.getString(cursor.getColumnIndex("fuwulx"));
                tv_fuwumx = cursor.getString(cursor.getColumnIndex("fuwumx"));
                tv_name = cursor.getString(cursor.getColumnIndex("name"));
                tv_phone = cursor.getString(cursor.getColumnIndex("phone"));
                tv_dizhi = cursor.getString(cursor.getColumnIndex("dizhi"));
                tv_zhiufufs = cursor.getString(cursor.getColumnIndex("zhifufs"));
            } while (cursor.moveToNext());
            sr_name.setText("用户姓名：" + tv_name);
            sr_dizhi.setText("用户住址：" + tv_dizhi);
            sr_phone.setText("用户联系方式：" +tv_phone );
            sr_fuwulx.setText("服务类型：" + tv_fuwulx);
            sr_fuwumx.setText("服务明细：" + tv_fuwumx);
            sr_time.setText("服务时间：" + tv_time);
            sr_zhifufs.setText("支付方式：" + tv_zhiufufs);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private int getSqlcount()
    {
        sqLiteDatabase = sqlUtil.getReadOpenhelp();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from worktool",new String[]{});
        sqlcount = cursor.getCount();
        return sqlcount;
    }
    private void ItemQuery()
    {
        list = new ArrayList<>();
        for(int i = 0;i<getSqlcount();i++)
        {
            list.add(items[i]);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }

    private String SplitWuliao(String wuliao) {
        String wl[] = wuliao.split("\\.");
        String wu = "今日物料使用情况\n" +
                "机器数量：" + wl[0] + "\n" +
                "A芯数量："+wl[1]+"\n" +
                "B芯数量："+wl[2]+"\n" +
                "C芯数量："+wl[3]+"\n" +
                "D芯数量："+wl[4]+"\n" +
                "龙头数量："+wl[5]+"\n" +
                "三通数量："+wl[6]+"\n" +
                "压力桶数量："+wl[7]+"\n" +
                "其他物料使用情况："+wl[8];
        return wu;
    }

    private void submit() {
        // validate
        String wuliao = show_wuliao.getText().toString().trim();
        if (TextUtils.isEmpty(wuliao)) {
            Toast.makeText(this, "物料登记..........", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
    }
    private void ShowToast(String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }
}
