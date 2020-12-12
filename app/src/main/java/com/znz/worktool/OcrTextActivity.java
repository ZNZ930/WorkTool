package com.znz.worktool;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.znz.worktool.TextOCR.PtOrText;

public class OcrTextActivity extends AppCompatActivity implements View.OnClickListener {

    private Button choice_ph;
    private static final int PICK_PHOTO = 102;
    private TextView ocr_text;
    private Button start_ocr;
    private TessBaseAPI tessBaseAPI;
    private Bitmap ImgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_text);
        initView();
    }

    private void initView() {
        tessBaseAPI = new TessBaseAPI();
        choice_ph = (Button) findViewById(R.id.choice_ph);
        choice_ph.setOnClickListener(this);
        ocr_text = (TextView) findViewById(R.id.ocr_text);
        ocr_text.setOnClickListener(this);
        start_ocr = (Button) findViewById(R.id.start_ocr);
        start_ocr.setOnClickListener(this);
    }

    @RequiresApi(api = 30)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choice_ph:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //Intent.ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO);
                break;
            case R.id.start_ocr:
                SetOcrText();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @RequiresApi(api = 30)
    private void SetOcrText()
    {
        String Path = Environment.getExternalStorageDirectory().getPath();
        Log.i("",Path);
        tessBaseAPI.init(Path+"/tesseract/","chi_sim");
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
        tessBaseAPI.setImage(ImgBitmap);
        String OcrText = tessBaseAPI.getUTF8Text();
        ocr_text.setText(OcrText);
    }

}
