package com.example.cynthia.bihu.Adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cynthia.bihu.Config;
import com.example.cynthia.bihu.Data.Answer;
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

public class AnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Answer> mAnswerList;
    private String token;
    private int qid;
    private int userId;
    private int qAuthorId;
    private final static int ITEM_ANSWER = 0;
    private final static int ITEM_END = 1;

    private final static int ITEM_WAIT_FOR_LOAD = 0;
    private final static int ITEM_LOADING = 1;
    private final static int ITEM_FINISH =2;

    private int load_more_status = 2;

    public AnswerAdapter(ArrayList<Answer> answers, String token, int qId,
                         int qAuthorId,int userId){
        this.mAnswerList = answers;
        this.token = token;
        this.qid = qId;
        this.userId = userId;
        this.qAuthorId = qAuthorId;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount()-1 ? ITEM_END : ITEM_ANSWER ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_ANSWER){
            View itemView = layoutInflater.inflate(R.layout.answer, parent, false);
            final AnswerViewHolder holder = new AnswerViewHolder(itemView);
            if (userId == qAuthorId ){
                holder.best.setVisibility(View.VISIBLE);
            }else {
                holder.best.setVisibility(View.GONE);
            }
            holder.exciting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    clickExciting(position);
                    if(mAnswerList.get(position).isIs_exciting()){
                        holder.exciting.setImageResource(R.drawable.ic_good);
                        holder.excitingNum.setText("("+(mAnswerList.get(position).
                                getExciting()-1)+")");
                        mAnswerList.get(position).setIs_exciting(false);
                        mAnswerList.get(position).setExciting(mAnswerList.get(position).
                                getExciting()-1);
                    }else {
                        holder.exciting.setImageResource(R.drawable.ic_good_selected);
                        holder.excitingNum.setText("("+(mAnswerList.get(position).
                                getExciting()+1)+")");
                        mAnswerList.get(position).setIs_exciting(true);
                        mAnswerList.get(position).setExciting(mAnswerList.get(position).
                                getExciting()+1);
                    }
                }
            });
            holder.naive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    clickNaive(position);
                    if(mAnswerList.get(position).isIs_naive()){
                        holder.naive.setImageResource(R.drawable.ic_bad);
                        holder.naiveNum.setText("("+(mAnswerList.get(position).
                                getNaive()-1)+")");
                        mAnswerList.get(position).setIs_naive(false);
                        mAnswerList.get(position).setNaive(mAnswerList.get(position).
                                getNaive()-1);
                    }else {
                        holder.naive.setImageResource(R.drawable.ic_bad_selected);
                        holder.naiveNum.setText("("+(mAnswerList.get(position).
                                getNaive()+1)+")");
                        mAnswerList.get(position).setIs_naive(true);
                        mAnswerList.get(position).setNaive(mAnswerList.get(position).
                                getNaive()+1);
                    }
                }
            });
            holder.best.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    clickBest(position);
                    if(mAnswerList.get(position).getBest() == 0){
                        holder.best.setImageResource(R.drawable.ic_best_selected);
                        mAnswerList.get(position).setBest(1);
                    }
                }
            });
            return holder;
        }
        else {
            View itemView = layoutInflater.inflate(R.layout.no_more, parent, false);
            return new LoadViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AnswerViewHolder){
            final AnswerViewHolder holder1 = (AnswerViewHolder) holder;
        int naiveImage = mAnswerList.get(position).isIs_naive()?
                (R.drawable.ic_bad_selected):(R.drawable.ic_bad);
        int excitingImage = mAnswerList.get(position).isIs_exciting()?
                (R.drawable.ic_good_selected):(R.drawable.ic_good);
        int bestImage = mAnswerList.get(position).getBest() == 1?
                (R.drawable.ic_best_selected):(R.drawable.ic_best);

        int excitingNum = mAnswerList.get(position).getExciting();
        int naiveNum = mAnswerList.get(position).getNaive();
        holder1.content.setText(mAnswerList.get(position).getContent());

        String time = DateUrl.getDate(mAnswerList.get(position).getDate());
        holder1.date.setText(time);
        holder1.authorName.setText(mAnswerList.get(position).getAuthorName());
        holder1.excitingNum.setText("("+excitingNum+")");
        holder1.naiveNum.setText("("+naiveNum+")");
        holder1.naive.setImageResource(naiveImage);
        holder1.exciting.setImageResource(excitingImage);
        holder1.best.setImageResource(bestImage);
        if (!mAnswerList.get(position).getAuthorAvatar().equals("null")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = BitmapUrl.getBitmap(mAnswerList.get(position).getAuthorAvatar());
                    if (bitmap != null){
                        holder1.authorAvatar.post(new Runnable() {
                            @Override
                            public void run() {
                                holder1.authorAvatar.setImageBitmap(bitmap);
                            }
                        });
                    }else{
                        holder1.authorAvatar.post(new Runnable() {
                            @Override
                            public void run() {
                                holder1.authorAvatar.setImageResource(R.drawable.ic_head);
                                if (qAuthorId != mAnswerList.get(position).getAuthorId())
                                ToastUrl.showError("头像资源加载失败...");
                                else {

                                }
                            }
                        });
                        }
                }
            }).start();
        }else{
            holder1.authorAvatar.setImageResource(R.drawable.ic_head);
        }
        }else{
            LoadViewHolder footer = (LoadViewHolder) holder;
            footer.progressBar.setVisibility(View.GONE);
            footer.toast.setText("没有更多了 :)");
        }
    }

    @Override
    public int getItemCount() {
        return mAnswerList.size()+1;
    }

    class AnswerViewHolder extends RecyclerView.ViewHolder {

        TextView authorName;
        TextView content;
        TextView date;
        TextView excitingNum;
        TextView naiveNum;
        ImageView naive;
        ImageView exciting;
        ImageView best;
        CircleImageView authorAvatar;


        public AnswerViewHolder(View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.answer_id);
            content = itemView.findViewById(R.id.answer_comment);
            date = itemView.findViewById(R.id.answer_date);
            excitingNum = itemView.findViewById(R.id.answer_exciting_num);
            naiveNum = itemView.findViewById(R.id.answer_naive_num);
            naive = itemView.findViewById(R.id.answer_naive);
            exciting = itemView.findViewById(R.id.answer_exciting);
            best = itemView.findViewById(R.id.answer_best);
            authorAvatar = itemView.findViewById(R.id.answer_avatar);

        }
    }

    class LoadViewHolder extends RecyclerView.ViewHolder{

        TextView toast;
        ProgressBar progressBar;

        public LoadViewHolder(View itemView) {
            super(itemView);
            toast = itemView.findViewById(R.id.end_toast);
            progressBar = itemView.findViewById(R.id.end_progress);
        }
    }

    private void clickNaive(int position){
        String param = "id="+mAnswerList.get(position).getId()+
                "&type=2&token="+token;
        if(mAnswerList.get(position).isIs_naive()){
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
        String param = "id="+mAnswerList.get(position).getId()+
                "&type=2&token="+token;
        if(mAnswerList.get(position).isIs_exciting()){
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

    private void clickBest(int position){
        String param = "qid="+qid+"&aid="+mAnswerList.get(position).getId()+
                "&token="+token;
        if(mAnswerList.get(position).getBest() == 0){
            HttpUrl.sendHttpRequest(Config.ACCEPT, param, new HttpUrl.Callback() {
                @Override
                public void onFinish(String response) {
                    Log.d("采纳","succeed");

                }

                @Override
                public void onError(String error) {
                    Log.d("采纳",error);

                }
            });
        }else{
            ToastUrl.showResponse("该回答已采纳");
        }
    }

}
