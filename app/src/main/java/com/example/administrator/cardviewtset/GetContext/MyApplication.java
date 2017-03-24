package com.example.administrator.cardviewtset.GetContext;

import android.app.Application;
import android.content.Context;

/**
 * 获取全局context  通过修改启动时初始化的Application（清单文件中）改成从该类启动
 * Created by Administrator on 2017/3/21 0021.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}

