package com.example.cynthia.bihu.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cynthia.bihu.Config;
import com.example.cynthia.bihu.R;
import com.example.cynthia.bihu.Tools.HttpUrl;
import com.example.cynthia.bihu.Tools.MyApplication;
import com.example.cynthia.bihu.Tools.ToastUrl;


import org.json.JSONException;
import org.json.JSONObject;



public class LogInActivity extends BaseActivity {

    private PopupWindow mPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_log_in);
        Button logIn = findViewById(R.id.logIn);
        final EditText name = findViewById(R.id.name);
        final EditText psw = findViewById(R.id.pw);
        TextView register = findViewById(R.id.register_new);



        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                final String password = psw.getText().toString();
                String param = "username="+username+"&password="+password;
                Log.d("param",param);
//          添加动画显示登陆中
            HttpUrl.sendHttpRequest(Config.LOGIN, param, new HttpUrl.Callback() {
                @Override
                public void onFinish(String response) {
                    jsonOfLogIn(response);
                    MyApplication.setPassword(password);
                }

                @Override
                public void onError(final String error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUrl.showError(error);
                        }
                    });
                }
            });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

    }

    private void showPopupWindow(){
        View contentView = LayoutInflater.from(LogInActivity.this).inflate(R.layout.register, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        Button register = contentView.findViewById(R.id.register);
        TextView back = contentView.findViewById(R.id.back_register);
        final EditText userName = contentView.findViewById(R.id.fill_user_name);
        final EditText userPassword = contentView.findViewById(R.id.fill_password);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              显示动画正在注册中
                String name = userName.getText().toString();
                String psw = userPassword.getText().toString();
                String param = "username="+name+"&password="+psw;
                HttpUrl.sendHttpRequest(Config.REGISTER, param, new HttpUrl.Callback() {
                            @Override
                            public void onFinish(String response) {
                                jsonOfRegister(response);
                            }

                            @Override
                            public void onError(final String error) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUrl.showError(error);
                                        mPopWindow.dismiss();
                                    }
                                });

                            }
                        });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        View rootView = LayoutInflater.from(LogInActivity.this).inflate(R.layout.activity_log_in, null);
        mPopWindow.setAnimationStyle(R.style.RegisterShow);
        mPopWindow.setTouchable(true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    private void jsonOfRegister(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            int status = jsonObject.getInt("status");
            final String info = jsonObject.getString("info");
            if (status == 200){
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                final String name1 = jsonObject1.getString("username");
                Log.d("LogIn",name1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPopWindow.dismiss();
                        Toast.makeText(LogInActivity.this,"注册成功，请重新输入！",
                                Toast.LENGTH_SHORT).show();
                        EditText name = findViewById(R.id.name);
                        name.setText(name1);
                    }
                });

            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LogInActivity.this,info,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void jsonOfLogIn(String data){
        try{
            JSONObject jsonObject = new JSONObject(data);
            int status = jsonObject.getInt("status");
            final String info = jsonObject.getString("info");
            if (status == 200){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LogInActivity.this,"登陆成功！",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                MyApplication.setUserId(jsonObject1.getInt("id"));
                MyApplication.setUserAvatar(jsonObject1.getString("avatar"));
                MyApplication.setUserName(jsonObject1.getString("username"));
                MyApplication.setUserToken(jsonObject1.getString("token"));
                start();
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LogInActivity.this,info,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void start(){
        Intent intent = new Intent(LogInActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
