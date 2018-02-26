package com.example.cynthia.bihu.Tools;

import android.widget.Toast;

/**
 * Created by Cynthia on 2018/2/24.
 */

public class ToastUrl {
    public static void showError(String error){
        Toast.makeText(MyApplication.getContext(),error,
                Toast.LENGTH_LONG).show();
    }

    public static void showResponse(String info){
        Toast.makeText(MyApplication.getContext(),info,
                Toast.LENGTH_SHORT).show();
    }
}
