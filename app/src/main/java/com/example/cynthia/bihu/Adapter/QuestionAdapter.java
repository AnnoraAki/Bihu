package com.example.cynthia.bihu.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.cynthia.bihu.Activity.AnswerQuestionActivity;
import com.example.cynthia.bihu.Config;
import com.example.cynthia.bihu.Data.Question;
import com.example.cynthia.bihu.R;
import com.example.cynthia.bihu.Tools.BitmapUrl;
import com.example.cynthia.bihu.Tools.CircleImageView;
import com.example.cynthia.bihu.Tools.DateUrl;
import com.example.cynthia.bihu.Tools.HttpUrl;
import com.example.cynthia.bihu.Tools.ToastUrl;

import java.util.ArrayList;

/**
 * Created by Cynthia on 2018/2/11.
 */

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private ArrayList<Question> mQuestionList;
    private String userToken;
    private final static int ITEM_QUESTION = 0;
    private final static int ITEM_END = 1;

    private final static int ITEM_WAIT_FOR_LOAD = 0;
    private final static int ITEM_LOADING = 1;
    private final static int ITEM_FINISH =2;

    private int load_more_status = 0;

    public QuestionAdapter(ArrayList<Question> questions,String token){
        this.mQuestionList = questions;
        this.userToken = token;

    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount()-1 ? ITEM_END : ITEM_QUESTION ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_QUESTION) {
            View itemView = layoutInflater.inflate(R.layout.question, parent, false);
            final QuestionViewHolder holder = new QuestionViewHolder(itemView);
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),AnswerQuestionActivity.class);
                    int position = holder.getAdapterPosition();
                    Question question = mQuestionList.get(position);
                    intent.putExtra("qId",question.getId());
                    intent.putExtra("title",question.getTitle());
                    intent.putExtra("authorName",question.getAuthorName());
                    intent.putExtra("authorId",question.getAuthorId());
                    intent.putExtra("content",question.getContent());
                    intent.putExtra("naive",question.isIs_naive());
                    intent.putExtra("exciting",question.isIs_exciting());
                    intent.putExtra("naiveNum",question.getNaive());
                    intent.putExtra("excitingNum",question.getExciting());
                    intent.putExtra("favorite",question.isIs_favorite());
                    intent.putExtra("authorAvatar",question.getAuthorAvatar());
                    intent.putExtra("answerCount",question.getAnswerCount());
                    intent.putExtra("date",question.getDate());
                    intent.putExtra("recent",question.getRecent());
                    intent.putExtra("image", question.getImages());
                    v.getContext().startActivity(intent);
                }
            });
            holder.naive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    clickNaive(position);
                    if(mQuestionList.get(position).isIs_naive()){
                        holder.naive.setImageResource(R.drawable.ic_bad);
                        holder.naiveNum.setText("("+(mQuestionList.get(position).
                                getNaive()-1)+")");
                        mQuestionList.get(position).setIs_naive(false);
                        mQuestionList.get(position).setNaive(mQuestionList.get(position).
                                getNaive()-1);
                    }else {
                        holder.naive.setImageResource(R.drawable.ic_bad_selected);
                        holder.naiveNum.setText("("+(mQuestionList.get(position).
                                getNaive()+1)+")");
                        mQuestionList.get(position).setIs_naive(true);
                        mQuestionList.get(position).setNaive(mQuestionList.get(position).
                                getNaive()+1);
                    }
                }
            });
            holder.exciting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    clickExciting(position);
                    if(mQuestionList.get(position).isIs_exciting()){
                        holder.exciting.setImageResource(R.drawable.ic_good);
                        holder.excitingNum.setText("("+(mQuestionList.get(position).
                                getExciting()-1)+")");
                        mQuestionList.get(position).setIs_exciting(false);
                        mQuestionList.get(position).setExciting(mQuestionList.get(position).
                                getExciting()-1);
                    }else {
                        holder.exciting.setImageResource(R.drawable.ic_good_selected);
                        holder.excitingNum.setText("("+(mQuestionList.get(position).
                                getExciting()+1)+")");
                        mQuestionList.get(position).setIs_exciting(true);
                        mQuestionList.get(position).setExciting(mQuestionList.get(position).
                                getExciting()+1);
                    }
                }
            });
            holder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    clickFavorite(position);
                    if(mQuestionList.get(position).isIs_favorite()){
                        holder.favorite.setImageResource(R.drawable.ic_favorite1);
                        mQuestionList.get(position).setIs_favorite(false);
                    }else {
                        holder.favorite.setImageResource(R.drawable.ic_favorite1_selected);
                        mQuestionList.get(position).setIs_favorite(true);
                    }
                }
            });
            return holder;
        }else if(viewType == ITEM_END){
            View itemView = layoutInflater.inflate(R.layout.no_more, parent, false);
            return new LoadViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof QuestionViewHolder) {
            final QuestionViewHolder holder1 = (QuestionViewHolder) holder;
            int naiveImage = mQuestionList.get(position).isIs_naive() ?
                    (R.drawable.ic_bad_selected) : (R.drawable.ic_bad);
            int excitingImage = mQuestionList.get(position).isIs_exciting() ?
                    (R.drawable.ic_good_selected) : (R.drawable.ic_good);
            int favoriteImage = mQuestionList.get(position).isIs_favorite() ?
                    (R.drawable.ic_favorite1_selected) : (R.drawable.ic_favorite1);

            int AnswerCount = mQuestionList.get(position).getAnswerCount();
            int excitingNum = mQuestionList.get(position).getExciting();
            int naiveNum = mQuestionList.get(position).getNaive();
            holder1.title.setText(mQuestionList.get(position).getTitle());
            holder1.context.setText(mQuestionList.get(position).getContent());
            if (mQuestionList.get(position).getRecent().equals("null")) {
                String time = DateUrl.getDate(mQuestionList.get(position).getDate());
                holder1.date.setText(time + "发布");
            } else {
                String time = DateUrl.getDate(mQuestionList.get(position).getRecent());
                holder1.date.setText(time + "更新");
            }
            holder1.userId.setText(mQuestionList.get(position).getAuthorName());
            holder1.answerCount.setText("共" + AnswerCount + "条评论");
            holder1.excitingNum.setText("(" + excitingNum + ")");
            holder1.naiveNum.setText("(" + naiveNum + ")");
            holder1.naive.setImageResource(naiveImage);
            holder1.exciting.setImageResource(excitingImage);
            holder1.favorite.setImageResource(favoriteImage);
            if (!mQuestionList.get(position).getAuthorAvatar().equals("null")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = BitmapUrl.getBitmap(mQuestionList.get(position).getAuthorAvatar());
                        if(bitmap != null){
                            holder1.userAvatar.post(new Runnable() {
                                @Override
                                public void run() {
                                    holder1.userAvatar.setImageBitmap(bitmap);
                                }
                            });
                        }else {
                            holder1.userAvatar.post(new Runnable() {
                                @Override
                                public void run() {
                                    holder1.userAvatar.setImageResource(R.drawable.ic_head);
                                    ToastUrl.showError("头像资源加载失败...");
                                }
                            });
                        }

                    }
                }).start();
            }else{
                holder1.userAvatar.setImageResource(R.drawable.ic_head);
            }
            if (!mQuestionList.get(position).getImages().equals("null")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = BitmapUrl.getBitmap(mQuestionList.get(position).getImages());
                        if(bitmap != null){
                            holder1.image.post(new Runnable() {
                                @Override
                                public void run() {
                                    holder1.image.setImageBitmap(bitmap);
                                }
                            });
                        }else {
                            holder1.image.post(new Runnable() {
                                @Override
                                public void run() {
                                    holder1.image.setVisibility(View.GONE);
                                    ToastUrl.showError("图片资源加载失败...");
                                }
                            });
                        }

                    }
                }).start();
            }else {
                holder1.image.setVisibility(View.GONE);
            }
        }else{
            LoadViewHolder footer = (LoadViewHolder) holder;
            switch(load_more_status){
                case ITEM_WAIT_FOR_LOAD:
                    footer.progressBar.setVisibility(View.GONE);
                    footer.toast.setText("上拉加载更多 :)");
                    break;
                case ITEM_LOADING:
                    footer.progressBar.setVisibility(View.VISIBLE);
                    footer.toast.setText("正在加载...");
                    break;
                case ITEM_FINISH:
                    footer.progressBar.setVisibility(View.GONE);
                    footer.toast.setText("没有更多了 :)");
            }
        }
    }


    @Override
    public int getItemCount() {
        return mQuestionList.size()+1;
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView userId;
        TextView context;
        TextView excitingNum;
        TextView naiveNum;
        TextView more;
        TextView date;
        TextView answerCount;
        CircleImageView userAvatar;
        ImageView exciting;
        ImageView naive;
        ImageView favorite;
        ImageView image;

        QuestionViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.question_title);
            userId = itemView.findViewById(R.id.question_user_id);
            context = itemView.findViewById(R.id.question_context);
            excitingNum = itemView.findViewById(R.id.question_exciting_num);
            naiveNum = itemView.findViewById(R.id.question_naive_num);
            more = itemView.findViewById(R.id.question_more);
            userAvatar = itemView.findViewById(R.id.question_user_image);
            exciting = itemView.findViewById(R.id.question_exciting);
            naive = itemView.findViewById(R.id.question_naive);
            favorite = itemView.findViewById(R.id.question_favorite);
            date = itemView.findViewById(R.id.question_date);
            answerCount = itemView.findViewById(R.id.question_answer_num);
            image = itemView.findViewById(R.id.question_context_image);
        }
    }

    class LoadViewHolder extends RecyclerView.ViewHolder{

        TextView toast;
        ProgressBar progressBar;

        LoadViewHolder(View itemView) {
            super(itemView);
            toast = itemView.findViewById(R.id.end_toast);
            progressBar = itemView.findViewById(R.id.end_progress);
        }
    }

    private void clickNaive(int position){
        String param = "id="+mQuestionList.get(position).getId()+
                "&type=1&token="+userToken;
        if(mQuestionList.get(position).isIs_naive()){
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

    private void clickExciting(int position){
        String param = "id="+mQuestionList.get(position).getId()+
                "&type=1&token="+userToken;
        if(mQuestionList.get(position).isIs_exciting()){
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

    private void clickFavorite(int position){
        String param = "qid="+mQuestionList.get(position).getId()+
                "&token="+userToken;
        if(mQuestionList.get(position).isIs_favorite()){
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

    public void changeMoreStatus(int status){
        load_more_status=status;
        notifyDataSetChanged();
    }

}
