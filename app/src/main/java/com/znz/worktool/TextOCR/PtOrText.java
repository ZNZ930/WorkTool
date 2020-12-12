package com.znz.worktool.TextOCR;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PtOrText {

    private TessBaseAPI tessBaseAPI;
    private String Language;
    private String lugPath;

    public PtOrText(TessBaseAPI tessBaseAPI,String Language,String lugPath)
    {
        this.tessBaseAPI = tessBaseAPI;
        this.lugPath = lugPath;
        this.Language = lugPath;
    }

    public void OcrText()
    {
        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(getLugPath(),getLanguage());
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);

    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getLugPath() {
        return lugPath;
    }

    public void setLugPath(String lugPath) {
        this.lugPath = lugPath;
    }

    public TessBaseAPI getTessBaseAPI() {
        return tessBaseAPI;
    }

    public void setTessBaseAPI(TessBaseAPI tessBaseAPI) {
        this.tessBaseAPI = tessBaseAPI;
    }
}
