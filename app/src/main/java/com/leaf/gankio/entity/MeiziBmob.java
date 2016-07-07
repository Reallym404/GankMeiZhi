package com.leaf.gankio.entity;

import cn.bmob.v3.BmobObject;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/13 17:56
 * @TODO ： 喜欢的妹纸
 */

public class MeiziBmob extends BmobObject {

    private String publishedAt;
    private String desc;
    private String type;
    private String url;
    private String uid;

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public MeiziBmob(String publishedAt, String desc, String type, String url, String uid) {
        this.publishedAt = publishedAt;
        this.desc = desc;
        this.type = type;
        this.url = url;
        this.uid = uid;
    }

    public MeiziBmob() {
    }
}
