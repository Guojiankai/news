package com.example.administrator.cardviewtset.Account;

/**
 * 登录或者注册操作后回调接口
 * Created by Administrator on 2017/3/24 0024.
 */

public interface UserLoginCallback {
    void LoginSuccessful(String access_token,String refresh_token);
    void loginFailed(String msg);
}
