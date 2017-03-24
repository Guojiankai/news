package com.example.administrator.cardviewtset.JSON;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 从指定地址获取网络数据
 */

public class

HttpUtil {
    /**
     *
     * @param address   获取数据的地址
     * @param listener  数据下载完成后的回调接口
     */
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {//readLine 读取一行
                        stringBuilder.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(stringBuilder.toString());//回调接口  处理下载的数据
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onError(e);//回调接口 处理异常
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
