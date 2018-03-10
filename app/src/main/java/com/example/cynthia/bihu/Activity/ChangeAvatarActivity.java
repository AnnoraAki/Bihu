package com.example.cynthia.bihu.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cynthia.bihu.Fragment.SettingFragment;
import com.example.cynthia.bihu.R;
import com.example.cynthia.bihu.Tools.BitmapUrl;
import com.example.cynthia.bihu.Tools.CircleImageView;
import com.example.cynthia.bihu.Tools.MyApplication;
import com.example.cynthia.bihu.Tools.StatusBarUrl;
import com.example.cynthia.bihu.Tools.ToastUrl;

import java.io.FileNotFoundException;

public class ChangeAvatarActivity extends BaseActivity {

    ImageView back;
    CircleImageView avatar;
    Button chooseCamera;
    Button chooseAlbum;
    Button sure;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUrl.setColor(this,R.color.barColor);
        setContentView(R.layout.activity_change_avatar);
        back = findViewById(R.id.back_change_a);
        avatar = findViewById(R.id.show_image);
        chooseAlbum = findViewById(R.id.choose_album);
        chooseCamera = findViewById(R.id.choose_camera);
        sure = findViewById(R.id.choose_sure);

        initData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chooseCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openCamera();
                ToastUrl.showResponse("暂时不支持拍照上传~");
            }
        });

        chooseAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndOpenAlbum();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapUrl.upToQiniu(imagePath,ChangeAvatarActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5*1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(ChangeAvatarActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();

            }
        });
    }

    private void initData(){
        if (MyApplication.getMUser().getAvatar().equals("null") == false){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = BitmapUrl.getBitmap(MyApplication.getMUser().getAvatar());
                    avatar.post(new Runnable() {
                        @Override
                        public void run() {
                            avatar.setImageBitmap(bitmap);
                        }
                    });
                }
            }).start();
        } else {
            avatar.setImageResource(R.drawable.ic_head);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA:
                if (resultCode == RESULT_OK){
//                    cropImage("file://" + getExternalCacheDir() + "/" + System.currentTimeMillis());
                    imagePath = getImagePath(imageUri,null);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    avatar.setImageBitmap(bitmap);
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
                        avatar.setImageBitmap(bitmap);
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
                        avatar.setImageBitmap(bitmap);
                    }else{
                        ToastUrl.showError("获取图片失败，请重试 :(");
                    }
                }
                break;
            default:
                break;
        }
    }
}
