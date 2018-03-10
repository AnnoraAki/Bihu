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
import com.example.cynthia.bihu.Tools.StatusBarUrl;
import com.example.cynthia.bihu.Tools.ToastUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YourFavoriteActivity extends BaseActivity {

    ImageView back;
    RecyclerView favoriteRv;
    private ArrayList<Question> questions = new ArrayList<>();
    private QuestionAdapter questionAdapter;
    private int totalPage;
    private int totalCount;
    private int curPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUrl.setColor(this,R.color.barColor);
        setContentView(R.layout.activity_your_favorite);

        favoriteRv = findViewById(R.id.favorite_recycler);
        back = findViewById(R.id.back_favorite);

        initData();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        favoriteRv.setLayoutManager(layoutManager);

        favoriteRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                Log.d("VisibleItem",""+lastVisibleItem);
                if (lastVisibleItem % 15 == 0 && lastVisibleItem != totalCount ){
                    questionAdapter.changeMoreStatus(1);
                    if (questions.size() != totalCount ){
                        loadData();
                    }
                }else if (lastVisibleItem == totalCount){
                    questionAdapter.changeMoreStatus(2);
                }
            }
        });

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
                jsonFavoriteList(response,false);
            }

            @Override
            public void onError(String error) {
                ToastUrl.showError(error);
            }
        });
    }

    private void jsonFavoriteList(String data, Boolean reload) {
        try {
            Log.d("FavoriteList", "成功传达数据");
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            totalPage = jsonObject1.getInt("totalPage");
            totalCount = jsonObject1.getInt("totalCount");
            Log.d("FavoriteList", "totalPage=" + totalPage);
            JSONArray jsonArray = jsonObject1.getJSONArray("questions");
            final ArrayList<Question> questions1 = new ArrayList<>();
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
                String images = jsonObject2.getString("images");
                Log.d("images=",images);
                if (images.contains(",")){
                    String[] images1 = images.split(",");
                    String image = images1[0];
                    Log.d("处理后image=",image);
                    question.setImages(image);
                } else {
                    question.setImages(images);
                }
                question.setIs_exciting(jsonObject2.getBoolean("is_exciting"));
                question.setIs_naive(jsonObject2.getBoolean("is_naive"));
                question.setIs_favorite(true);
                questions1.add(question);
            }
            curPage = jsonObject1.getInt("curPage");
            Log.d("FavoriteList", "列表是否有内容:" + questions.size());
            Log.d("FavoriteList", "处理完毕，view加载中..当前为第" + curPage + "页");
            if (!reload) {
                questions.clear();
                questions.addAll(questions1);
                Log.d("QRv", "列表是否有内容:" + questions.size());
                initView();
            } else {

                questions.addAll(questions1);
                Log.d("QRv Loading", "列表是否有内容:" + questions.size());


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                questionAdapter = new QuestionAdapter(questions,MyApplication.getMUser().getToken());
                favoriteRv.setAdapter(questionAdapter);
            }
        });

    }

    public void loadData(){
        String param = "page="+(curPage+1)+"&count=15&token="+ MyApplication.getMUser().getToken();
        HttpUrl.sendHttpRequest(Config.GET_FAVORITE_LIST, param, new HttpUrl.Callback() {
            @Override
            public void onFinish(String response) {
                jsonFavoriteList(response, true);
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
}
