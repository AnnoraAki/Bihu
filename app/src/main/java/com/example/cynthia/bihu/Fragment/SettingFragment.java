package com.example.cynthia.bihu.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cynthia.bihu.Activity.ChangeAvatarActivity;
import com.example.cynthia.bihu.Activity.ChangePasswordActivity;
import com.example.cynthia.bihu.Activity.LogInActivity;
import com.example.cynthia.bihu.Activity.MainActivity;
import com.example.cynthia.bihu.Data.User;
import com.example.cynthia.bihu.R;
import com.example.cynthia.bihu.Tools.BitmapUrl;
import com.example.cynthia.bihu.Tools.CircleImageView;
import com.example.cynthia.bihu.Tools.MyApplication;

/**
 * Created by Cynthia on 2018/2/14.
 */

public class SettingFragment extends android.support.v4.app.Fragment {

    TextView changeAvatar;
    TextView changePassword;
    TextView exit;
    TextView userId;
    CircleImageView userAvatar;
    SharedPreferences sharedPreferences;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        initData(view);

        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeAvatarActivity.class);
                startActivity(intent);
            }
        });

        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeAvatarActivity.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(),LogInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    private void initData(View view){
        changeAvatar = view.findViewById(R.id.change_avatar);
        changePassword = view.findViewById(R.id.change_password);
        exit = view.findViewById(R.id.exit_id);
        userAvatar = view.findViewById(R.id.picture);
        userId = view.findViewById(R.id.show_user_id);
        userId.setText(MyApplication.getMUser().getUsername());
        if (MyApplication.getMUser().getAvatar().equals("null") == false){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = BitmapUrl.getBitmap(MyApplication.getMUser().getAvatar());
                    userAvatar.post(new Runnable() {
                        @Override
                        public void run() {
                            userAvatar.setImageBitmap(bitmap);
                        }
                    });
                }
            }).start();

        }
    }

}
