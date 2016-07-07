package com.leaf.gankio.utils;

import android.support.v7.app.ActionBar;

import com.leaf.gankio.R;
import com.leaf.gankio.ui.base.BaseActivity;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/2 16:01
 * @TODO ：
 */

public class ActionBarUtil {

    /**
     * 带返回按钮的actionBar
     * @param resId
     * @param activity
     */
    public static void initLeftBackActionBar(int resId, BaseActivity activity) {
        if (activity == null)
            return;
        ActionBar actionBar = activity.getSupportActionBar() ;
        actionBar.setTitle(resId);
        actionBar.collapseActionView() ;
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static void initLeftBackActionBar(String title, BaseActivity activity) {
        if (activity == null)
            return;
        ActionBar actionBar = activity.getSupportActionBar() ;
        actionBar.setTitle(title);
        actionBar.collapseActionView() ;
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 带返回按钮的actionBar 无标题
     * @param activity
     */
    public static void initLeftBackNoTitleActionBar(BaseActivity activity) {
        if (activity == null)
            return;
        ActionBar actionBar = activity.getSupportActionBar() ;
        actionBar.setTitle("");
        actionBar.collapseActionView() ;
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 设置标题
     * @param resId
     * @param activity
     */
    public static void setActionBarTitle(int resId,BaseActivity activity){
        ActionBar actionBar = activity.getSupportActionBar() ;
        actionBar.setTitle(resId);
    }
}
