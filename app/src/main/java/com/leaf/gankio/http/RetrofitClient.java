package com.leaf.gankio.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/2 16:39
 * @TODO ： Retrofit 封装
 */

public class RetrofitClient {

    public static final String BASEURL = "http://gank.io/api/";
    private static final int DEFAULT_TIMEOUT = 5;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private RetrofitClient(){
        httpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    private static class SingletonHolder{
        private static final RetrofitClient INSTANCE = new RetrofitClient() ;
    }

    public static RetrofitClient getInstance(){
        return SingletonHolder.INSTANCE ;
    }

    /*private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASEURL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create());*/

    public <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

    public <S> S createServiceOfScalars(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .client(httpClient.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }
}
