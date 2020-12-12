package com.znz.worktool;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.znz.worktool.MyService.TongZhiService;
import com.znz.worktool.SqLite.SqlUtil;
import com.znz.worktool.SqLite.WeatherBean;

import org.apache.http.HttpEntity;
import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText time;
    private EditText dizhi;
    private EditText fuwulx;
    private EditText name;
    private EditText phone;
    private EditText fuwumx;
    private EditText zhifufs;
    private SQLiteDatabase sqLiteDatabase;
    private Context mContext = MainActivity.this;
    private Button insert;
    private int j = 1;
    private Button activity_search;
    private SqlUtil sqlUtil;
    private Button activity_del;
    private Button activity_update;
    //启动Activity
    private Intent queryIntent;
    private Intent updateIntent;
    private TextView readme;
    private AlertDialog alertDialog = null;
    private AlertDialog.Builder builder = null;
    private Button look_more;
    private Intent LookMIntent;
    private Button look_ocr;
    private Intent OcrIntent;
    private Intent recordIntent;
    private Button tz_stop;
    private Intent tzintent;
    private TongZhiService.MyBinder myBinder;
    private ServiceConnection conn;
    private AlarmManager alarmManager;
    private TabHost tabHost;
    private Intent dataIntent;
    private Spinner id_spinner;
    private ImageView weather_img;
    private Bitmap bitmap;
    private InputStream is;
    private String weatherimg;
    private String wind;
    private String temp;
    private String aqi;
    private String pm25;
    private String imgurl;
    private Bitmap ImgBitmap;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Bundle bundle = (Bundle)msg.obj;
                    weatherimg = bundle.getString("weatherimg");
                    wind = bundle.getString("wind");
                    temp = bundle.getString("temp");
                    pm25 = bundle.getString("pm25");
                    aqi = bundle.getString("aqi");
                    try {
                        weather_img.setImageBitmap(getImg(weatherimg));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ShowToast(weatherimg);
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getWea();
            }
        }, 1000);
    }

    private void initView() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Perapp();
        sqlUtil = new SqlUtil(mContext);
        time = (EditText) findViewById(R.id.time);
        dizhi = (EditText) findViewById(R.id.dizhi);
        fuwulx = (EditText) findViewById(R.id.fuwulx);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        fuwumx = (EditText) findViewById(R.id.fuwumx);
        zhifufs = (EditText) findViewById(R.id.zhifufs);
        insert = (Button) findViewById(R.id.insert);
        insert.setOnClickListener(this);
        activity_search = (Button) findViewById(R.id.activity_search);
        activity_search.setOnClickListener(this);
        activity_del = (Button) findViewById(R.id.activity_del);
        activity_del.setOnClickListener(this);
        activity_update = (Button) findViewById(R.id.activity_update);
        activity_update.setOnClickListener(this);
        //查询
        queryIntent = new Intent();
        queryIntent.setClass(mContext, LookActivity.class);
        //修改
        updateIntent = new Intent();
        updateIntent.setClass(mContext, UpadteActivity.class);
        //更多数据
        LookMIntent = new Intent();
        LookMIntent.setClass(mContext, LookMoreActivity.class);
        recordIntent = new Intent();
        recordIntent.setClass(mContext, DataRecord.class);
        readme = (TextView) findViewById(R.id.readme);
        Readme();
        builder = new AlertDialog.Builder(mContext);
        look_more = (Button) findViewById(R.id.look_more);
        look_more.setOnClickListener(this);
        look_ocr = (Button) findViewById(R.id.look_ocr);
        look_ocr.setOnClickListener(this);
        OcrIntent = new Intent(mContext, OcrTextActivity.class);
        tz_stop = (Button) findViewById(R.id.tz_stop);
        tz_stop.setOnClickListener(this);
        tzintent = new Intent().setClass(mContext, TongZhiService.class);
        tzintent.setAction("com.znz.worktool.TONGZHI_SERVICE");
        // tzintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.i("ZNZ", "......onServiceConnected....");
                iBinder = (TongZhiService.MyBinder) iBinder;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.i("ZNZ", "......onServiceDisconnected....");
            }
        };
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        id_spinner = (Spinner) findViewById(R.id.id_spinner);
        id_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                j = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        weather_img = (ImageView) findViewById(R.id.weather_img);

    }

    private void submit() {
        // validate
        String timeString = time.getText().toString().trim();
        if (TextUtils.isEmpty(timeString)) {
            Toast.makeText(this, "请输入日期", Toast.LENGTH_SHORT).show();
            return;
        }

        String dizhiString = dizhi.getText().toString().trim();
        if (TextUtils.isEmpty(dizhiString)) {
            Toast.makeText(this, "请输入地址", Toast.LENGTH_SHORT).show();
            return;
        }

        String fuwulxString = fuwulx.getText().toString().trim();
        if (TextUtils.isEmpty(fuwulxString)) {
            Toast.makeText(this, "请输入服务类型", Toast.LENGTH_SHORT).show();
            return;
        }

        String nameString = name.getText().toString().trim();
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        String phoneString = phone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneString)) {
            Toast.makeText(this, "请输入联系方式（手机号）", Toast.LENGTH_SHORT).show();
            return;
        }

        String fuwumxString = fuwumx.getText().toString().trim();
        if (TextUtils.isEmpty(fuwumxString)) {
            Toast.makeText(this, "请输入服务明细", Toast.LENGTH_SHORT).show();
            return;
        }

        String zhifufsString = zhifufs.getText().toString().trim();
        if (TextUtils.isEmpty(zhifufsString)) {
            Toast.makeText(this, "请输入支付方式", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }

    //添加数据
    private void InsertSql(String sql) {
        sqLiteDatabase = sqlUtil.getSqOpenHelp().getWritableDatabase();
//        sqLiteDatabase.execSQL("insert into worktool(id,time,dizhi,fuwulx,fuwumx,zhifufs,name,phone) values(?,?,?,?,?,?,?,?)",
//                new String[]{
//                        "" + i,
//                        time.getText().toString().trim(),
//                        dizhi.getText().toString().trim(),
//                        fuwulx.getText().toString().trim(),
//                        fuwumx.getText().toString().trim(),
//                        zhifufs.getText().toString().trim(),
//                        name.getText().toString().trim(),
//                        phone.getText().toString().trim()});
        String xinxi[] = sql.split("\\|");
        ContentValues values = new ContentValues();
        values.put("id", "" + getEndId());
        values.put("time", xinxi[0]);
        values.put("dizhi", xinxi[1]);
        values.put("fuwulx", xinxi[2]);
        values.put("name", xinxi[3]);
        values.put("phone", xinxi[4]);
        values.put("fuwumx", xinxi[5]);
        values.put("zhifufs", xinxi[6]);
        sqLiteDatabase.insert("worktool", null, values);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert:
                if (TextUtils.isEmpty(time.getText())) {
                    Toast.makeText(mContext, "数据不能为空", Toast.LENGTH_LONG).show();
                } else {
                    Wuliao();
                }
                break;
            case R.id.activity_search:
                startActivity(queryIntent);
                break;
            case R.id.activity_del:
                alertDialog = builder.
                        setTitle("您确定要删除表数据吗？")
                        .setNegativeButton("不小心点到了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        })
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DelSql();
                            }
                        })
                        .create();
                alertDialog.show();
                break;
            case R.id.activity_update:
                startActivity(updateIntent);
                break;
            case R.id.look_more:
                startActivity(LookMIntent);
                break;
            case R.id.look_ocr:
                // bindService(tzintent,conn,Service.BIND_AUTO_CREATE);
                //startService(tzintent);
                Calendar calendar = Calendar.getInstance();
                PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, tzintent, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 20);
                calendar.set(Calendar.MINUTE, 59);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 3600, pendingIntent);
                Log.i("ZNZ", "" + calendar.getTimeInMillis());
                break;
            case R.id.tz_stop:
                // stopService(tzintent);
                break;
        }
    }

    //删除数据
    private void DelSql() {
        sqLiteDatabase = sqlUtil.getSqOpenHelp().getWritableDatabase();
        sqLiteDatabase.execSQL("delete from worktool");
    }

    //使用说明
    private void Readme() {
        readme.setTextSize(20);
        readme.setText("*在文本框中输入数据后，点击添加数据按钮\n" +
                "*支持删除所有数据\n" +
                "*支持查询数据\n" +
                "*支持修改数据\n");
    }


    //查询数据
    private void ChaShujuSql() {
        sqLiteDatabase = sqlUtil.getReadOpenhelp();
        Cursor cursor = sqLiteDatabase.rawQuery("select time from worktool", new String[]{});
        if (cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext());
        }
    }


    //权限申请
    private void Perapp() {
        int hasWriteES = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadES = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasReadES != PackageManager.PERMISSION_GRANTED || hasWriteES != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }

    private void ShowToast(String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    //获取最后一个数据的ID
    private int getEndId() {
        sqLiteDatabase = sqlUtil.getReadOpenhelp();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from worktool", new String[]{});
        int count = cursor.getCount();
        if (count == 0) {
            return 1;
        } else {
            return count + 1;
        }
    }

    //请求数据
    private void getWea() {
        final Gson gson = new Gson();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://api.help.bj.cn/apis/weather/?id=101060101", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    WeatherBean weatherBean = gson.fromJson(jsonObject.toString(), WeatherBean.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("weatherimg","https:"+weatherBean.getWeatherimg());
                    bundle.putString("temp",weatherBean.getTemp());
                    bundle.putString("wind",weatherBean.getWind());
                    bundle.putString("pm25",weatherBean.getPm25());
                    bundle.putString("aqi",weatherBean.getAqi());
                    Message message = handler.obtainMessage();
                    message.what  = 1;
                    message.obj = bundle;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);
    }

    public Bitmap getImg (String url) throws Exception {
        URL url1 = new URL(url);
        ShowToast("getImg" +url);
        is = url1.openStream();
        bitmap = BitmapFactory.decodeStream(is);
        is.close();
        return bitmap;
    }

    private void Wuliao()
    {
        String wl[] = time.getText().toString().trim().split("\\|");
        int wllen = wl.length;
        if (wllen == 7)
        {
            InsertSql(time.getText().toString().trim());
            ShowToast("添加成功！");
        }
        else if (wllen <7)
        {
            ShowToast("缺少数据！");
        }
        else
        {
            ShowToast("数据多出！");
        }
    }
}
