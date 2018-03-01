package com.example.cynthia.bihu.Tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.cynthia.bihu.Config;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Cynthia on 2018/2/26.
 */

public class BitmapUrl {

    protected static UploadManager uploadManager;
    private static String filename = "p4ugmd4b8.bkt.clouddn.com";
    private static String accessKey = "Wc1wt1WPEosGdBeX-HU-M2YQeseQBHwASr8ZMRbE";
    private static String secretKey = "y0QwVBv_3avW1Xl5s32h51iZaKgq7gbUUtubind1";
    private static String bucket = "cchanges";
    private static String updateToken;
    private static String upKey;
    private static Boolean succeed = false;


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

    private static void getTokenAndKey(){
        if (!succeed){
            upKey = "image"+System.currentTimeMillis();
            String param = "accessKey="+accessKey+"&secretKey="+secretKey+"&bucket="+bucket;
            HttpUrl.sendHttpRequest(Config.GET_TOKEN, param, new HttpUrl.Callback() {
                @Override
                public void onFinish(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        updateToken = jsonObject.getString("token");
                        succeed = true;
                    } catch (JSONException e) {
                        e.printStackTrace();}
                }
                @Override
                public void onError(String error) {
                    ToastUrl.showError("不明原因导致图片上传云服务器失败...");
                }
            });
        } else {
            ToastUrl.showError("已经获得数据..与服务器对接中..请耐心等待..");
        }
    }

    public static void upToQiniu(final String imagePath, final Activity activity){
        getTokenAndKey();
        Configuration configuration = new Configuration.Builder()
                .zone(FixedZone.zone2).build();
        uploadManager = new UploadManager(configuration);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * 1000);
//                  延迟运行这边的使得另一个方法能够顺利执行完。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (succeed) {
                    if (imagePath != null) {
                        uploadManager.put(imagePath, upKey, updateToken, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (info.isOK()) {
                                    changeAvatar(activity);
                                } else {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUrl.showError("上传失败，请重试  :)");
                                        }
                                    });
                                }
                            }
                        }, null);
                    } else {
                        ToastUrl.showError("选择头像才能上传的 :)");
                    }
                }else{
                ToastUrl.showError("与服务器对接获取信息中...");
            }
        }
    }).start();
}


    private static void changeAvatar(final Activity activity){
        final String update = "http://"+filename+"/"+upKey+"?imageView2/2/w/200/h/200/q/75|imageslim";
        String param = "token="+MyApplication.getMUser().getToken()+"&avatar="+update;
        HttpUrl.sendHttpRequest(Config.MODIFY_AVATAR, param, new HttpUrl.Callback() {
            @Override
            public void onFinish(String response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUrl.showResponse("头像上传成功！");
                        succeed = false;
                        MyApplication.setUserAvatar(update);
                    }
                });
            }

            @Override
            public void onError(final String error) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUrl.showError(error);
                    }
                });
            }
        });
    }
}

