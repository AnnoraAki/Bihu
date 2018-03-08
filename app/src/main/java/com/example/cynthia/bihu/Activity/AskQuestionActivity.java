package com.example.cynthia.bihu.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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
import com.example.cynthia.bihu.Tools.BitmapUrl;
import com.example.cynthia.bihu.Tools.HttpUrl;
import com.example.cynthia.bihu.Tools.MyApplication;
import com.example.cynthia.bihu.Tools.StatusBarUrl;
import com.example.cynthia.bihu.Tools.ToastUrl;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

public class AskQuestionActivity extends BaseActivity {

    EditText fillTitle;
    EditText fillContext;
    ImageView image;
    private String imagePath;
    private String update;
    private String title;
    private String content;

    protected  UploadManager uploadManager;
    private String filename = "p4ugmd4b8.bkt.clouddn.com";
    private String accessKey = "Wc1wt1WPEosGdBeX-HU-M2YQeseQBHwASr8ZMRbE";
    private String secretKey = "y0QwVBv_3avW1Xl5s32h51iZaKgq7gbUUtubind1";
    private String bucket = "cchanges";
    private String updateToken;
    private String upKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUrl.setColor(this);
        setContentView(R.layout.activity_ask_question);

        ImageView backToPrevious = findViewById(R.id.back_ask);
        TextView addPhoto = findViewById(R.id.add_photo);
        fillTitle = findViewById(R.id.send_title);
        fillContext = findViewById(R.id.send_context);
        image = findViewById(R.id.send_image);
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
                showDialog();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = fillTitle.getText().toString();
                content = fillContext.getText().toString();
                if (imagePath == null){
                    update = null;
                    String param = "title="+title+"&content="+content+"&images=&token="+ MyApplication.getMUser().getToken();
                    HttpUrl.sendHttpRequest(Config.QUESTION, param, new HttpUrl.Callback() {
                        @Override
                        public void onFinish(String response) {
                            jsonQuestion(response);
                        }

                        @Override
                        public void onError(String error) {
                            ToastUrl.showError(error);
                        }
                    });
                }else{
                    getUpdate(imagePath);
                }

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
                finish();
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
                ToastUrl.showResponse(info);
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AskQuestionActivity.this);
        builder.setTitle("您想使用哪儿的照片？");
        builder.setMessage("一篇文章只能上传一张照片ovo");
        builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkAndOpenAlbum();
            }
        });
        builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openCamera();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA:
                if (resultCode == RESULT_OK){
                    imagePath = getImagePath(imageUri,null);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    image.setImageBitmap(bitmap);
                }
                break;
            case ALBUM:
                if (resultCode == RESULT_OK){
//                  判断版本处理图片的Uri
                    if (Build.VERSION.SDK_INT >= 19){
                        imagePath = handleImageOnKitKat(data);
                    } else {
                        imagePath = handleImageBeforeKitKat(data);
                    }
                    if (imagePath != null){
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        image.setImageBitmap(bitmap);
                    } else {
                        ToastUrl.showError("获取图片失败，请重试 :(");
                    }
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK){
                    if (imageUri != null){
                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(MyApplication.getContext().getContentResolver().openInputStream(data.getData()));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        image.setImageBitmap(bitmap);
                    }else{
                        ToastUrl.showError("获取图片失败，请重试 :(");
                    }
                }
                break;
            default:
                break;
        }
    }

//  为什么不写在bitmap类里面？
//  因为线程没理清楚导致图片还没在七牛云上传成功就直接把七牛云地址传到了服务器:)
//  为了不gg还是就放在这儿了
    protected void getUpdate(String imagePath){
        upKey = "image"+System.currentTimeMillis();
        String param = "accessKey="+accessKey+"&secretKey="+secretKey+"&bucket="+bucket;
        HttpUrl.sendHttpRequest(Config.GET_TOKEN, param, new HttpUrl.Callback() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    updateToken = jsonObject.getString("token");
                    upToQiniu();
                } catch (JSONException e) {
                    e.printStackTrace();}
            }
            @Override
            public void onError(String error) {
                        ToastUrl.showError("不明原因导致图片上传云服务器失败...");
                    }
        });
    }

    protected void upToQiniu(){
        Configuration configuration = new Configuration.Builder()
                .zone(FixedZone.zone2).build();
        uploadManager = new UploadManager(configuration);
        uploadManager.put(imagePath, upKey, updateToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    upToServers();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUrl.showError("上传失败，请重试  :)");
                        }
                    });
                }
            }
        }, null);
    }

    protected void upToServers(){
        String update = "http://"+filename+"/"+upKey+"?imageView2/2/w/200/h/200/q/75|imageslim";
        String param = "title="+title+"&content="+content+"&images="+update+"&token="+ MyApplication.getMUser().getToken();
        HttpUrl.sendHttpRequest(Config.QUESTION, param, new HttpUrl.Callback() {
            @Override
            public void onFinish(String response) {
                jsonQuestion(response);
            }

            @Override
            public void onError(String error) {
                ToastUrl.showError(error);
            }
        });
    }
}


