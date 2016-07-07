package com.leaf.gankio.http.api;

import com.leaf.gankio.entity.Classify;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/2 16:58
 * @TODO ： 分类API
 */

public interface ClassifyAPI {

    /*分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
      数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
      请求个数： 数字，大于0
      第几页：数字，大于0
    */

    // http://gank.io/api/data/Android/10/1
    @GET("data/Android/15/{pageNo}")
    Observable<Classify> getClassifyAndroid(@Path("pageNo") int pageNo) ;

    @GET("data/福利/15/{pageNo}")
    Observable<Classify> getClassifyMeizhi(@Path("pageNo") int pageNo) ;

    @GET("data/iOS/15/{pageNo}")
    Observable<Classify> getClassifyiOS(@Path("pageNo") int pageNo) ;

    @GET("data/拓展资源/15/{pageNo}")
    Observable<Classify> getClassifyExpand(@Path("pageNo") int pageNo) ;

    @GET("data/前端/15/{pageNo}")
    Observable<Classify> getClassifyFe(@Path("pageNo") int pageNo) ;

    @GET("data/all/15/{pageNo}")
    Observable<Classify> getClassifyAll(@Path("pageNo") int pageNo) ;

    @GET("data/休息视频/15/{pageNo}")
    Observable<Classify> getClassifyRestvideo(@Path("pageNo") int pageNo) ;

    @GET("data/瞎推荐/15/{pageNo}")
    Observable<Classify> getClassifyRecommend(@Path("pageNo") int pageNo) ;
}
