package com.example.cynthia.bihu.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cynthia.bihu.Tools.ActivityCollector;

/**
 * Created by Cynthia on 2018/2/25.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
