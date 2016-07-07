package com.leaf.gankio.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/8 9:47
 * @TODO ： glide 配置
 */

public class MyGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache( new LruResourceCache( customMemoryCacheSize ));
        builder.setBitmapPool( new LruBitmapPool( customBitmapPoolSize ));

        int cacheSize50MegaBytes = 52428800;

        /*builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes)
        );*/

        /*builder.setDiskCache(
        new ExternalCacheDiskCacheFactory());*/
        builder.setDiskCache(new DiskLruCacheFactory(Constants.OwnCacheDirectory,"Cache",cacheSize50MegaBytes)) ;
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888); //PREFER_RGB_565
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
