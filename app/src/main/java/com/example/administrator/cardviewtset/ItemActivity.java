package com.example.administrator.cardviewtset;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.cardviewtset.GetContext.MyApplication;
import com.example.administrator.cardviewtset.JSON.HttpCallbackListener;
import com.example.administrator.cardviewtset.JSON.HttpUtil;
import com.example.administrator.cardviewtset.JSON.JSON_show;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 新闻内容页
 */
public class ItemActivity extends AppCompatActivity {
    public static final String NAME = "name_item";  //新闻标题
    public static final String ID = "name_image_id";//新闻图片地址
    public static final String DES = "description";//新闻摘要
    public static final String PATH_ID = "cx_id";//热词ID 用来获取新闻内容
    private final String def_image_id = "http://tnfs.tngou.net/image/top/default.jpg"; //默认图片地址
    private final String def_content_id = "http://www.tngou.net/api/top/show?id=";//读取新闻内容地址必须添加的前缀
    private TextView textView_cx;//新闻内容
    private Long cx_id;//热词ID 用来获取新闻内容
    private String name_item;//新闻标题
    private List<Map<String, String>> list;

    private TextView comments_yh_content_i;//评论内容
    private CircleImageView comments_yh_HeadPortrait_i;//头像
    private TextView comments_yh_id_i;//用户ID

        private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    comments_yh_content_i.setText(list.get(0).get("memo"));
                    Glide.with(MyApplication.getContext()).load("http://tnfs.tngou.net/img" + list.get(0).get("avatar")).into(comments_yh_HeadPortrait_i);
                    comments_yh_id_i.setText(list.get(0).get("account"));
                }
            }
        };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent intent = getIntent();
        name_item = intent.getStringExtra(NAME);
        String id_item = intent.getStringExtra(ID);
        String cx_item = intent.getStringExtra(DES);
        cx_id = intent.getLongExtra(PATH_ID, 0);  //获取传递过来的数据

        Toolbar toolbar = (Toolbar) findViewById(R.id.item_toolbar); //初始化toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView name_item_text = (TextView) findViewById(R.id.item_text_title);//新闻标题
        name_item_text.setText(name_item);
        ImageView imageView_item = (ImageView) findViewById(R.id.item_image_cx);//新闻图片
        if ((def_image_id).equals(id_item)) {//如果反馈的地址是服务器默认图片的地址，更换服务器返回的默认图片
            Glide.with(this).load(MainActivity.get_Image_ID()).into(imageView_item);
        } else {
            Glide.with(this).load(id_item).into(imageView_item);
        }
        TextView nr_item_text = (TextView) findViewById(R.id.item_text_pse);//新闻摘要
        nr_item_text.setText(cx_item);
        textView_cx = (TextView) findViewById(R.id.item_text_cx);

        //加载新闻内容
        HttpUtil.sendHttpRequest(def_content_id + cx_id, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                JSON_show json_show = new JSON_show(); //解析Json数据
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
                Log.d("ItemActivity", "请求数据失败");
            }
        });

        comments_yh_content_i = (TextView)findViewById(R.id.comments_yh_content);
        comments_yh_HeadPortrait_i = (CircleImageView)findViewById(R.id.comments_yh_HeadPortrait);
        comments_yh_id_i = (TextView)findViewById(R.id.comments_yh_id);

        HttpUtil.sendHttpRequest("http://www.tngou.net/api/memo/comment?id=" + cx_id + "&type=top", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("tngou");

                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Map<String, String> map = new HashMap<String, String>();
                    //JSONObject getString()方法和optString()方法，如果key对应的值为null,getString方法就会异常
                    map.put("account", jsonObject1.optString("account"));//评论的ID
                    map.put("memo", jsonObject1.optString("memo"));//评论的内容
                    map.put("avatar", jsonObject1.optString("avatar"));//头像地址
                    list.add(map);
                    handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
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

    public void buttonOnClick(View view) {
        switch (view.getId()) {
            case R.id.item_text_comments_button1: { //获取更多热评
                Intent intent = new Intent(ItemActivity.this, CommentsActivity.class);
                intent.putExtra(PATH_ID, cx_id);//新闻热词ID传递给评论界面去获取评论
                startActivity(intent);
                break;
            }
            case R.id.item_text_comments_button2: {//获取更多热评
                Intent intent = new Intent(ItemActivity.this, CommentsActivity.class);
                intent.putExtra(PATH_ID, cx_id);//新闻热词ID传递给评论界面去获取评论
                startActivity(intent);
                break;
            }
            case R.id.item_text_comments_praise: {//点赞数量+1   每个用户对每条新闻只能点赞一次
                Toast.makeText(this, "点赞数量+1   每个用户对每条新闻只能点赞一次", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.item_text_comments_button_dialog: {//弹出对话框填写评论，可以上传图片
                Toast.makeText(this, "弹出对话框填写评论", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
