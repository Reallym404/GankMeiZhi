package com.leaf.gankio.http.api;

import com.leaf.gankio.entity.SinaTokenInfo;
import com.leaf.gankio.entity.SinaUser;

import java.util.HashMap;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/3 17:24
 * @TODO ： 微博API
 */

public interface SinaUserAPI {

    /*获取新浪用户信息
    SINA_USER_INFO_URL = "https://api.weibo.com/2/users/show.json";
    必选	类型及范围	说明
    access_token	true	string	采用OAuth授权方式为必填参数，OAuth授权后获得。
    uid	false	int64	需要查询的用户ID。
    screen_name	false	string	需要查询的用户昵称。*/
    @GET("https://api.weibo.com/2/users/show.json")
    Observable<SinaUser> getSinaUserInfo(@Query("access_token") String access_token, @Query("uid") String uid) ;

    /**
     *  https://api.weibo.com/oauth2/get_token_info
     *  POST
     *  查询用户access_token的授权相关信息，包括授权时间，过期时间和scope权限。
     *  参数：
     *  access_token：用户授权时生成的access_token。
     */
    @POST("https://api.weibo.com/oauth2/get_token_info")
    Observable<SinaTokenInfo> getSinaTokenInfo(@QueryMap HashMap<String, String> map) ;

}
