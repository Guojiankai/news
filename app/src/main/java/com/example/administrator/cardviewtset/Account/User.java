package com.example.administrator.cardviewtset.Account;

import android.os.Handler;
import android.text.TextUtils;

import com.example.administrator.cardviewtset.JSON.HttpCallbackListener;
import com.example.administrator.cardviewtset.JSON.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class User {
    private String account;    //登录帐号
    private String email;    //邮箱
    private String password;    //密码


    private String domain;    //用户主页域名
    private String title;        //个性标题
    private String avatar;    //头像地址
    private String phone;    //联系电话 手机，电话等
    private int gender;    //性别，1：男 0：女
    private String province;    // 省份
    private String city;    //城市
    private String signature;   //个性签名
    private int integral;    //用户积分
    private int isemail;    //是否显示邮箱 1：显示 0：不显示
    private int isphone;    //是否显示联系电话 1：显示 0：不显示
    private int role;    //用户权限，0：普通用户，1：管理员；-1:未激活；-2：黑名单

    private final String path = "http://www.tngou.net/api/user?";//个人信息请求地址
    private final String path_yh = "http://www.tngou.net/my/";//用户主页域名前缀

    public User(String access_token, final String refresh_token, final Handler handler) {
        String path_str = null;
        if (!(TextUtils.isEmpty(access_token))) {//access_token为空则不执行  没有访问令牌
            path_str = path + "access_token=" + access_token;
            if (!TextUtils.isEmpty(refresh_token)) {
                path_str = path + "&refresh_token=" + refresh_token;
            }
            HttpUtil.sendHttpRequest(path_str, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    getData(response);
                    handler.sendEmptyMessage(2);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

    }

    private void getData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
//            account = jsonObject.getString("account");//账号
//            email = jsonObject.getString("email");//邮箱
//            password = jsonObject.getString("password");//密码
            domain = path_yh + jsonObject.getString("domain");//主页域名
//            title = jsonObject.getString("title");//个性标题
//            phone = jsonObject.getString("phone");//电话
            avatar = jsonObject.getString("avatar");//头像地址
//            gender = jsonObject.getInt("gender");//用户积分
//            province = jsonObject.getString("province");//省份
//            city = jsonObject.getString("city");//城市
            signature = jsonObject.getString("signature");//个性签名
//            integral = jsonObject.getInt("integral");//用户积分
//            isemail = jsonObject.getInt("isemail");//是否显示邮箱
//            isphone = jsonObject.getInt("isphone");//是否显示联系电话
//            role = jsonObject.getInt("role");//用户权限
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getSignature() {
        return signature;
    }

    public String getAvatar() {
        return avatar;
    }
    public String getDomain() {
        return domain;
    }

}
