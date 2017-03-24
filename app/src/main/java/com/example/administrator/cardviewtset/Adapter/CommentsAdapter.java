package com.example.administrator.cardviewtset.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.cardviewtset.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻评论列表适配器
 * Created by Administrator on 2017/3/16 0016.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private List<String> list = new ArrayList<>();
    public CommentsAdapter(List<String> list){
        this.list = list;
    }
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_recycler,parent,false);
        CommentsAdapter.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentsAdapter.ViewHolder holder, int position) {
        holder.content_text.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content_text;
        public ViewHolder(View itemView) {
            super(itemView);
            content_text = (TextView)itemView.findViewById(R.id.comments_yh_content);
        }
    }
}
