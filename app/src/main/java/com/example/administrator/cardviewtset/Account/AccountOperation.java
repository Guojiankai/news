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
import android.widget.Toast;

import com.example.administrator.cardviewtset.GetContext.MyApplication;
import com.example.administrator.cardviewtset.JSON.HttpCallbackListener;
import com.example.administrator.cardviewtset.JSON.HttpUtil;
import com.example.administrator.cardviewtset.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 账号注册
 * Created by Administrator on 2017/3/21 0021.
 */
public class AccountOperation {
    public static final String CLIENT_ID = "9164530";//api平台获取到的OAuth2客户ID
    public static final String CLIENT_SECRET = "027ece210c6c5e24ffe9f70f312435f0";//api平台获取到的安全密文
    private String registeredAccountPATH = "http://www.tngou.net/api/oauth2/reg?";//账号注册api地址
    private String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";//邮箱格式验证正则表达式
    private AlertDialog.Builder alertDialog;//注册对话框
    private AlertDialog aDialog;//注册对话框
    private Context context;//上下文对象
    private AlertDialog.Builder builderDialog;//注册成功失败提示框
    private boolean flag_dialog = false;//注册成功的时候用来关闭注册对话框
    private EditText email_text;//邮箱
    private EditText account_text;//账号
    private EditText password1_text;//密码
    private EditText password2_text;//密码
    private StringBuilder builder;//账号申请链接

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
                if (status.equals("false")) { //注册失败
                    msg1 = bundle.getString("msg");
                    builderDialog.setMessage(msg1);
                    builderDialog.show();
                    userLoginCallback.loginFailed(msg1);
                    flag_dialog = false;
                } else {//注册成功
                    access_token = bundle.getString("access_token");
                    refresh_token = bundle.getString("refresh_token");
                    userLoginCallback.LoginSuccessful(access_token, refresh_token);
                    builderDialog.setMessage("注册成功");
                    flag_dialog = true;
                    builderDialog.show();
                }
            }
        }
    };

    public AccountOperation(Context context,UserLoginCallback userLoginCallback) {
        this.context = context;
        this.userLoginCallback = userLoginCallback;
        registeredAccount();
    }

    /**
     * 弹出注册账号对话框 注册账号
     *
     * @return 账号注册请求连接
     */
    public void registeredAccount() {
        alertDialog = new AlertDialog.Builder(context);
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
        View view = LayoutInflater.from(context).inflate(R.layout.registered_account, null);

        email_text = (EditText) view.findViewById(R.id.email_input_text);//邮箱
        email_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    //失去焦点处理
                    if (!(email_text.getText().toString().matches(regex))) {
                        email_text.setError("邮箱格式错误");
                    }
                }
            }
        });
        account_text = (EditText) view.findViewById(R.id.account_input_text);//账号
        account_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (account_text.getText().toString().length() < 6) {
                        account_text.setError("账号为空或者小于6位");
                    }
                }
            }
        });
        password1_text = (EditText) view.findViewById(R.id.password_input_text);//密码
        password1_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (password1_text.getText().toString().length() < 6) {
                        password1_text.setError("密码太简单，请设置不小于6位密码");
                    }
                }
            }
        });
        password2_text = (EditText) view.findViewById(R.id.password_input_text_2);//确认密码
        Button button1 = (Button) view.findViewById(R.id.registered_dialog_button_NO);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aDialog.dismiss();
            }
        });
        Button button2 = (Button) view.findViewById(R.id.registered_dialog_button_OK);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_str = email_text.getText().toString();
                String account_str = account_text.getText().toString();
                String password1_str = password1_text.getText().toString();
                String password2_str = password2_text.getText().toString();
                if (!(((password1_str == password2_str) | password1_str.equals(password2_str)))) {
                    Toast.makeText(MyApplication.getContext(), "两次密码输入不一致", Toast.LENGTH_LONG).show();
                } else if ((email_text.getText().toString().matches(regex)) && //邮箱格式正确
                        (account_text.getText().toString().length() >= 6) && //账号大于6位
                        (password1_text.getText().toString().length() >= 6)) //密码大于6位
                {
                    builder = new StringBuilder();
                    builder.append(registeredAccountPATH).append("client_id=").append(CLIENT_ID).append("&")
                            .append("client_secret=").append(CLIENT_SECRET).append("&")
                            .append("email=").append(email_str).append("&")
                            .append("account=").append(account_str).append("&")
                            .append("password=").append(password1_str);
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
                                if (status.equals("false")) { //返回 false  注册失败，将反馈信息发送给UI线程
                                    msg = jsonObject.getString("msg");
                                    bundle.putString("msg", msg);
                                } else {//返回true 注册成功，将访问令牌和刷新令牌反馈给UI线程
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
                } else {
                    Toast.makeText(MyApplication.getContext(), "资料填写不完整或者不正确，请重新检查！", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.setView(view);
        alertDialog.setTitle("注册账号");
        alertDialog.setCancelable(false);
        aDialog = alertDialog.create();
        aDialog.show();
    }
}
