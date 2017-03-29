package com.example.administrator.cardviewtset;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.cardviewtset.Adapter.CommentsAdapter;
import com.example.administrator.cardviewtset.JSON.HttpCallbackListener;
import com.example.administrator.cardviewtset.JSON.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论页
 */
public class CommentsActivity extends AppCompatActivity {
    private Long cx_id;//热词ID 用来获取新闻内容
    private List<Map<String, String>> list;
    private RecyclerView recyclerView_comments;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentsActivity.this);
                CommentsAdapter adapter = new CommentsAdapter(list);
                recyclerView_comments.setLayoutManager(linearLayoutManager);
                recyclerView_comments.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Intent intent = getIntent();
        cx_id = intent.getLongExtra(ItemActivity.PATH_ID, 0);
        recyclerView_comments = (RecyclerView) findViewById(R.id.comments_recycler);//评论列表
        getComments("http://www.tngou.net/api/memo/comment?id="+cx_id+"&type=top");
//        Toast.makeText(this, "http://www.tngou.net/api/memo/comment?id=" + cx_id + "&type=top", Toast.LENGTH_SHORT).show();
    }


    private void getComments(String path) { //获取全部评论数据
        HttpUtil.sendHttpRequest(path, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("tngou");
                    int j = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Map<String, String> map = new HashMap<String, String>();
                        //JSONObject getString()方法和optString()方法，如果key对应的值为null,getString方法就会异常
                        String account = jsonObject1.optString("account");//评论的ID
                        String memo = jsonObject1.optString("memo");//评论的内容
                        String avatar = jsonObject1.optString("avatar");//头像地址
                        map.put("account", account);
                        map.put("memo", memo);
                        map.put("avatar", avatar);
                        list.add(map);
                    }
                    handler.sendEmptyMessage(2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Exception e) {

            }
        });
    }
}
