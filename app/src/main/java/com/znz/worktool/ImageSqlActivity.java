package com.znz.worktool;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.znz.worktool.SqLite.ImgSqlOpenhelper;
import com.znz.worktool.WKListView.ImgSqlAdapter;
import com.znz.worktool.WKListView.ImgSqlInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageSqlActivity extends AppCompatActivity implements View.OnClickListener{

    private Button imgsql_xz;
    private Button imgsql_cx;
    private ListView imgsql_listview;
    private static final int PICK_PHOTO = 102;
    private Bitmap ImgBitmap;
    private String imagePath;
    private List<ImgSqlInfo> imgSqlInfos;
    private ImgSqlAdapter imgSqlAdapter;
    private Context mContext = this;
    private SQLiteDatabase imgdatabase;
    private ImgSqlOpenhelper imgSqlOpenhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_sql);
        initView();
    }

    private void initView() {
        imgsql_xz = (Button) findViewById(R.id.imgsql_xz);
        imgsql_cx = (Button) findViewById(R.id.imgsql_cx);
        imgsql_listview = (ListView) findViewById(R.id.imgsql_listview);
        imgsql_xz.setOnClickListener(this);
        imgsql_cx.setOnClickListener(this);
        imgSqlInfos = new ArrayList<>();
        imgSqlOpenhelper = new ImgSqlOpenhelper(mContext,"imgsql.db",null,1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgsql_xz:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO);
                break;
            case R.id.imgsql_cx:
                if (imgSqlAdapter != null)
                {
                    imgSqlAdapter.clear();
                    OpenImg();
                }else
                {
                    OpenImg();
                }
                break;
        }
    }
    private void getImage(Intent data) {

        imagePath = null;
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
        InsertImg();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,001,1,"重置数据");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case 001:
                DelSql();
                imgSqlAdapter.clear();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    getImage(data);
                }
                break;
            default:
                break;
        }
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

    //存入图片
    private void InsertImg()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImgBitmap = BitmapFactory.decodeFile(imagePath);
        ImgBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte [] imgb = baos.toByteArray();
        imgdatabase = imgSqlOpenhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time",new MyTimeUtil().GetNowTime());
        values.put("img",imgb);
        values.put("path",imagePath);
        imgdatabase.insert("imgsql",null,values);
    }

    //读取图片
    private void OpenImg()
    {
        Bitmap bitmap = null;
        byte [] bytes = new byte[]{};
        imgdatabase = imgSqlOpenhelper.getReadableDatabase();
        Cursor cursor = imgdatabase.rawQuery("select img,time from imgsql",new String[]{});
        if (cursor.moveToFirst())
        {
            do {
                bytes = cursor.getBlob(cursor.getColumnIndex("img"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imgSqlInfos.add(new ImgSqlInfo(getDrawable(bitmap),time));
            }while (cursor.moveToNext());
            imgSqlAdapter = new ImgSqlAdapter(imgSqlInfos,mContext);
            imgsql_listview.setAdapter(imgSqlAdapter);
        }
        if (cursor!=null)
        {
            cursor.close();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imgdatabase != null)
        {
            imgdatabase.close();
        }
    }
    private void ShowTomast(String msg)
    {
        Toast toast = Toast.makeText(mContext,msg,Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    //删除数据
    private void DelSql()
    {
        imgdatabase = imgSqlOpenhelper.getWritableDatabase();
        imgdatabase.execSQL("delete  from imgsql");
    }

    //获取数据库中的Blob
//    private Bitmap getBitmap()
//    {
//        imgdatabase = imgSqlOpenhelper.getReadableDatabase();
//        Cursor cursor = imgdatabase.rawQuery("select img from imgsql",new String[]{});
//        if (cursor.moveToFirst())
//        {
//            do {
//            }while (cursor.moveToNext());
//    }
//        return bitmap;
//    }

    //转换图片为Drawable
    private Drawable getDrawable(Bitmap bp)
    {
        Bitmap bm = bp;
        Drawable db = new BitmapDrawable(getResources(),bm);
        return db;
    }
}
