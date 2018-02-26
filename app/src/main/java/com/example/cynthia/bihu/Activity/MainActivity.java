package com.example.cynthia.bihu.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cynthia.bihu.Fragment.MainFragment;
import com.example.cynthia.bihu.Fragment.SettingFragment;
import com.example.cynthia.bihu.R;
import com.example.cynthia.bihu.Tools.MyApplication;

import java.util.List;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private String TAG1 = "MainFragment";
    private String TAG2 = "SettingFragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        initView();
    }

    protected void initView(){
        RadioGroup radioGroup = findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(this);
        ImageView choose = findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        addFragment1();
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()){
            case  R.id.main:
                hideFragment(TAG2);
                showFragment(TAG1);
                break;
            case  R.id.self:
                FragmentManager manager = getSupportFragmentManager();
                if (manager.findFragmentByTag(TAG2)==null){
                    addFragment2();
                    hideFragment(TAG1);
                }else {
                    showFragment(TAG2);
                    hideFragment(TAG1);
                }
                break;
        }
    }

    private void addFragment1() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name",MyApplication.getMUser().getUsername());
        bundle.putString("avatar",MyApplication.getMUser().getAvatar());
        bundle.putString("token",MyApplication.getMUser().getToken());
        mainFragment.setArguments(bundle);
        transaction.add(R.id.container, mainFragment, TAG1);
        transaction.commit();
    }

    private void addFragment2() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SettingFragment settingFragment = new SettingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name",MyApplication.getMUser().getUsername());
        bundle.putString("avatar",MyApplication.getMUser().getAvatar());
        bundle.putString("token",MyApplication.getMUser().getToken());
        settingFragment.setArguments(bundle);
        transaction.add(R.id.container, settingFragment, TAG2);
        transaction.commit();
    }

    private void hideFragment(String tag){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);
        transaction.hide(fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showFragment(String tag){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);
        transaction.show(fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showPopupWindow(){
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.choose, null);
        final PopupWindow mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        ImageView wantSend = contentView.findViewById(R.id.want_send);
        ImageView wangSeeFavorite = contentView.findViewById(R.id.want_see_favorite);
        ImageView cancel = contentView.findViewById(R.id.cancel);

        wantSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AskQuestionActivity.class);
                startActivity(intent);
                mPopWindow.dismiss();
            }
        });

        wangSeeFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,YourFavoriteActivity.class);
                startActivity(intent);
                mPopWindow.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });

        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
//        mPopWindow.setAnimationStyle(R.style.);
        mPopWindow.setTouchable(true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断当前按键是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

