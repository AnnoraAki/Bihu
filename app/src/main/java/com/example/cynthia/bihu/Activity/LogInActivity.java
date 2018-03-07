package com.example.cynthia.bihu.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    EditText name;
    EditText psw;
    Button logIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      设置透明状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_log_in);
        logIn = findViewById(R.id.logIn);
        name = findViewById(R.id.name);
        psw = findViewById(R.id.pw);
        TextView register = findViewById(R.id.register_new);

        loadData();

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                final String userPw = psw.getText().toString();
                String param = "username="+userName+"&password="+userPw;
                Log.d("param",param);
//          添加动画显示登陆中
            HttpUrl.sendHttpRequest(Config.LOGIN, param, new HttpUrl.Callback() {
                @Override
                public void onFinish(String response) {
                    MyApplication.setPassword(userPw);
                    jsonOfLogIn(response,false);
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

    private void loadData(){
        SharedPreferences sp = getSharedPreferences("user",Context.MODE_PRIVATE);
        String name = sp.getString("userName",null);
        final String password = sp.getString("userPassword",null);
        Log.d("读取数据","name="+name+" pw="+password);
        if (name != null && password != null){
            String param = "username="+name+"&password="+password;
            Log.d("param",param);
//          添加动画显示登陆中
            HttpUrl.sendHttpRequest(Config.LOGIN, param, new HttpUrl.Callback() {
                @Override
                public void onFinish(String response) {
                    MyApplication.setPassword(password);
                    jsonOfLogIn(response,true);
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
        }else{

        }
    }

    private void showPopupWindow(){
        View contentView = LayoutInflater.from(LogInActivity.this).inflate(R.layout.register, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        Button register = contentView.findViewById(R.id.register);
        TextView back = contentView.findViewById(R.id.back_register);
        final EditText uName = contentView.findViewById(R.id.fill_user_name);
        final EditText uPassword = contentView.findViewById(R.id.fill_password);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              显示动画正在注册
                String userName = uName.getText().toString();
                String userPw = uPassword.getText().toString();
                if (userName.equals("") || userPw.equals("")){
                    ToastUrl.showError("请填写用户名或密码!");
//                  使得输入法消失
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(uName.getWindowToken(), 0);
                } else if (userName.length() < 2){
                    ToastUrl.showError("用户名过短");
                    uName.setText("");
                }else if (userPw.length() < 5){
                    ToastUrl.showError("密码过短");
                    uPassword.setText("");
                } else {
                    String param = "username="+userName+"&password="+userPw;
                    Log.d("register","param="+param);
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

    private void jsonOfRegister(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            MyApplication.setUserId(jsonObject1.getInt("id"));
            MyApplication.setUserAvatar(jsonObject1.getString("avatar"));
            MyApplication.setUserName(jsonObject1.getString("username"));
            MyApplication.setUserToken(jsonObject1.getString("token"));
            MyApplication.setPassword(jsonObject1.getString("password"));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPopWindow.dismiss();
                    ToastUrl.showResponse("注册成功！将为您自动登录...");
                    start(false);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void jsonOfLogIn(String data,Boolean reload) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUrl.showResponse("欢迎来到逼乎社区!");
                }
            });
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            MyApplication.setUserId(jsonObject1.getInt("id"));
            MyApplication.setUserAvatar(jsonObject1.getString("avatar"));
            MyApplication.setUserName(jsonObject1.getString("username"));
            MyApplication.setUserToken(jsonObject1.getString("token"));
            start(reload);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void start(Boolean reload){
        if (!reload){
            SharedPreferences sharedPreferences = getSharedPreferences("user",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName",MyApplication.getMUser().getUsername());
            editor.putString("userPassword",MyApplication.getPassword());
            editor.commit();
            Log.d("存储数据","succeed");
        }
        Intent intent = new Intent(LogInActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
