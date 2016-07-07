package com.leaf.gankio.listener;

import android.view.View;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/8 21:25
 * @TODO ： 点击监听
 */

public interface OnRecyclerViewItemClickListener {

    <T> void onItemClick(View view , T object,int position);
}
