package com.example.cynthia.bihu.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Cynthia on 2018/2/26.
 */

public class BitmapUrl {
    public static Bitmap getBitmap(final String url) {
        URL mUrl = null;
        Bitmap bitmap = null;
        try {
            mUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            if (conn.getResponseCode() == 200){
                bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

