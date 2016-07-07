package com.leaf.gankio.http.api;

import com.leaf.gankio.entity.Daily;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/2 17:15
 * @TODO ： 每日数据api
 */

public interface DailyAPI {

    /*  每日数据： http://gank.io/api/day/年/月/日
        例：
         http://gank.io/api/day/2015/08/06*/

    @GET("day{date}")
    Observable<Daily> getDailyData(@Path("date") String date) ;
}
