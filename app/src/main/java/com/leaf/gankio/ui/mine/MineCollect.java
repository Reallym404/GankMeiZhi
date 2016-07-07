package com.leaf.gankio.ui.mine;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.kennyc.view.MultiStateView;
import com.leaf.gankio.R;
import com.leaf.gankio.entity.GankBmob;
import com.leaf.gankio.listener.OnRecyclerViewItemClickListener;
import com.leaf.gankio.ui.base.BaseActivity;
import com.leaf.gankio.ui.base.GankIoApp;
import com.leaf.gankio.ui.gank.WebGank;
import com.leaf.gankio.utils.ActionBarUtil;
import com.leaf.gankio.utils.Constants;
import com.leaf.gankio.utils.PreferenceManager;
import com.leaf.gankio.utils.RecyclerViewStateUtils;
import com.leaf.gankio.utils.SnackbarUtil;
import com.leaf.gankio.widget.LoadingFooter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/17 16:01
 * @TODO ： 收藏
 */

public class MineCollect extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = "MineCollect";
    @Bind(R.id.collectRecyclerview)
    RecyclerView collectRecyclerview;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    private List<GankBmob> list;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private int pageNo = 0;
    private CollectAdapter adapter;
    private boolean isRefresh = true;
    private int index = -1 ;

    @Override
    protected int setLayoutResource() {
        return R.layout.activity_mine_collect;
    }

    @Override
    protected void initActionBar() {
        ActionBarUtil.initLeftBackActionBar(R.string.mine_collect, this);
    }

    @Override
    protected void initView() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorTheme, R.color.colorTheme);
        list = new ArrayList<GankBmob>();
        adapter = new CollectAdapter(list);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        collectRecyclerview.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        collectRecyclerview.setLayoutManager(layoutManager);
        collectRecyclerview.setHasFixedSize(true);

        //设置Item增加、移除动画
        collectRecyclerview.setItemAnimator(new DefaultItemAnimator());
        //// 分割线
        collectRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .drawable(R.drawable.listdivier)
                .size(GankIoApp.dpToPx(1))
                .build());

        collectRecyclerview.addOnScrollListener(mOnScrollListener);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter.setmOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public <T> void onItemClick(View view, T object,int position) {
                index = position ;
                GankBmob gankBmob = (GankBmob) object;
                Intent intent = new Intent(MineCollect.this,WebGank.class) ;
                intent.putExtra("desc",gankBmob.getDesc()) ;
                intent.putExtra("url",gankBmob.getUrl()) ;
                intent.putExtra("category",gankBmob.getType()) ;
                intent.putExtra("collect","collect") ;
                intent.putExtra("objectId",gankBmob.getObjectId()) ;
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 取消收藏返回 刷新
         */
        if(list.size() > 0 && WebGank.isDel && index > -1){
            list.remove(index) ;
            adapter.notifyDataSetChanged();
        }
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

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(collectRecyclerview);
            if (state == LoadingFooter.State.Loading || state == LoadingFooter.State.TheEnd) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }
            if (pageNo == 0) {
                pageNo = pageNo + 1;
            }
            RecyclerViewStateUtils.setFooterViewState(MineCollect.this, collectRecyclerview, Constants.PageSize, LoadingFooter.State.Loading, null);
            requestData();
        }
    };

    @Override
    protected void requestData() {
        BmobQuery<GankBmob> bmobQuery = new BmobQuery<GankBmob>();
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(Constants.PageSize);
        bmobQuery.addWhereEqualTo("uid", PreferenceManager.getInstance(this).getUid());
        bmobQuery.setSkip(Constants.PageSize * (pageNo++));
        bmobQuery.findObjects(this, new FindListener<GankBmob>() {
            @Override
            public void onSuccess(List<GankBmob> gankBmobs) {

                if (gankBmobs.size() > 0) {
                    if (multiStateView.getViewState() == MultiStateView.VIEW_STATE_LOADING || isRefresh) {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                        swipeRefreshLayout.setRefreshing(false);
                        isRefresh = false;
                        list.clear();
                    } else {
                        if (gankBmobs.size() < Constants.PageSize) {
                            RecyclerViewStateUtils.setFooterViewState(MineCollect.this, collectRecyclerview, Constants.PageSize, LoadingFooter.State.TheEnd, null);
                        } else {
                            RecyclerViewStateUtils.setFooterViewState(MineCollect.this, collectRecyclerview, Constants.PageSize, LoadingFooter.State.Normal, null);
                        }

                    }
                    list.addAll(gankBmobs);
                    adapter.notifyDataSetChanged();
                } else {
                    if (multiStateView.getViewState() == MultiStateView.VIEW_STATE_LOADING || isRefresh) {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(MineCollect.this, collectRecyclerview, Constants.PageSize, LoadingFooter.State.TheEnd, null);
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
                    RecyclerViewStateUtils.setFooterViewState(MineCollect.this, collectRecyclerview, Constants.PageSize, LoadingFooter.State.NetWorkError, reLoadClick);
                }
            }
        });
    }

    /**
     * 加载更多 重试
     */
    private View.OnClickListener reLoadClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(MineCollect.this, collectRecyclerview, Constants.PageSize, LoadingFooter.State.Loading, null);
            if (pageNo > 0) {
                pageNo--;
            }
            requestData();
        }
    };

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageNo = 0;
        requestData();
    }

    @OnClick(R.id.retry)
    public void retry() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        onRefresh();
    }
}
