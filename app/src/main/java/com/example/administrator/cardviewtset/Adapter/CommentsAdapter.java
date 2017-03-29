package com.example.administrator.cardviewtset.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.cardviewtset.GetContext.MyApplication;
import com.example.administrator.cardviewtset.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 新闻评论列表适配器
 * Created by Administrator on 2017/3/16 0016.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private List<Map<String, String>> list = new ArrayList<>();
    public CommentsAdapter(List<Map<String, String>> list){
        this.list = list;
    }
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_recycler,parent,false);
        CommentsAdapter.ViewHolder holder = new ViewHolder(view);
        return holder;
    }
//http://tnfs.tngou.net/img/avatar/170321/468912a393c2737159ebbb6f343c0dd7.jpg
    @Override
    public void onBindViewHolder(CommentsAdapter.ViewHolder holder, int position) {
        Map<String,String> map = new HashMap<>();
        map = list.get(position);
        holder.comments_yh_content.setText(map.get("memo"));
        Glide.with(MyApplication.getContext()).load("http://tnfs.tngou.net/img" + map.get("avatar")).into(holder.comments_yh_HeadPortrait);
        holder.comments_yh_id.setText(map.get("account"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView comments_yh_content;//评论内容
        private CircleImageView comments_yh_HeadPortrait;//头像
        private TextView comments_yh_id;//用户ID
        public ViewHolder(View itemView) {
            super(itemView);
            comments_yh_content = (TextView)itemView.findViewById(R.id.comments_yh_content);
            comments_yh_HeadPortrait = (CircleImageView)itemView.findViewById(R.id.comments_yh_HeadPortrait);
            comments_yh_id = (TextView)itemView.findViewById(R.id.comments_yh_id);

        }
    }
}
