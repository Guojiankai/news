package com.example.administrator.cardviewtset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.administrator.cardviewtset.JSON.HttpCallbackListener;
import com.example.administrator.cardviewtset.JSON.HttpUtil;
import com.example.administrator.cardviewtset.JSON.JSON_show;
import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity {
    public static final String NAME = "name_item";
    public static final String ID = "name_image_id";
    public static final String DES = "description";
    public static final String PATH_ID = "cx_id";
    private TextView textView_cx;
    private  Long cx_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent intent = getIntent();
        String name_item = intent.getStringExtra(NAME);
        String id_item = intent.getStringExtra(ID);
        String cx_item = intent.getStringExtra(DES);
        cx_id = intent.getLongExtra(PATH_ID,0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.item_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView name_item_text = (TextView) findViewById(R.id.item_text_title);
        name_item_text.setText(name_item);
        ImageView imageView_item = (ImageView) findViewById(R.id.item_image_cx);
        if (("http://tnfs.tngou.net/image/top/default.jpg").equals(id_item)) {//更换服务器返回的默认图片
            Glide.with(this).load(MainActivity.get_Image_ID()).into(imageView_item);
        } else {
            Glide.with(this).load(id_item).into(imageView_item);
        }
        TextView nr_item_text = (TextView) findViewById(R.id.item_text_pse);
        nr_item_text.setText(cx_item);
        textView_cx = (TextView) findViewById(R.id.item_text_cx);
        HttpUtil.sendHttpRequest("http://www.tngou.net/api/top/show?id=" + cx_id, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                JSON_show json_show = new JSON_show();
                json_show.withJSONObject(response);
                final String cx_str = json_show.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_cx.setText(cx_str);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(ItemActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });

//        RecyclerView recyclerView_comments = (RecyclerView) findViewById(R.id.recycler_comments_view);//评论列表
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        CommentsAdapter adapter = new CommentsAdapter(init_list());
//        recyclerView_comments.setLayoutManager(linearLayoutManager);
//        recyclerView_comments.setAdapter(adapter);
    }

    private List<String> init_list() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("123");
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buttonOnClick(View view){
        switch (view.getId()){
            case R.id.item_text_comments_button1:{ //获取更多热评
                Toast.makeText(this, "获取更多热评", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.item_text_comments_button2:{//获取更多热评
                Toast.makeText(this, "获取更多热评", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.item_text_comments_praise:{//点赞数量+1   每个用户对每条新闻只能点赞一次
                Toast.makeText(this, "点赞数量+1   每个用户对每条新闻只能点赞一次", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.item_text_comments_button_dialog:{//弹出对话框填写评论，可以上传图片
                Toast.makeText(this, "弹出对话框填写评论", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
