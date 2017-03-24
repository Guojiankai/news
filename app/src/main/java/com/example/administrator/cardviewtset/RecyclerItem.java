package com.example.administrator.cardviewtset;

/**
 * 新闻类
 * Created by Administrator on 2017/3/3 0003.
 */

public class RecyclerItem {
    private String name; //新闻标题



    private String image_ID; //图片地址
    private String description; //新闻摘要
    private Long PATH_ID;//热词ID  用于获取内容

    public RecyclerItem(String name, String image_ID, String description, Long id) {
        this.name = name;
        this.image_ID = "http://tnfs.tngou.net/image" + image_ID; //图片获取地址，通过id加默认地址
        this.description = description;
        this.PATH_ID = id; //新闻详情获取地址 通过热词ID加默认地址
    }

    public String getImage_ID() {
        return image_ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getPATH_ID() {
        return PATH_ID;
    }

}
