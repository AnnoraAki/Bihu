package com.example.cynthia.bihu.Tools;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import javax.security.auth.callback.Callback;

/**
 * Created by Cynthia on 2018/2/21.
 */

public class HttpUrl {
    public static void sendHttpRequest(final String api, final String param, final Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(api);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setReadTimeout(5 * 1000);
                    connection.setConnectTimeout(10 * 1000);
                    if (param == null)
                        connection.setRequestMethod("GET");
                    else{
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    OutputStream os = connection.getOutputStream();
                    os.write(param.getBytes("UTF-8"));
                    os.flush();
                    os.close();
                    }
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                    try {
                        JSONObject object = new JSONObject(response.toString());
                        int status = object.getInt("status");
                        String info = object.getString("info");
                        Log.d("response",""+response);
                        if (status == 200 && callback != null){
                            callback.onFinish(response.toString());
                        }else {
                            callback.onError(info);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (MalformedURLException e) {
                    if (callback != null)
                        callback.onError(e.toString());
                } catch (IOException e) {
                    if (callback != null)
                        callback.onError(e.toString());
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public interface Callback{

        void onFinish(String response);

        void onError(String error);
    }

}
