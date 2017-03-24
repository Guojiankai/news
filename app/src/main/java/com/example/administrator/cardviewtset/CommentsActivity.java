package com.example.administrator.cardviewtset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.cardviewtset.Adapter.CommentsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 评论页
 */
public class CommentsActivity extends AppCompatActivity {
    private Long cx_id;//热词ID 用来获取新闻内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Intent intent = getIntent();
        cx_id = intent.getLongExtra(ItemActivity.PATH_ID,0);

        RecyclerView recyclerView_comments = (RecyclerView) findViewById(R.id.comments_recycler);//评论列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        CommentsAdapter adapter = new CommentsAdapter(init_list());
        recyclerView_comments.setLayoutManager(linearLayoutManager);
        recyclerView_comments.setAdapter(adapter);
    }

    private List<String> init_list() { //模仿评论 实际应该获取网络数据
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            StringBuilder builder = new StringBuilder();
            int j = random.nextInt(10);
            while (j >= 0) {

                j--;
                builder.append("哇，好想去哦！有没有人一起？");
            }
            list.add(builder.toString());
        }
        return list;
    }
}

