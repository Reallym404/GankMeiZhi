package com.leaf.gankio.http.api;

import com.leaf.gankio.entity.History;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/15 12:44
 * @TODO ： 干货历史 API
 */

public class HistoryAPI {

    private final String URL = "http://gank.io/history" ;

    /**
     * 获取历史数据
     * @param s
     */
   public void getHitoryAll(Subscriber s){
        Observable.just(URL)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Func1<String, List<History>>() {
                    @Override
                    public List<History> call(String s) {
                        return parseHtml(s);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
   }

    /**
     * 解析网页 封装History类
     * @param url
     * @return
     */
    private List<History> parseHtml(String url) {
        List<History> list = new ArrayList<History>();
        try {
            Document doc = Jsoup.connect(url).timeout(5000).get();
            Element io_list = doc.getElementsByClass("content").get(0);
            Elements io_lists = io_list.select("li");
            for (Element io : io_lists) {
                History history = new History();
                Element href_a = io.getElementsByTag("a").first();
                history.setDate(href_a.attr("href"));
                history.setTitle(href_a.text());
                list.add(history);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
