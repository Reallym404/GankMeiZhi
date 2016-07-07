package com.leaf.gankio.ui.gank;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.kennyc.view.MultiStateView;
import com.leaf.gankio.R;
import com.leaf.gankio.entity.Daily;
import com.leaf.gankio.http.RetrofitClient;
import com.leaf.gankio.http.api.DailyAPI;
import com.leaf.gankio.ui.base.BaseActivity;
import com.leaf.gankio.utils.ActionBarUtil;
import com.leaf.gankio.utils.ShareUtil;
import com.leaf.gankio.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/15 15:51
 * @TODO ： 每天干货
 */

public class DailyGank extends BaseActivity {

    @Bind(R.id.dailyGankRecyclerview)
    RecyclerView dailyGankRecyclerview;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    private String TAG = "DailyGank";

    private String title = "";
    private String date = "";
    private List<Daily.ResultsBean.GankBean> list ;
    private DailyGankAdapter adapter ;

    @Override
    protected int setLayoutResource() {
        return R.layout.activity_daily_gank;
    }

    @Override
    protected void initActionBar() {
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        ActionBarUtil.initLeftBackActionBar(title,this);
    }

    @Override
    protected void initView() {
        Log.d(TAG, "----initView:"+title+","+date);
        list = new ArrayList<Daily.ResultsBean.GankBean>() ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dailyGankRecyclerview.setLayoutManager(layoutManager);

        adapter = new DailyGankAdapter(list) ;
        dailyGankRecyclerview.setAdapter(adapter);
    }

    @Override
    protected void requestData() {
        RetrofitClient.getInstance().createService(DailyAPI.class).getDailyData(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Daily>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "----onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "----onError"+e.getMessage());
                        if(multiStateView.getViewState() == MultiStateView.VIEW_STATE_LOADING){
                            multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                        }
                        SnackbarUtil.checkThrowableException(mContext,e,getWindow().getDecorView());
                    }

                    @Override
                    public void onNext(Daily daily) {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                        if(daily.getResults().getAndroid() != null) list.addAll(daily.getResults().getAndroid()) ;
                        if(daily.getResults().getIOS() != null) list.addAll(daily.getResults().getIOS()) ;
                        if(daily.getResults().getRecommend() != null) list.addAll(daily.getResults().getRecommend()) ;
                        if(daily.getResults().getExpand() != null) list.addAll(daily.getResults().getExpand()) ;
                        if(daily.getResults().getRestVideo() != null) list.addAll(0,daily.getResults().getRestVideo()) ;
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.daily_gank_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break ;
            case R.id.action_share:
                if (list.size() != 0) {
                    Daily.ResultsBean.GankBean gankBean= list.get(0) ;
                    String shareText = String.format(getString(R.string.share_from), gankBean.getDesc() +" "+ gankBean.getUrl()+" ");
                    ShareUtil.share(mContext, shareText);
                } else {
                    ShareUtil.share(mContext, getResources().getString(R.string.share_text));
                }
                break ;
        }
        return true ;
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
