package com.leaf.gankio.entity;

import cn.bmob.v3.BmobObject;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/17 13:20
 * @TODO ： 收藏的干货
 */

public class GankBmob extends BmobObject {

    private String desc;
    private String type;
    private String url;
    private String uid;
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


    @Override
    public String toString() {
        return "GankBmob{" +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }

    public GankBmob(String desc, String type, String url, String uid) {
        this.desc = desc;
        this.type = type;
        this.url = url;
        this.uid = uid;
    }

    public GankBmob() {
    }
}
