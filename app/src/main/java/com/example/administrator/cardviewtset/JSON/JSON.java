package com.example.administrator.cardviewtset.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析从指定网址获取的JSON数据，提取出图片的地址，保存到List_img中
 * Created by Administrator on 2017/3/9 0009.
 */

public class JSON {
    private int total;//数据总数
    private List<String> list_img; //图片地址list
    private List<String> list_title; //新闻标题list
    private List<String> list_description; //新闻摘要list
    private List<Long> list_ID; //热词ID list

    /**
     * @param jsonData 需要解析的Json数据
     */
    public void withJSONObject(String jsonData) {
        try {
            list_img = new ArrayList<>();
            list_title = new ArrayList<>();
            list_description = new ArrayList<>();
            list_ID = new ArrayList<>();
            JSONObject jsonObj = new JSONObject(jsonData); //大括号用JSONObject
            total = jsonObj.getInt("total");
            JSONArray jsonArray = jsonObj.getJSONArray("tngou");//中括号JSONArray
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject_tngou = jsonArray.getJSONObject(i);
                list_title.add(jsonObject_tngou.getString("title"));
                list_img.add(jsonObject_tngou.getString("img"));
                list_description.add(jsonObject_tngou.getString("description"));
                list_ID.add(jsonObject_tngou.getLong("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getList_title() {
        return list_title;
    }

    public List<String> getImg_list() {
        return list_img;
    }

    public int getTotal() {
        return total;
    }

    public List<String> getList_description() {
        return list_description;
    }

    public List<Long> getList_ID() {
        return list_ID;
    }
}


