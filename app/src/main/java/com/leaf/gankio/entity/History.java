package com.leaf.gankio.entity;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/15 13:07
 * @TODO ： 历史
 */

public class History {

    private String title ;
    private String date ;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "History{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
