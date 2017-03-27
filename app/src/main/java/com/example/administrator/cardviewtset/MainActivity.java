package com.example.administrator.cardviewtset;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.cardviewtset.Account.AccountLogin;
import com.example.administrator.cardviewtset.Account.AccountOperation;
import com.example.administrator.cardviewtset.Account.UserLoginCallback;
import com.example.administrator.cardviewtset.Adapter.RecyclerAdapter;
import com.example.administrator.cardviewtset.JSON.HttpCallbackListener;
import com.example.administrator.cardviewtset.JSON.HttpUtil;
import com.example.administrator.cardviewtset.JSON.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 主页 app第一页
 */
public class MainActivity extends AppCompatActivity {

    private String PATH = "http://www.tngou.net/api/top/list";//热点词api，通过获取热点词汇的id获取热词详情
    private RecyclerView recyclerView;  //新闻列表
    private DrawerLayout drawerLayout;  //抽屉视图
    private NavigationView navigationView; //导航视图
    private SwipeRefreshLayout swipeRefreshLayout; //下拉刷新
    private FloatingActionButton actionButton; //悬浮按钮
    private RecyclerAdapter adapter; //新闻列表适配器
    private CircleImageView circleImageView;//头像
    private TextView individuality_text;//个性签名
    private List<RecyclerItem> list_items = new ArrayList<>(); //新闻列表子项

    private static int[] image_ID = {R.drawable.aa, R.drawable.bb, R.drawable.cc, R.drawable.dd, R.drawable.ee, R.drawable.ff, R.drawable.gg, R.drawable.hh, R.drawable.ii,
            R.drawable.jj, R.drawable.kk, R.drawable.ll, R.drawable.mm, R.drawable.nn, R.drawable.bb, R.drawable.cc, R.drawable.dd, R.drawable.ee, R.drawable.ff, R.drawable.gg};

    public static int get_Image_ID() {   //为没有图片的新闻随机一个图片
        Random random = new Random();
        return image_ID[random.nextInt(image_ID.length)];
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);       //网络数据获取后通过message发送给UI线程
            switch (msg.what) {
                case 1:
                    JSON json = new JSON();
                    json.withJSONObject(msg.getData().getString("JSON_data"));   //JSON数据解析
                    List<String> list_img = json.getImg_list(); // 新闻图片地址
                    List<String> list_title = json.getList_title(); //新闻标题
                    List<String> list_description = json.getList_description(); //新闻摘要
                    List<Long> list_ID = json.getList_ID(); //新闻详情地址
                    for (int i = 0; i < list_img.size(); i++) {
                        list_items.add(new RecyclerItem(list_title.get(i), list_img.get(i), list_description.get(i), list_ID.get(i)));
                    }
                    adapter.notifyDataSetChanged();  //通知适配器数据更新
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  //设置自定义tab
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示HomeAsUp键
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);  //设置HomeAsUp键图标
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_call); //设置导航视图默认选中项
        //导航视图子项事件监听
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_task://切换到账号注册
                        AccountOperation accountOperation = new AccountOperation(MainActivity.this, new UserLoginCallback() {
                            @Override
                            public void LoginSuccessful(String access_token, String refresh_token) { // access_token访问令牌 refresh_token 刷新令牌

                            }

                            @Override
                            public void loginFailed(String msg) { //服务端反馈的消息

                            }
                        });

                        break;
                    case R.id.nav_mail://切换到账号登录
                        AccountLogin accountLogin =new AccountLogin(MainActivity.this, new UserLoginCallback() {
                            @Override
                            public void LoginSuccessful(String access_token, String refresh_token) {

                            }

                            @Override
                            public void loginFailed(String msg) {


                            }
                        });
                        break;
                }
                return true;
            }
        });

//        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);//加载头部view
        View headerView = navigationView.getHeaderView(0);//获得头部View
        circleImageView = (CircleImageView)headerView.findViewById(R.id.head_portrait);
        individuality_text = (TextView)headerView.findViewById(R.id.individuality_signature);
        circleImageView.setImageResource(R.drawable.bb);
        individuality_text.setText("我在等待，一个有你的未来!");


        actionButton = (FloatingActionButton) findViewById(R.id.FAButton);//悬浮按钮设置及事件监听
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "是否关注Ta?", Snackbar.LENGTH_LONG).setAction("关注", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "已关注", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        data_init(); //新闻数据初始化（包括加载网络数据）
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(list_items);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);//recyclerView相关设置
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data_init();//下拉刷新数据
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void data_init() {
        HttpUtil.sendHttpRequest(PATH, new HttpCallbackListener() {//加载网络数据并将数据通过msg发送到UI线程
            @Override
            public void onFinish(final String response) {
                Bundle bundle = new Bundle();
                bundle.putString("JSON_data", response);
                Message msg = new Message();
                msg.setData(bundle);
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //菜单选项点击监听
        switch (item.getItemId()) {
            case R.id.ic_backup:
                Toast.makeText(this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.ic_comment:
                Toast.makeText(this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}
