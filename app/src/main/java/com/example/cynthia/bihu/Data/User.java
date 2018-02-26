package com.example.cynthia.bihu.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cynthia on 2018/2/11.
 */

public class User {
    private int id;
    private String username;
    private String avatar;
    private String token;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}