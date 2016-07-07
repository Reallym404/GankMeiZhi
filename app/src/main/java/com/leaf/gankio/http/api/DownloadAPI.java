package com.leaf.gankio.http.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/19 22:33
 * @TODO ： ...
 */

public interface DownloadAPI {

    @Streaming
    @GET
    Observable<ResponseBody> downloadMeizhi(@Url String url) ;
}
