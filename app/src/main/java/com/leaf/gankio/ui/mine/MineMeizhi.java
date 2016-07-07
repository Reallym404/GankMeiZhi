package com.leaf.gankio.ui.mine;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.ExStaggeredGridLayoutManager;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.HeaderSpanSizeLookup;
import com.kennyc.view.MultiStateView;
import com.leaf.gankio.R;
import com.leaf.gankio.entity.MeiziBmob;
import com.leaf.gankio.listener.OnRecyclerViewItemClickListener;
import com.leaf.gankio.ui.base.BaseActivity;
import com.leaf.gankio.ui.meizi.ZoomMeiziAty;
import com.leaf.gankio.utils.ActionBarUtil;
import com.leaf.gankio.utils.Constants;
import com.leaf.gankio.utils.PreferenceManager;
import com.leaf.gankio.utils.RecyclerViewStateUtils;
import com.leaf.gankio.utils.SnackbarUtil;
import com.leaf.gankio.widget.LoadingFooter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/17 20:24
 * @TODO ： ...
 */

public class MineMeizhi extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.mineMeizhiRecyclerview)
    RecyclerView mineMeizhiRecyclerview;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    private List<MeiziBmob> list;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private MineMeizhiAdapter adapter;
    private int pageNo = 0;
    private boolean isRefresh = true;
    private int index = -1 ;

    @Override
    protected int setLayoutResource() {
        return R.layout.activity_mine_meizhi;
    }

    @Override
    protected void initActionBar() {
        ActionBarUtil.initLeftBackActionBar(R.string.mine_like, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break ;
        }
        return true ;
    }

    @Override
    protected void initView() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

        swipeRefreshLayout.setColorSchemeColors(R.color.colorTheme,R.color.colorTheme);
        list = new ArrayList<MeiziBmob>();
        adapter = new MineMeizhiAdapter(list);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        mineMeizhiRecyclerview.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        //setLayoutManager
        ExStaggeredGridLayoutManager manager = new ExStaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL) ;
        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) mineMeizhiRecyclerview.getAdapter(), manager.getSpanCount()));
        mineMeizhiRecyclerview.setLayoutManager(manager);

        mineMeizhiRecyclerview.addOnScrollListener(mOnScrollListener);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter.setmOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public <T> void onItemClick(View view, T object, int positon) {
                index = positon ;
                MeiziBmob meiziBmob = (MeiziBmob) object;
                Intent intent = new Intent(MineMeizhi.this,ZoomMeiziAty.class) ;
                intent.putExtra("meizi",meiziBmob) ;
                intent.putExtra("like",true) ;
                startActivity(intent);

            }
        });
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mineMeizhiRecyclerview);
            if (state == LoadingFooter.State.Loading  || state == LoadingFooter.State.TheEnd) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }

            if (pageNo == 0) {
                pageNo = pageNo + 1;
            }
            RecyclerViewStateUtils.setFooterViewState(MineMeizhi.this, mineMeizhiRecyclerview, Constants.PageSize, LoadingFooter.State.Loading, null);
            requestData();
        }
    };

    @Override
    protected void requestData() {

        BmobQuery<MeiziBmob> bmobQuery = new BmobQuery<MeiziBmob>();
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(Constants.PageSize);
        bmobQuery.addWhereEqualTo("uid", PreferenceManager.getInstance(this).getUid());
        bmobQuery.setSkip(Constants.PageSize * (pageNo++));
        bmobQuery.findObjects(this, new FindListener<MeiziBmob>() {
            @Override
            public void onSuccess(List<MeiziBmob> meiziBmobs) {

                if (meiziBmobs.size() > 0) {
                    if (multiStateView.getViewState() == MultiStateView.VIEW_STATE_LOADING || isRefresh) {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                        swipeRefreshLayout.setRefreshing(false);
                        isRefresh = false;
                        list.clear();
                    } else {
                        if (meiziBmobs.size() < Constants.PageSize) {
                            RecyclerViewStateUtils.setFooterViewState(MineMeizhi.this, mineMeizhiRecyclerview, Constants.PageSize, LoadingFooter.State.TheEnd, null);
                        } else {
                            RecyclerViewStateUtils.setFooterViewState(MineMeizhi.this, mineMeizhiRecyclerview, Constants.PageSize, LoadingFooter.State.Normal, null);
                        }

                    }
                    list.addAll(meiziBmobs);
                    adapter.notifyDataSetChanged();
                } else {
                    if (multiStateView.getViewState() == MultiStateView.VIEW_STATE_LOADING || isRefresh) {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(MineMeizhi.this, mineMeizhiRecyclerview, Constants.PageSize, LoadingFooter.State.TheEnd, null);
                    }
                }


            }

            @Override
            public void onError(int i, String s) {
                if (multiStateView.getViewState() == MultiStateView.VIEW_STATE_LOADING || isRefresh) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    swipeRefreshLayout.setRefreshing(false);
                    SnackbarUtil.checkErrorCode(mContext, i, getWindow().getDecorView(),null);
                } else {
                    RecyclerViewStateUtils.setFooterViewState(MineMeizhi.this, mineMeizhiRecyclerview, Constants.PageSize, LoadingFooter.State.NetWorkError, reLoadClick);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(list.size() > 0 && ZoomMeiziAty.isUnLikeOpre && index > -1){
            list.remove(index) ;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageNo = 0;
        requestData();
    }

    /**
     * 加载更多 重试
     */
    private View.OnClickListener reLoadClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(MineMeizhi.this, mineMeizhiRecyclerview, Constants.PageSize, LoadingFooter.State.Loading, null);
            if (pageNo > 0) {
                pageNo--;
            }
            requestData();
        }
    };

    /**
     * 重试
     */
    @OnClick(R.id.retry)
    public void retry() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        requestData();
    }
}
