package com.example.cynthia.bihu.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cynthia.bihu.R;
import com.example.cynthia.bihu.Tools.StatusBarUrl;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    TextView skip;
    private int time = 3;
    private Handler handler;
    Timer timer = new Timer();
    Boolean finish = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUrl.setTransparent(this);
        setContentView(R.layout.activity_welcome);
        initView();
        timer.schedule(task,1000,1000);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (finish){
                Intent intent = new Intent(WelcomeActivity.this,LogInActivity.class);
                startActivity(intent);
                finish();
                }
            }
        }, 3000);
    }

    private void initView(){
        skip = findViewById(R.id.loading);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish = false;
                Intent intent = new Intent(WelcomeActivity.this,LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    time--;
                    skip.setText("跳过 | " + time);
                    if (time < 0) {
                        timer.cancel();
                        skip.setVisibility(View.GONE);
                    }
                }
            });
        }
    };
}
