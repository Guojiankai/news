package com.example.administrator.cardviewtset.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.cardviewtset.R;

public class UserInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        WebView webView = (WebView)findViewById(R.id.user_information);
        webView.getSettings().setJavaScriptEnabled(true);//设置WebView支持JavaScript 脚本
        webView.setWebViewClient( new WebViewClient());//设置 当网页要打开一个新的连接的时候不去打开浏览器，从改webView中显示
        webView.loadUrl(path);
    }
}
