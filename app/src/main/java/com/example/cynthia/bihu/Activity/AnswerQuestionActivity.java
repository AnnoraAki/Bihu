package com.example.cynthia.bihu.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cynthia.bihu.Adapter.AnswerAdapter;
import com.example.cynthia.bihu.Config;
import com.example.cynthia.bihu.Data.Answer;
import com.example.cynthia.bihu.Data.Question;
import com.example.cynthia.bihu.R;
import com.example.cynthia.bihu.Tools.BitmapUrl;
import com.example.cynthia.bihu.Tools.CircleImageView;
import com.example.cynthia.bihu.Tools.DateUrl;
import com.example.cynthia.bihu.Tools.HttpUrl;
import com.example.cynthia.bihu.Tools.MyApplication;
import com.example.cynthia.bihu.Tools.ToastUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnswerQuestionActivity extends BaseActivity {

    private ImageView back;
    private TextView title;
    private TextView authorName;
    private TextView context;
    private ImageView naive;
    private ImageView exciting;
    private ImageView favorite;
    private ImageView image;
    private CircleImageView authorAvatar;
    private TextView naiveNum;
    private TextView excitingNum;
    private TextView date;
    private TextView commentNum;
    private EditText comment;
    private Button send;
    private int qId;
    private int totalCount;
    private int totalPage;
    private int curPage;
    private RecyclerView mAnswerQv;
    private SwipeRefreshLayout mRefresh;
    private Question mQuestion = new Question();
    private ArrayList<Answer> answers = new ArrayList<>();
    private AnswerAdapter answerAdapter;

    private boolean mloading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        back = findViewById(R.id.back_more);
        title = findViewById(R.id.more_title);
        authorName = findViewById(R.id.more_user_id);
        authorAvatar = findViewById(R.id.more_user_image);
        context = findViewById(R.id.more_context);
        comment = findViewById(R.id.text_comment);
        send = findViewById(R.id.send_comment);
        naive = findViewById(R.id.more_naive);
        exciting = findViewById(R.id.more_exciting);
        naiveNum = findViewById(R.id.more_record_naive);
        excitingNum = findViewById(R.id.more_record_exciting);
        favorite = findViewById(R.id.more_favorite);
        date = findViewById(R.id.more_date);
        image = findViewById(R.id.more_context_image);
        commentNum = findViewById(R.id.more_comment_num);
        mAnswerQv = findViewById(R.id.answer_recycler);
        mRefresh = findViewById(R.id.refresh_answer);


        setUpRefresh();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAnswerQv.setLayoutManager(layoutManager);
        mAnswerQv.setNestedScrollingEnabled(false);
        mRefresh.post(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = comment.getText().toString();
                String param = "qid="+qId+"&content="+data+"&image=&token="+MyApplication.getMUser().getToken();
                Log.d("发送回答",param);
                HttpUrl.sendHttpRequest(Config.ANSWER, param, new HttpUrl.Callback() {
                    @Override
                    public void onFinish(String response) {
                        jsonResponse(response);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

            }
        });

        exciting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickExciting();
                if(mQuestion.isIs_exciting()){
                    exciting.setImageResource(R.drawable.ic_good);
                    excitingNum.setText("("+(mQuestion.getExciting()-1)+")");
                    mQuestion.setIs_exciting(false);
                    mQuestion.setExciting(mQuestion.getExciting()-1);
                }else{
                    exciting.setImageResource(R.drawable.ic_good_selected);
                    excitingNum.setText("("+(mQuestion.getExciting()+1)+")");
                    mQuestion.setIs_exciting(true);
                    mQuestion.setExciting(mQuestion.getExciting()+1);
                }
            }
        });

        naive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNaive();
                if(mQuestion.isIs_naive()){
                    naive.setImageResource(R.drawable.ic_bad);
                    naiveNum.setText("("+(mQuestion.getNaive()-1)+")");
                    mQuestion.setIs_naive(false);
                    mQuestion.setNaive(mQuestion.getNaive()-1);
                }else{
                    naive.setImageResource(R.drawable.ic_bad_selected);
                    naiveNum.setText("("+(mQuestion.getNaive()+1)+")");
                    mQuestion.setIs_naive(true);
                    mQuestion.setNaive(mQuestion.getNaive()+1);
                }
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFavorite();
                if(mQuestion.isIs_favorite()){
                    favorite.setImageResource(R.drawable.ic_favorite1);
                    mQuestion.setIs_favorite(false);
                }else{
                    favorite.setImageResource(R.drawable.ic_favorite1_selected);
                    mQuestion.setIs_favorite(true);
                }
            }
        });
    }

    private void setUpRefresh() {
        mRefresh.setColorSchemeResources(R.color.colorSelected);

        mRefresh.post(new Runnable() {
            @Override
            public void run() {
               mRefresh.setRefreshing(true);
               initData(false);
            }
        });

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(true);
            }
        });

    }

    private void initData(Boolean reload){
        if (!reload) {
            Intent intent = getIntent();
            loadQuestion(intent);
            qId = intent.getIntExtra("qId", -1);
            title.setText(mQuestion.getTitle());
            authorName.setText(mQuestion.getAuthorName());
            context.setText(mQuestion.getContent());
            excitingNum.setText("(" + mQuestion.getExciting() + ")");
            naiveNum.setText("(" + mQuestion.getNaive() + ")");
            commentNum.setText("共" + mQuestion.getAnswerCount() + "条评论");

            if (mQuestion.getRecent().equals("null")) {
                String time = DateUrl.getDate(mQuestion.getDate());
                date.setText(time + "发布");
            } else {
                String time = DateUrl.getDate(mQuestion.getRecent());
                date.setText(time + "更新");
            }

            int naiveImage = mQuestion.isIs_naive() ?
                    (R.drawable.ic_bad_selected) : (R.drawable.ic_bad);
            int excitingImage = mQuestion.isIs_exciting() ?
                    (R.drawable.ic_good_selected) : (R.drawable.ic_good);
            int favoriteImage = mQuestion.isIs_favorite() ?
                    (R.drawable.ic_favorite1_selected) : (R.drawable.ic_favorite1);

            naive.setImageResource(naiveImage);
            exciting.setImageResource(excitingImage);
            favorite.setImageResource(favoriteImage);

            if (!mQuestion.getAuthorAvatar().equals("null")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = BitmapUrl.getBitmap(mQuestion.getAuthorAvatar());
                        if (bitmap != null) {
                            authorAvatar.post(new Runnable() {
                                @Override
                                public void run() {
                                    authorAvatar.setImageBitmap(bitmap);
                                }
                            });
                        } else {
                            authorAvatar.post(new Runnable() {
                                @Override
                                public void run() {
                                    authorAvatar.setImageResource(R.drawable.ic_head);
                                    ToastUrl.showError("头像资源加载失败...");
                                }
                            });
                        }
                    }
                }).start();
            } else {
                authorAvatar.setImageResource(R.drawable.ic_head);
            }
            if (!mQuestion.getImages().equals("null")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = BitmapUrl.getBitmap(mQuestion.getImages());
                        if (bitmap != null) {
                            image.post(new Runnable() {
                                @Override
                                public void run() {
                                    image.setImageBitmap(bitmap);
                                }
                            });
                        } else {
                            image.post(new Runnable() {
                                @Override
                                public void run() {
                                    image.setVisibility(View.GONE);
//                                    有些返回就是空的。。不是null...
//                                    ToastUrl.showError("图片资源加载失败...");
                                }
                            });
                        }
                    }
                }).start();
            } else {
                image.setVisibility(View.GONE);
            }
        }

        String param = "page=0&count="+mQuestion.getAnswerCount()+"&qid="+qId+"&token="+MyApplication.getMUser().getToken();

        HttpUrl.sendHttpRequest(Config.GET_ANSWER_LIST, param, new HttpUrl.Callback() {
            @Override
            public void onFinish(String response) {
                jsonAnswerList(response,false);
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

    private void jsonResponse(String data){
        Log.d("Answer","网络申请完成");
        Log.d("Answer",data);
        try {
            JSONObject object = new JSONObject(data);
            int status = object.getInt("status");
            final String info = object.getString("info");
            if (status == 200){
                Log.d("Answer","succeed");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUrl.showResponse("回答发布成功！");
                        comment.setText("");
                        comment.clearFocus();
                        mQuestion.setAnswerCount(mQuestion.getAnswerCount()+1);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(comment.getWindowToken(), 0);
                        mRefresh.post(new Runnable() {
                            @Override
                            public void run() {
                                initData(true);
                            }
                        });
                    }
                });

            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUrl.showError(info);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void jsonAnswerList(String data,Boolean reload){
        try{
            Log.d("ARv","成功传达数据");
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            totalPage = jsonObject1.getInt("totalPage");
            totalCount = jsonObject1.getInt("totalCount");
            Log.d("AnswerList","totalPage="+totalPage);
            JSONArray jsonArray = jsonObject1.getJSONArray("answers");
            ArrayList<Answer> answerList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Answer answer = new Answer();
                answer.setId(jsonObject2.getInt("id"));
                answer.setContent(jsonObject2.getString("content"));
                answer.setDate(jsonObject2.getString("date"));
                answer.setExciting(jsonObject2.getInt("exciting"));
                answer.setNaive(jsonObject2.getInt("naive"));
                answer.setAuthorId(jsonObject2.getInt("authorId"));
                answer.setAuthorName(jsonObject2.getString("authorName"));
                answer.setAuthorAvatar(jsonObject2.getString("authorAvatar"));
                answer.setIs_exciting(jsonObject2.getBoolean("is_exciting"));
                answer.setIs_naive(jsonObject2.getBoolean("is_naive"));
                answer.setBest(jsonObject2.getInt("best"));
                answerList.add(answer);
            }
            curPage = jsonObject1.getInt("curPage");
                Log.d("ARv","处理完毕，view加载中...当前为第"+curPage+"页");
                mQuestion.setAnswerCount(totalCount);
                if (!reload){
                    answers.clear();
                    answers.addAll(answerList);
                    initView();
                }else {
                    answers.addAll(answerList);
                    mloading = false;
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefresh.setRefreshing(false);
                    }
                });
                commentNum.setText("共"+mQuestion.getAnswerCount()+"条评论");

                answerAdapter = new AnswerAdapter(answers,
                        MyApplication.getMUser().getToken(),qId,mQuestion.getAuthorId(),
                        MyApplication.getMUser().getId());
                mAnswerQv.setAdapter(answerAdapter);

                mloading = false;
            }
        });

    }

    private void loadQuestion(Intent intent){
        mQuestion.setTitle(intent.getStringExtra("title"));
        mQuestion.setAuthorName(intent.getStringExtra("authorName"));
        mQuestion.setContent(intent.getStringExtra("content"));
        mQuestion.setExciting(intent.getIntExtra("excitingNum",0));
        mQuestion.setNaive(intent.getIntExtra("naiveNum",0));
        mQuestion.setAuthorId(intent.getIntExtra("authorId",0));
        mQuestion.setAnswerCount(intent.getIntExtra("answerCount",0));
        mQuestion.setDate(intent.getStringExtra("date"));
        mQuestion.setRecent(intent.getStringExtra("recent"));
        mQuestion.setAuthorAvatar(intent.getStringExtra("authorAvatar"));
        mQuestion.setImages(intent.getStringExtra("image"));
        mQuestion.setIs_naive(intent.getBooleanExtra("naive",false));
        mQuestion.setIs_exciting(intent.getBooleanExtra("exciting",false));
        mQuestion.setIs_favorite(intent.getBooleanExtra("favorite",false));
    }

    private void clickExciting(){
        String param = "id="+qId+"&type=1&token="+MyApplication.getMUser().getToken();
        if(mQuestion.isIs_exciting()){
            HttpUrl.sendHttpRequest(Config.CANCEL_EXCITING, param, new HttpUrl.Callback() {
                @Override
                public void onFinish(String response) {
                    Log.d("取消喜欢","succeed");

                }

                @Override
                public void onError(String error) {
                    Log.d("取消喜欢",error);

                }
            });
        }else{
            HttpUrl.sendHttpRequest(Config.EXCITING, param, new HttpUrl.Callback() {
                @Override
                public void onFinish(String response) {
                    Log.d("喜欢","succeed");
                }

                @Override
                public void onError(String error) {
                    Log.d("喜欢",error);
                }
            });
        }
    }

    private void clickNaive(){
        String param = "id="+qId+"&type=1&token="+MyApplication.getMUser().getToken();
        if(mQuestion.isIs_naive()){
            HttpUrl.sendHttpRequest(Config.CANCEL_NAIVE, param, new HttpUrl.Callback() {
                @Override
                public void onFinish(String response) {
                    Log.d("取消讨厌","succeed");

                }

                @Override
                public void onError(String error) {
                    Log.d("取消讨厌",error);

                }
            });
        }else{
            HttpUrl.sendHttpRequest(Config.NAIVE, param, new HttpUrl.Callback() {
                @Override
                public void onFinish(String response) {
                    Log.d("讨厌","succeed");
                }

                @Override
                public void onError(String error) {
                    Log.d("讨厌",error);
                }
            });
        }
    }

    private void clickFavorite(){
        String param = "qid="+qId+"&token="+MyApplication.getMUser().getToken();
        if(mQuestion.isIs_favorite()){
            HttpUrl.sendHttpRequest(Config.CANCEL_FAVORITE, param, new HttpUrl.Callback() {
                @Override
                public void onFinish(String response) {
                    Log.d("取消收藏","succeed");

                }

                @Override
                public void onError(String error) {
                    Log.d("取消收藏",error);

                }
            });
        }else{
            HttpUrl.sendHttpRequest(Config.FAVORITE, param, new HttpUrl.Callback() {
                @Override
                public void onFinish(String response) {
                    Log.d("收藏","succeed");
                }

                @Override
                public void onError(String error) {
                    Log.d("收藏",error);
                }
            });
        }
    }

}

