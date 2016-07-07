package com.leaf.gankio.ui.gank;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.kennyc.view.MultiStateView;
import com.leaf.gankio.R;
import com.leaf.gankio.entity.History;
import com.leaf.gankio.http.api.HistoryAPI;
import com.leaf.gankio.listener.OnRecyclerViewItemClickListener;
import com.leaf.gankio.ui.base.BaseFragment;
import com.leaf.gankio.ui.base.GankIoApp;
import com.leaf.gankio.utils.Constants;
import com.leaf.gankio.utils.RecyclerViewStateUtils;
import com.leaf.gankio.widget.LoadingFooter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/15 13:27
 * @TODO ： 干货历史列表
 */

public class GankHistoryFmt extends BaseFragment {

    private String TAG = "GankHistoryFmt" ;
    @Bind(R.id.gankRecyclerview)
    RecyclerView gankRecyclerview;
    /*@Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;*/
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    private List<History> list;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private int pageNo = 1;
    private GankListAdapter adapter ;

    @Override
    protected int setLayoutResource() {
        return R.layout.fmt_gank_history;
    }

    @Override
    protected void initView() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        //swipeRefreshLayout.setRefreshing(false);

        list = new ArrayList<History>();
        adapter = new GankListAdapter(list);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        gankRecyclerview.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity) ;
        gankRecyclerview.setLayoutManager(layoutManager);
        gankRecyclerview.setHasFixedSize(true);

        //设置Item增加、移除动画
        gankRecyclerview.setItemAnimator(new DefaultItemAnimator());
        //// 分割线
        gankRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(activity)
                .drawable(R.drawable.listdivier)
                .size(GankIoApp.dpToPx(2))
                .build());

        gankRecyclerview.addOnScrollListener(mOnScrollListener);

        adapter.setmOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public <T> void onItemClick(View view, T object,int positon) {
                Intent intent = new Intent(activity, DailyGank.class);
                intent.putExtra("title",((History)object).getTitle()) ;
                intent.putExtra("date",((History)object).getDate()) ;
                activity.startActivity(intent);
            }
        });
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(gankRecyclerview);
            if (state == LoadingFooter.State.Loading) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }
            RecyclerViewStateUtils.setFooterViewState(activity, gankRecyclerview, Constants.PageSize, LoadingFooter.State.TheEnd, null);
        }
    };

    @Override
    protected void requestData() {
        Subscriber<List<History>> subscriber = new Subscriber<List<History>>() {
            @Override
            public void onCompleted() {
                Log.d("TAG", "----onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                if(multiStateView.getViewState() == MultiStateView.VIEW_STATE_LOADING){
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                }
            }

            @Override
            public void onNext(List<History> histories) {
                Log.d("TAG", "----onNext:" + histories.size());
                if(histories.size()>0){
                    // 2015/05/18 开始提供每天数据
                    histories = histories.subList(0,histories.size() - 44) ;
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    list.clear();
                    list.addAll(histories);
                    adapter.notifyDataSetChanged();
                }else{
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                }

            }
        } ;
        new HistoryAPI().getHitoryAll(subscriber);
    }

    /**
     * 重试
     */
    @OnClick(R.id.retry)
    public void retry() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        requestData();
    }
}
