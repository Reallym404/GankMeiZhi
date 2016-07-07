package com.leaf.gankio.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/2 17:07
 * @TODO ： 分类数据
 */

public class Classify {


    /**
     * error : false
     * results : [{"_id":"574f9c87421aa910abe2bfba","createdAt":"2016-06-02T10:40:07.670Z","desc":"BubbleView是带箭头的气泡控件/容器类","publishedAt":"2016-06-02T11:30:26.566Z","source":"chrome","type":"Android","url":"https://github.com/cpiz/BubbleView","used":true,"who":"大熊"},{"_id":"574e5f66421aa910b3910ada","createdAt":"2016-06-01T12:07:02.872Z","desc":"An Android app to demonstrate react-native-material-design","publishedAt":"2016-06-02T11:30:26.566Z","source":"chrome","type":"Android","url":"https://github.com/react-native-material-design/demo-app","used":true,"who":"wuzheng"},{"_id":"574e5941421aa910b7ff04fa","createdAt":"2016-06-01T11:40:49.727Z","desc":"一个集Gank.Io，Rxjava示例，操作符,MD控件使用,各种好玩Ap示例的学习App。","publishedAt":"2016-06-01T12:01:44.959Z","source":"web","type":"Android","url":"https://github.com/HotBitmapGG/StudyProject","used":true,"who":"HotbitmapGG"},{"_id":"574e5438421aa910b3910ad8","createdAt":"2016-06-01T11:19:20.520Z","desc":"JSON解析的成长史\u2014\u2014原来还可以这么简单","publishedAt":"2016-06-01T12:01:44.959Z","source":"web","type":"Android","url":"http://www.jianshu.com/p/cbc1aa0c7661","used":true,"who":"于连林"},{"_id":"574e52b1421aa910a742e78d","createdAt":"2016-06-01T11:12:49.773Z","desc":"BaseAdapter基本使用","publishedAt":"2016-06-01T12:01:44.959Z","source":"web","type":"Android","url":"http://mafei.site/2015/11/22/BaseAdapter%E4%BD%BF%E7%94%A8%E4%B9%8B%E9%80%97%E6%AF%94%E5%BC%8F%E3%80%81%E6%99%AE%E9%80%9A%E5%BC%8F%E5%92%8C%E6%96%87%E8%89%BA%E5%BC%8F/","used":true,"who":"马飞"},{"_id":"574e44d9421aa910b7ff04f2","createdAt":"2016-06-01T10:13:45.597Z","desc":"Android开发书籍推荐：从入门到精通系列学习路线书籍介绍","publishedAt":"2016-06-01T12:01:44.959Z","source":"web","type":"Android","url":"http://diycode.cc/wiki/androidbook","used":true,"who":null},{"_id":"574e39c6421aa910abe2bfa4","createdAt":"2016-06-01T09:26:30.688Z","desc":"基于第三方WheelView 实现的一个时间选择器","publishedAt":"2016-06-01T12:01:44.959Z","source":"web","type":"Android","url":"https://github.com/chsmy/DateSelecter","used":true,"who":"命运"},{"_id":"574e0f81421aa910b7ff04ec","createdAt":"2016-06-01T06:26:09.927Z","desc":"搜狐 优酷 爱奇艺 乐视 无广告 视频客户端 ","publishedAt":"2016-06-01T12:01:44.959Z","source":"chrome","type":"Android","url":"https://github.com/fire3/sailorcast","used":true,"who":" 大熊"},{"_id":"574e0e3c421aa910abe2bfa1","createdAt":"2016-06-01T06:20:44.545Z","desc":"Material版的显示提示信息的View","publishedAt":"2016-06-01T12:01:44.959Z","source":"chrome","type":"Android","url":"https://github.com/fcannizzaro/material-tip","used":true,"who":"大熊"},{"_id":"574d6a92421aa910abe2bf99","createdAt":"2016-05-31T18:42:26.172Z","desc":"EventBus3 事件管理插件","publishedAt":"2016-06-01T12:01:44.959Z","source":"chrome","type":"Android","url":"https://github.com/kgmyshin/eventbus3-intellij-plugin","used":true,"who":"蒋朋"}]
     */

    private boolean error;
    /**
     * _id : 574f9c87421aa910abe2bfba
     * createdAt : 2016-06-02T10:40:07.670Z
     * desc : BubbleView是带箭头的气泡控件/容器类
     * publishedAt : 2016-06-02T11:30:26.566Z
     * source : chrome
     * type : Android
     * url : https://github.com/cpiz/BubbleView
     * used : true
     * who : 大熊
     */

    private List<ClassifyResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ClassifyResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ClassifyResultsBean> results) {
        this.results = results;
    }

    public static class ClassifyResultsBean implements Serializable{
        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
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

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        @Override
        public String toString() {
            return "ResultsBean{" +
                    "_id='" + _id + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", desc='" + desc + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", source='" + source + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    ", used=" + used +
                    ", who='" + who + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Classify{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
