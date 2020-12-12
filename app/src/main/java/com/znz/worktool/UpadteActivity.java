package com.znz.worktool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.znz.worktool.SqLite.SqlUtil;

public class UpadteActivity extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase sqLiteDatabase;
    private SqlUtil sqlUtil;
    private EditText et_update;
    private Button update_time;
    private Button update_dizhi;
    private Button update_zhifufs;
    private Button update_fuwulx;
    private Button update_fuwumx;
    private Button update_name;
    private Button update_phone;
    private Context mContext;
    private EditText et_update_id;
    private Button update_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upadte);
        initView();
    }

    private void initView() {
        mContext = UpadteActivity.this;
        sqlUtil = new SqlUtil(mContext);
        et_update = (EditText) findViewById(R.id.et_update);
        update_time = (Button) findViewById(R.id.update_time);
        update_dizhi = (Button) findViewById(R.id.update_dizhi);
        update_zhifufs = (Button) findViewById(R.id.update_zhifufs);
        update_fuwulx = (Button) findViewById(R.id.update_fuwulx);
        update_fuwumx = (Button) findViewById(R.id.update_fuwumx);
        update_name = (Button) findViewById(R.id.update_name);
        update_phone = (Button) findViewById(R.id.update_phone);
        sqLiteDatabase = sqlUtil.getWriteOpenhelp();
        update_time.setOnClickListener(this);
        update_dizhi.setOnClickListener(this);
        update_zhifufs.setOnClickListener(this);
        update_fuwulx.setOnClickListener(this);
        update_fuwumx.setOnClickListener(this);
        update_name.setOnClickListener(this);
        update_phone.setOnClickListener(this);
        et_update_id = (EditText) findViewById(R.id.et_update_id);
        et_update_id.setOnClickListener(this);
        update_id = (Button) findViewById(R.id.update_id);
        update_id.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_time:
                sqLiteDatabase.execSQL("update worktool set time=? where id=?",
                        new String[]{et_update.getText().toString().trim(),
                                et_update_id.getText().toString().trim()});
                UpdateSuccess();
                break;
            case R.id.update_dizhi:
                sqLiteDatabase.execSQL("update worktool set dizhi=? where id=?",
                        new String[]{et_update.getText().toString().trim(),
                                et_update_id.getText().toString().trim()});
                UpdateSuccess();
                break;
            case R.id.update_zhifufs:
                sqLiteDatabase.execSQL("update worktool set zhifufs=? where id=?",
                        new String[]{et_update.getText().toString().trim(),
                                et_update_id.getText().toString().trim()});
                UpdateSuccess();
                break;
            case R.id.update_fuwulx:
                sqLiteDatabase.execSQL("update worktool set fuwulx=? where id=?",
                        new String[]{et_update.getText().toString().trim(),
                                et_update_id.getText().toString().trim()});
                UpdateSuccess();
                break;
            case R.id.update_fuwumx:
                sqLiteDatabase.execSQL("update worktool set fuwumx=? where id=?",
                        new String[]{et_update.getText().toString().trim(),
                                et_update_id.getText().toString().trim()});
                UpdateSuccess();
                break;
            case R.id.update_name:
                sqLiteDatabase.execSQL("update worktool set name=? where id=?",
                        new String[]{et_update.getText().toString().trim(),
                                et_update_id.getText().toString().trim()});
                UpdateSuccess();
                break;
            case R.id.update_phone:
                sqLiteDatabase.execSQL("update worktool set phone=? where id=?",
                        new String[]{et_update.getText().toString().trim(),
                                et_update_id.getText().toString().trim()});
                UpdateSuccess();
                break;
            case R.id.update_id:
                sqLiteDatabase.execSQL("update worktool set id=? where id=?",
                        new String[]{et_update.getText().toString().trim(),
                                et_update_id.getText().toString().trim()});
                UpdateSuccess();
                break;
        }
    }

    private void UpdateSuccess() {
        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }
}
