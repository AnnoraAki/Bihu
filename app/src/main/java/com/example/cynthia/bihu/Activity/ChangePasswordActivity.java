package com.example.cynthia.bihu.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.cynthia.bihu.Config;
import com.example.cynthia.bihu.R;
import com.example.cynthia.bihu.Tools.ActivityCollector;
import com.example.cynthia.bihu.Tools.HttpUrl;
import com.example.cynthia.bihu.Tools.MyApplication;
import com.example.cynthia.bihu.Tools.StatusBarUrl;
import com.example.cynthia.bihu.Tools.ToastUrl;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends BaseActivity {

    ImageView back;
    EditText originPw;
    EditText newPw;
    Button sure;
    String newToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUrl.setColor(this,R.color.barColor);
        setContentView(R.layout.activity_change_password);

        back = findViewById(R.id.back_change_pw);
        originPw = findViewById(R.id.origin_pw);
        newPw = findViewById(R.id.new_pw);
        sure = findViewById(R.id.sure);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String originPassword = originPw.getText().toString();
                final String newPassword = newPw.getText().toString();
                if (checkOriginPw(originPassword)&&checkNewPw(newPassword)){
                    String param = "password="+newPassword+"&token="+MyApplication.getMUser().getToken();
                    HttpUrl.sendHttpRequest(Config.CHANGE_PASSWORD, param, new HttpUrl.Callback() {
                        @Override
                        public void onFinish(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONObject object1 = object.getJSONObject("data");
                                newToken = object1.getString("token");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUrl.showResponse("更改成功！");
                                    MyApplication.setPassword(newPassword);
                                    MyApplication.setUserToken(newToken);
                                    finish();
                                }
                            });

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
                }else if (checkOriginPw(originPassword) == false){
                    ToastUrl.showError("原密码输入错误，请重新输入");
                    originPw.setText("");

                }else {
                    ToastUrl.showError("新设置密码与原密码相同，请重新设置新密码");
                    newPw.setText("");

                }
            }
        });
    }

    private boolean checkOriginPw(String oPw){
        return oPw.equals(MyApplication.getPassword());
    }

    private boolean checkNewPw(String nPw){
        return !nPw.equals(MyApplication.getPassword());
    }
}
