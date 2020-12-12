package com.znz.worktool;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.znz.worktool.SqLite.DtRecordHelper;
import com.znz.worktool.SqLite.SqlUtil;
import com.znz.worktool.WKListView.RecordAdapter;
import com.znz.worktool.WKListView.RecordInfo;

import java.util.ArrayList;
import java.util.List;

public class DataRecord extends AppCompatActivity implements View.OnClickListener {
    private Button record_cx;
    private Button record_tj;
    private ListView record_listview;
    private SQLiteDatabase database;
    private DtRecordHelper dataRecord;
    private Context mContext = this;
    private RecordAdapter recordAdapter;
    private String rdizhi;
    private String rfuwulx;
    private List<RecordInfo> recordInfoList;
    private SqlUtil sqlUtil;
    private SQLiteDatabase databasesq;
    private String dizhi_wk;
    private String fuwulx_wk;
    private Button record_hq;
    private TextView record_sj;
    private static final int PICK_PHOTO = 102;
    private Intent intentPhoto;
    private Bitmap ImgBitmap;
    private Intent PhIntent;
    private EditText et_filter;
     /*
    获取worktool里的地址和服务类型
    将dizhi和fuwulx里的存到新的数据库里
    增加查询和计数功能
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_record);
        initView();
    }

    private void initView() {
        record_cx = (Button) findViewById(R.id.record_cx);
        record_tj = (Button) findViewById(R.id.record_tj);
        record_listview = (ListView) findViewById(R.id.record_listview);
        record_cx.setOnClickListener(this);
        record_tj.setOnClickListener(this);
        dataRecord = new DtRecordHelper(mContext, "record.db", null, 1);
        recordInfoList = new ArrayList<>();
        sqlUtil = new SqlUtil(mContext);
        record_hq = (Button) findViewById(R.id.record_hq);
        record_hq.setOnClickListener(this);
        record_sj = (TextView) findViewById(R.id.record_sj);
        record_sj.setOnClickListener(this);
        PhIntent = new Intent();
        PhIntent.setClass(mContext, ImageSqlActivity.class);
        et_filter = (EditText) findViewById(R.id.et_filter);
        et_filter.setOnClickListener(this);
        et_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recordAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_cx:
                if (recordAdapter != null) {
                    recordAdapter.clear();
                }
                ChaSql();
                break;
            case R.id.record_tj:
                record_sj.setText("总服务数：" + SqlCount("fuwulx"));
                break;
            case R.id.record_hq:
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                alertDialog = builder.setTitle("是否添加数据")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Insert();
                                ShwoToast("添加成功");
                            }
                        })
                        .show();
                break;
        }
    }

    //添加数据
    private void Insert() {
        databasesq = sqlUtil.getReadOpenhelp();
        Cursor cursor = databasesq.rawQuery("select dizhi,fuwulx from worktool ", new String[]{});
        if (cursor.moveToFirst()) {
            do {
                dizhi_wk = cursor.getString(cursor.getColumnIndex("dizhi"));
                fuwulx_wk = cursor.getString(cursor.getColumnIndex("fuwulx"));
                InsertSql(dizhi_wk, fuwulx_wk);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void InsertSql(String rdizhi, String rfuwulx) {
        database = dataRecord.getWritableDatabase();
        database.execSQL("insert into record(dizhi,fuwulx) values(?,?)", new String[]{rdizhi, rfuwulx});
    }

    //总数据数
    private int SqlCount(String index) {
        database = dataRecord.getReadableDatabase();
        Cursor cursor = database.rawQuery("select count(?) from record", new String[]{index});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }
    //服务类型数据

    //删除数据
    private void DelSql() {
        database = dataRecord.getWritableDatabase();
        database.execSQL("delete from  record");
    }

    //查询数据
    private void ChaSql() {
        database = dataRecord.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from record", new String[]{});
        if (cursor.moveToFirst()) {
            do {
                rdizhi = cursor.getString(cursor.getColumnIndex("dizhi"));
                rfuwulx = cursor.getString(cursor.getColumnIndex("fuwulx"));
                recordInfoList.add(new RecordInfo(rdizhi, rfuwulx));
            } while (cursor.moveToNext());
            recordAdapter = new RecordAdapter(mContext, recordInfoList);
            record_listview.setAdapter(recordAdapter);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void ShwoToast(String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 001, 1, "重置数据");
        menu.add(1, 002, 2, "图片管理");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 001:
                DelSql();
                ShwoToast("重置成功");
                break;
            case 002:
                startActivity(PhIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getImage(Intent data) {

        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        ImgBitmap = BitmapFactory.decodeFile(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recordAdapter = null;
        if (database != null) {
            database.close();
        }
    }

    private void submit() {
        // validate
        String filter = et_filter.getText().toString().trim();
        if (TextUtils.isEmpty(filter)) {
            Toast.makeText(this, "filter不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }

    public void SetContext(Context context)
    {
        this.mContext = context;
    }
}
