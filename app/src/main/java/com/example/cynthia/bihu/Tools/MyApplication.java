package com.example.cynthia.bihu.Tools;

import android.app.Application;
import android.content.Context;

import com.example.cynthia.bihu.Data.User;

/**
 * Created by Cynthia on 2018/2/24.
 */

public class MyApplication extends Application {
    private static Context mContext;
    private static User mUser = new User();
    private static String password;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

    }

    public static Context getContext() {
        return mContext;
    }

    public static void setUserId(int id){
        mUser.setId(id);
    }

    public static void setUserName(String name){
        mUser.setUsername(name);
    }

    public static void setUserAvatar(String avatarUrl){
        mUser.setAvatar(avatarUrl);
    }

    public static void setUserToken(String token){
        mUser.setToken(token);
    }

    public static User getMUser(){return mUser;}

    public static void setPassword(String pw){
        password = pw;
    }

    public static String getPassword(){
        return password;
    }
}

