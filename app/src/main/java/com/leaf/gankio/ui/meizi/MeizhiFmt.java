package com.leaf.gankio.ui.meizi;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.HeaderSpanSizeLookup;
import com.kennyc.view.MultiStateView;
import com.leaf.gankio.R;
import com.leaf.gankio.entity.Classify;
import com.leaf.gankio.http.RetrofitClient;
import com.leaf.gankio.http.api.ClassifyAPI;
import com.leaf.gankio.listener.OnRecyclerViewItemClickListener;
import com.leaf.gankio.ui.base.BaseFragment;
import com.leaf.gankio.utils.Constants;
import com.leaf.gankio.utils.RecyclerViewStateUtils;
import com.leaf.gankio.widget.LoadingFooter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/2 15:23
 * @TODO ： 妹纸
 */

public class MeizhiFmt extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = "MeizhiFmt" ;
    @Bind(R.id.meiziRecyclerview)
    RecyclerView meiziRecyclerview;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    private List<Classify.ClassifyResultsBean> list;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private RecyclerViewAdapter adapter;
    private int pageNo = 1;
    @Override
    protected int setLayoutResource() {
        return R.layout.fmt_meizhi;
    }

   /* @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }*/


    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void initView() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

        swipeRefreshLayout.setColorSchemeColors(R.color.colorTheme,R.color.colorTheme);
        list = new ArrayList<Classify.ClassifyResultsBean>();
        adapter = new RecyclerViewAdapter(list);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        meiziRecyclerview.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        //setLayoutManager
        GridLayoutManager manager = new GridLayoutManager(activity, 2);
        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) meiziRecyclerview.getAdapter(), manager.getSpanCount()));
        meiziRecyclerview.setLayoutManager(manager);

        meiziRecyclerview.addOnScrollListener(mOnScrollListener);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter.setmOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public <T> void onItemClick(View view, T object,int positon) {
                Classify.ClassifyResultsBean resultsBean = (Classify.ClassifyResultsBean) object;
                Intent intent = new Intent(activity,ZoomMeiziAty.class) ;
                intent.putExtra("meizi",resultsBean) ;
                activity.startActivity(intent);

                /*ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, view, ZoomMeiziAty.TRANSIT_PIC);
                try {
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    startActivity(intent);
                }*/
            }
        });

    }

    @Override
    protected void requestData() {
        RetrofitClient.getInstance().createService(ClassifyAPI.class).getClassifyMeizhi(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Classify>() {
                    @Override
                    public void onCompleted() {
                        Log.d("TAG", "----onCompleted");
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "----onError："+e.getMessage());
                        if(multiStateView.getViewState() == MultiStateView.VIEW_STATE_LOADING){
                            multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        //SnackbarUtil.checkThrowableException(activity,e,mContentView);
                    }

                    @Override
                    public void onNext(Classify classify) {
                        Log.d("TAG", "----onNext:" + classify.getResults().size());
                        swipeRefreshLayout.setRefreshing(false);
                        List<Classify.ClassifyResultsBean> resultsBeanList = classify.getResults() ;
                        if(resultsBeanList.size() > 0){
                            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                            list.clear();
                            list.addAll(classify.getResults());
                            //mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
                            adapter.notifyDataSetChanged();
                        }else{
                            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                        }

                    }
                });
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(meiziRecyclerview);
            if (state == LoadingFooter.State.Loading || state == LoadingFooter.State.TheEnd) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }
            loadMore();
        }
    };

    @Override
    public void onRefresh() {
        requestData();
    }

    /**
     * 重试
     */
    @OnClick(R.id.retry)
    public void retry() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        requestData();
    }

    /**
     * 加载更多 重试
     */
    private View.OnClickListener reLoadClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(activity, meiziRecyclerview, Constants.PageSize, LoadingFooter.State.Loading, null);
            loadMore();
        }
    };

    /**
     * 加载更多
     */
    private void loadMore(){
        pageNo ++ ;
        RecyclerViewStateUtils.setFooterViewState(activity, meiziRecyclerview, Constants.PageSize, LoadingFooter.State.Loading, null);
        RetrofitClient.getInstance().createService(ClassifyAPI.class).getClassifyMeizhi(pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Classify>() {
                    @Override
                    public void onCompleted() {
                        Log.d("tag", "----onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "----onError");
                        RecyclerViewStateUtils.setFooterViewState(activity, meiziRecyclerview, Constants.PageSize, LoadingFooter.State.NetWorkError, reLoadClick);
                    }

                    @Override
                    public void onNext(Classify classify) {
                        Log.d("TAG", "----onNext:" + classify.getResults().size());
                        if(classify.isError()){
                            RecyclerViewStateUtils.setFooterViewState(activity, meiziRecyclerview, Constants.PageSize, LoadingFooter.State.NetWorkError, reLoadClick);
                        }else{
                            List<Classify.ClassifyResultsBean> resultsBeanList = classify.getResults() ;
                            if(resultsBeanList.size() > 0 ){
                                RecyclerViewStateUtils.setFooterViewState(activity, meiziRecyclerview, Constants.PageSize, LoadingFooter.State.Normal, null);
                            }
                            if(resultsBeanList.size() < Constants.PageSize){
                                RecyclerViewStateUtils.setFooterViewState(activity, meiziRecyclerview, Constants.PageSize, LoadingFooter.State.TheEnd, null);
                            }
                            list.addAll(resultsBeanList) ;
                            //mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}
