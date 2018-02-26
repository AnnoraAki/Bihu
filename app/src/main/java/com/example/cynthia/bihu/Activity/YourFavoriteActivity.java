package com.example.cynthia.bihu.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cynthia.bihu.Adapter.AnswerAdapter;
import com.example.cynthia.bihu.Adapter.QuestionAdapter;
import com.example.cynthia.bihu.Config;
import com.example.cynthia.bihu.Data.Answer;
import com.example.cynthia.bihu.Data.Question;
import com.example.cynthia.bihu.R;
import com.example.cynthia.bihu.Tools.HttpUrl;
import com.example.cynthia.bihu.Tools.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YourFavoriteActivity extends BaseActivity {

    ImageView back;
    RecyclerView favoriteRv;
    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_favorite);

        favoriteRv = findViewById(R.id.favorite_recycler);
        back = findViewById(R.id.back_favorite);

        initData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){
        String param = "page=0&count=15&token="+ MyApplication.getMUser().getToken();
        HttpUrl.sendHttpRequest(Config.GET_FAVORITE_LIST, param, new HttpUrl.Callback() {
            @Override
            public void onFinish(String response) {
                jsonFavoriteList(response);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void jsonFavoriteList(String data){
        try{
            Log.d("FavoriteList","成功传达数据");
            JSONObject jsonObject = new JSONObject(data);
            int status = jsonObject.getInt("status");
            String info = jsonObject.getString("info");
            if (status == 200){
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                int totalPage = jsonObject1.getInt("totalPage");
                Log.d("FavoriteList","totalPage="+totalPage);
                JSONArray jsonArray = jsonObject1.getJSONArray("questions");
                questions = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    Question question = new Question();
                    question.setId(jsonObject2.getInt("id"));
                    question.setTitle(jsonObject2.getString("title"));
                    question.setContent(jsonObject2.getString("content"));
                    question.setDate(jsonObject2.getString("date"));
                    question.setExciting(jsonObject2.getInt("exciting"));
                    question.setNaive(jsonObject2.getInt("naive"));
                    question.setRecent(jsonObject2.getString("recent"));
                    question.setAnswerCount(jsonObject2.getInt("answerCount"));
                    question.setAuthorId(jsonObject2.getInt("authorId"));
                    question.setAuthorName(jsonObject2.getString("authorName"));
                    question.setAuthorAvatar(jsonObject2.getString("authorAvatar"));
                    question.setIs_exciting(jsonObject2.getBoolean("is_exciting"));
                    question.setIs_naive(jsonObject2.getBoolean("is_naive"));
                    question.setIs_favorite(true);
                    questions.add(question);
                }
                int curPage = jsonObject1.getInt("curPage");
                Log.d("FavoriteList","列表是否有内容:"+questions.size());
                Log.d("FavoriteList","处理完毕，view加载中..当前为第"+curPage+"页");
                initView();
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
                Toast.makeText(YourFavoriteActivity.this,info,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                favoriteRv.setLayoutManager(new LinearLayoutManager(YourFavoriteActivity.this));
                QuestionAdapter questionAdapter = new QuestionAdapter(questions,MyApplication.getMUser().getToken());
                favoriteRv.setAdapter(questionAdapter);
            }
        });

    }
}
