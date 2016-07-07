package com.leaf.gankio.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.leaf.gankio.R;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/8 12:10
 * @TODO ： ...
 */

public class GlideBuilderTool {

    private GlideBuilderTool() {
    }

    private static class SingletonHolder {
        public final static GlideBuilderTool instance = new GlideBuilderTool();
    }

    public static GlideBuilderTool getInstance() {
        return SingletonHolder.instance;
    }

    public void loadImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_default_load_bg)
                .error(R.mipmap.ic_default_fail_bg)
                //.diskCacheStrategy( DiskCacheStrategy.SOURCE )
                //.skipMemoryCache( true )
                .into(imageView);

        /*DiskCacheStrategy.NONE 什么都不缓存
        DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像。在我们上面的例子中，将会只有一个 1000x1000 像素的图片
        DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即，降低分辨率后的（或者是转换后的）
        DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）*/
    }

    /**
     * 加载第三方imgview
     *
     * @param context
     * @param url
     * @param imageView
     * @param placeholder
     */
    public void loadImgOfThird(Context context, String url, ImageView imageView, int placeholder) {
        Glide.with(context).load(url)
                .dontAnimate()
                .centerCrop()
                .placeholder(placeholder)
                //.diskCacheStrategy( DiskCacheStrategy.SOURCE )
                .into(imageView/*new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {

                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        super.onLoadCleared(placeholder);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                    }
                }*/);
    }

    /**
     * 回掉
     *
     * @param context
     * @param url
     * @param placeholder
     * @param simpleTarget
     */
    public void loadImgOfThird(Context context, String url, int placeholder, SimpleTarget<GlideDrawable> simpleTarget) {
        Glide.with(context).load(url)
                .dontAnimate()
                .centerCrop()
                .placeholder(placeholder)
                .into(simpleTarget);
    }

    /**
     * 圆形Img
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadCircleImg(final Context context, String url, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_default_load_bg)
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .into(imageView);

    }

    /**
     * 圆形头像
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadCircleAvatar(final Context context, String url, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.mine_avatar_normal)
                .transform(new GlideCircleTransform(context))
                .crossFade()
                //.diskCacheStrategy( DiskCacheStrategy.SOURCE )
                .into(imageView);

    }

    /**
     * 清楚缓存
     *
     * @param context
     */
    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 清楚磁盘缓存
     *
     * @param context
     */
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }
}
