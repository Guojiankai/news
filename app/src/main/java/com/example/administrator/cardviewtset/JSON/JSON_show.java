package com.example.administrator.cardviewtset.JSON;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/15 0015.
 * 解析热词详情 将新闻内容的JSON数据解析出来
 */

public class JSON_show {
    private String message;
    public void withJSONObject(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            message = jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getMessage() {
//        return message.replace("<p>", "   ").replace("</p>", "").replace("&nbsp;", "").replace("<strong>","").replace("</strong>","");
        return  delHTMLTag(message);
    }

    /**
     * 去掉HTML标签
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {

        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

        htmlStr = htmlStr.replaceAll(regEx_html,"    ").replaceAll(regEx_style,"").replaceAll(regEx_script,"");
        return htmlStr.replace("&nbsp;", ""); // 返回文本字符串

    }
}
