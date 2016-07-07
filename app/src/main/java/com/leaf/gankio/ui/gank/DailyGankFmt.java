package com.leaf.gankio.ui.gank;

import android.os.Bundle;

import com.leaf.gankio.R;
import com.leaf.gankio.ui.base.BaseFragment;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/15 15:57
 * @TODO ： ...
 */

public class DailyGankFmt extends BaseFragment {

    private String TAG = "DailyGankFmt" ;

    private String date ;

    public DailyGankFmt(){}

    public static DailyGankFmt newInstance(String date) {
        DailyGankFmt fragment = new DailyGankFmt();
        Bundle args = new Bundle();
        args.putString("date",date);
        fragment.setArguments(args);
        return fragment;
    }

    private void parseArguments() {
        Bundle bundle = getArguments();
        date = bundle.getString("date") ;
    }

    @Override
    protected int setLayoutResource() {
        return R.layout.fmt_daily_gank;
    }

    @Override
    protected void initView() {
        parseArguments() ;
    }


}
