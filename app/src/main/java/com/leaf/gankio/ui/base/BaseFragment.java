package com.leaf.gankio.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * @author :  Leaf<br>
 * @package ： com.leaf.gankio.base
 * @date ： 2016/5/25 15:25
 * @desc ： TODO
 */
public abstract class BaseFragment extends Fragment {

    protected View mContentView;
    protected BaseActivity activity ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mContentView) {
            mContentView = inflater.inflate(setLayoutResource(), container, false);
        }
        ButterKnife.bind(this,mContentView);
        activity = (BaseActivity) getActivity();
        initView();
        requestData();
        return mContentView;
    }

    protected abstract int setLayoutResource();

    protected void initView(){}

    protected void requestData(){}

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
