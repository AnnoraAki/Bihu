package com.example.cynthia.bihu.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cynthia.bihu.Adapter.QuestionAdapter;
import com.example.cynthia.bihu.Config;
import com.example.cynthia.bihu.Data.Question;
import com.example.cynthia.bihu.R;
import com.example.cynthia.bihu.Tools.HttpUrl;
import com.example.cynthia.bihu.Tools.MyApplication;
import com.example.cynthia.bihu.Tools.ToastUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Cynthia on 2018/2/14.
 */

public class MainFragment extends Fragment {

    private ArrayList<Question> questions = new ArrayList<>();
    private RecyclerView questionRv;
    private SwipeRefreshLayout mRefresh;
    private int totalPage;
    private int totalCount;
    private int curPage;
    private Boolean mloading;
    private QuestionAdapter questionAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        questionRv = view.findViewById(R.id.question_recycler);
        mRefresh = view.findViewById(R.id.refresh_question);



        mRefresh.post(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true);
                initData();
            }
        });

        initRefresh();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        questionRv.setLayoutManager(layoutManager);

        questionRv.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!mRefresh.isRefreshing()){
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    Log.d("VisibleItem",""+lastVisibleItem);
                    if (lastVisibleItem % 15 == 0 && lastVisibleItem != totalCount ){
                        questionAdapter.changeMoreStatus(1);
                        if (questions.size() != totalCount && !mloading ){
                            loadData();
                            mloading = true;
                        }
                    }else if (lastVisibleItem == totalCount){
                        questionAdapter.changeMoreStatus(2);
                    }

                }

            }
        });
        return view;
    }

    private void initRefresh() {
        mRefresh.setColorSchemeResources(R.color.colorSelected);

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    private void initData(){
        String param = "page=0&count=15&token="+ MyApplication.getMUser().getToken();
        HttpUrl.sendHttpRequest(Config.GET_QUESTION_LIST, param, new HttpUrl.Callback() {
            @Override
            public void onFinish(String response) {
                jsonQuestions(response,false);

            }

            @Override
            public void onError(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefresh.setRefreshing(false);
                        ToastUrl.showError(error);
                    }
                });
            }
        });
    }

    private void jsonQuestions(String data,Boolean reload){
        try{
            Log.d("Qrv","成功传达数据");
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            totalPage = jsonObject1.getInt("totalPage");
            totalCount = jsonObject1.getInt("totalCount");
            Log.d("MainQuestionsList","totalPage="+totalPage+" totalCount="+totalCount);

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
                    Log.d("处理后image=",images);
                    question.setImages(image);
                } else {
                  question.setImages(images);
                }
                question.setIs_exciting(jsonObject2.getBoolean("is_exciting"));
                question.setIs_naive(jsonObject2.getBoolean("is_naive"));
                question.setIs_favorite(jsonObject2.getBoolean("is_favorite"));
                questions1.add(question);
            }
            curPage = jsonObject1.getInt("curPage");

            Log.d("QRv","处理完毕，view加载中..当前为第"+curPage+"页");
            if (!reload) {
                questions.clear();
                questions.addAll(questions1);
                Log.d("QRv","列表是否有内容:"+questions.size());
                initView();
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        questions.addAll(questions1);
                        Log.d("QRv Loading","列表是否有内容:"+questions.size());
                        mloading = false;
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefresh.setRefreshing(false);
                    }
                });

                questionAdapter = new QuestionAdapter(questions,MyApplication.getMUser().getToken());
                questionRv.setAdapter(questionAdapter);
                mloading = false;
            }
        });

    }

    public void loadData(){
        String param = "page="+(curPage+1)+"&count=15&token="+ MyApplication.getMUser().getToken();
        HttpUrl.sendHttpRequest(Config.GET_QUESTION_LIST, param, new HttpUrl.Callback() {
            @Override
            public void onFinish(String response) {
                jsonQuestions(response, true);
            }

            @Override
            public void onError(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUrl.showError(error);
                    }
                });
            }
        });
    }
}



