package com.example.administrator.cardviewtset;

/**
 * Created by Administrator on 2017/3/3 0003.
 */

public class RecyclerItem {
    private String name;
    private String image_ID;
    private String description;
    private Long PATH_ID;

    public RecyclerItem(String name, String image_ID, String description, Long id) {
        this.name = name;
        this.image_ID = "http://tnfs.tngou.net/image" + image_ID; //图片获取地址，通过id加默认地址
        this.description = description;
        PATH_ID = id; //新闻详情获取地址 通过热词ID加默认地址
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
