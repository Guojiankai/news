package com.example.administrator.cardviewtset.JSON;
/*
 * 用于加载完成网络数据后接口回调用
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}

