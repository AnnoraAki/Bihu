package com.example.cynthia.bihu.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cynthia.bihu.Config;
import com.example.cynthia.bihu.R;
import com.example.cynthia.bihu.Tools.HttpUrl;
import com.example.cynthia.bihu.Tools.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class AskQuestionActivity extends BaseActivity {

    EditText fillTitle;
    EditText fillContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        ImageView backToPrevious = findViewById(R.id.back_ask);
        TextView addPhoto = findViewById(R.id.add_photo);
        fillTitle = findViewById(R.id.send_title);
        fillContext = findViewById(R.id.send_context);
        Button send = findViewById(R.id.publish);

        backToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AskQuestionActivity.this,"因为技术问题并没有实现，请见谅2333",
                        Toast.LENGTH_SHORT).show();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = fillTitle.getText().toString();
                String content = fillContext.getText().toString();
                String param = "title="+title+"&content="+content+"&images=&token="+ MyApplication.getMUser().getToken();
                HttpUrl.sendHttpRequest(Config.QUESTION, param, new HttpUrl.Callback() {
                    @Override
                    public void onFinish(String response) {
                        jsonQuestion(response);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });
    }

    private void jsonQuestion(String data)  {
        Log.d("AskQuestion","请求成功，数据处理中...");
        try {
            JSONObject object = new JSONObject(data);
            int status = object.getInt("status");
            String info = object.getString("info");
            if (status == 200){
                showToast("发表成功！");
                Intent intent = new Intent(AskQuestionActivity.this,MainActivity.class);
                startActivity(intent);
            }else{
                showToast(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showToast(final String info){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AskQuestionActivity.this,info,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
