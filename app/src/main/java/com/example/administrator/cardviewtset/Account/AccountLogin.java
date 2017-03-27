package com.example.administrator.cardviewtset.Account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.cardviewtset.JSON.HttpCallbackListener;
import com.example.administrator.cardviewtset.JSON.HttpUtil;
import com.example.administrator.cardviewtset.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 账号登录对话框
 * Created by Administrator on 2017/3/24 0024.
 */

public class AccountLogin {
    private Context context;//上下文对象
    private EditText account_text;//账号
    private EditText password_text;//密码
    private AlertDialog.Builder alertDialog;//登录对话框
    private AlertDialog aDialog;//登录对话框
    private AlertDialog.Builder builderDialog;//登录信息提示框
    private String PATH = "http://www.tngou.net/api/oauth2/login?";//开放平台api连接
    private boolean flag_dialog = false;//注册成功的时候用来关闭注册对话框
    private String access_token = null;//访问令牌
    private String refresh_token = null;//刷新令牌
    private UserLoginCallback userLoginCallback;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Bundle bundle = msg.getData();
                String msg1;
                String status;
                status = bundle.getString("status");
                if (status.equals("false")) { //登录失败
                    msg1 = bundle.getString("msg");
                    builderDialog.setMessage(msg1);
                    builderDialog.show();
                    userLoginCallback.loginFailed(msg1);
                    flag_dialog = false;
                } else {//登录成功
                    access_token = bundle.getString("access_token");
                    refresh_token = bundle.getString("refresh_token");
                    builderDialog.setMessage("登录成功");
                    flag_dialog = true;
                    userLoginCallback.LoginSuccessful(access_token, refresh_token);
                    builderDialog.show();
                }
            }
        }
    };

    public AccountLogin(Context context, UserLoginCallback userLoginCallback) {
        this.context = context;
        this.userLoginCallback = userLoginCallback;
        userLogin();
    }

    /**
     * 弹出用户登录对话框 登录个人账户
     *
     * @return 返回true登录成功
     */
    public void userLogin() {
        View view = LayoutInflater.from(context).inflate(R.layout.account_login, null);
        account_text = (EditText) view.findViewById(R.id.login_account_input_text);//账号
        password_text = (EditText) view.findViewById(R.id.login_password_input_text);//密码
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(view);
        alertDialog.setTitle("账号登录");
        alertDialog.setCancelable(false);
        aDialog = alertDialog.create();
        builderDialog = new AlertDialog.Builder(context);
        builderDialog.setTitle("提示");
        builderDialog.setCancelable(false);
        builderDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (flag_dialog) {
                    aDialog.dismiss();
                    flag_dialog = false;
                }
            }
        });
        Button button2 = (Button) view.findViewById(R.id.login_dialog_button_OK);//登录按钮
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder builder = new StringBuilder();
                builder.append(PATH).append("client_id=").append(AccountOperation.CLIENT_ID).append("&")
                        .append("client_secret=").append(AccountOperation.CLIENT_SECRET).append("&")
                        .append("name=").append(account_text.getText().toString()).append("&")
                        .append("password=").append(password_text.getText().toString());
                HttpUtil.sendHttpRequest(builder.toString(), new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        String access_token;
                        String refresh_token;
                        String status;
                        String msg;
                        Bundle bundle = new Bundle();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getString("status");//返回状态是否成功
                            bundle.putString("status", status);
                            if (status.equals("false")) { //返回 false  登录失败，将反馈信息发送给UI线程
                                msg = jsonObject.getString("msg");
                                bundle.putString("msg", msg);
                            } else {//返回true 登录成功，将访问令牌和刷新令牌反馈给UI线程
                                access_token = jsonObject.getString("access_token");//访问令牌
                                refresh_token = jsonObject.getString("refresh_token");//刷新令牌
                                bundle.putString("access_token", access_token);
                                bundle.putString("refresh_token", refresh_token);
                            }
                            //Message可以通过new Message构造来创建一个新的Message,但是这种方式很不好，不建议使用。最好使用Message.obtain()来获取Message实例,它创建了消息池来处理的
                            Message message = Message.obtain();
                            message.what = 1;
                            message.setData(bundle);
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });
        Button button1 = (Button) view.findViewById(R.id.login_dialog_button_NO);//取消按钮
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aDialog.dismiss();
            }
        });
        aDialog.show();
    }
}
