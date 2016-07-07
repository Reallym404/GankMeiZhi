package com.leaf.gankio.ui.gank;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.leaf.gankio.R;
import com.leaf.gankio.entity.GankBmob;
import com.leaf.gankio.ui.base.BaseActivity;
import com.leaf.gankio.utils.ActionBarUtil;
import com.leaf.gankio.utils.PreferenceManager;
import com.leaf.gankio.utils.ShareUtil;
import com.leaf.gankio.utils.SnackbarUtil;

import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/17 9:48
 * @TODO ： 干货源
 */

public class WebGank extends BaseActivity{

    private String TAG = "WebGank" ;
    @Bind(R.id.progressbar)
    NumberProgressBar mProgressbar;
    @Bind(R.id.webView)
    WebView mWebView;
    private String title ;
    private String url ;
    private String category ;
    private boolean dounCollect = false ;
    private String objectId = "" ;
    //private final static int request_code_del = 201 ;
    public static boolean isDel = false ;

    @Override
    protected int setLayoutResource() {
        return R.layout.activity_web_gank;
    }

    @Override
    protected void initView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mWebView.setWebChromeClient(new WebGankChromeClient());
        mWebView.setWebViewClient(new WebGankClient());

        mWebView.loadUrl(url);
    }

    @Override
    protected void initActionBar() {
        title = getIntent().getStringExtra("desc");
        url = getIntent().getStringExtra("url");
        category = getIntent().getStringExtra("category") ;
        objectId = getIntent().getStringExtra("objectId") ;
        String collect = getIntent().getStringExtra("collect") ;
        if("collect".equals(collect)){
            dounCollect = true ;
        }
        ActionBarUtil.initLeftBackActionBar(title,this);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(dounCollect){
            MenuItem menuItem = menu.findItem(R.id.action_collect) ;
            menuItem.setTitle(R.string.action_uncollect) ;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.web_gank_menu, menu);
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
                String shareText = String.format(getString(R.string.share_from), title+" for "+category+ " "+url);
                ShareUtil.share(mContext, shareText);
                break ;
            case R.id.action_collect:
                if(!dounCollect){
                    doCollect();
                }else{
                    doUnCollect();
                }
                break ;
            case R.id.action_copy_link:
                String tips = getString(R.string.copy_tips);
                ClipData clipData = ClipData.newPlainText("leaf_gankio_copy", mWebView.getUrl());
                ClipboardManager manager = (ClipboardManager)getSystemService(
                        Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(clipData);
                SnackbarUtil.showSnackBar(getWindow().getDecorView(),tips) ;
                break ;
        }
        return true ;
    }

    /**
     * collect
     */
    private void doCollect(){
        final GankBmob gankBmob = new GankBmob(title,category,url, PreferenceManager.getInstance(mContext).getUid()) ;
        BmobQuery<GankBmob> bmobQuery = new BmobQuery<GankBmob>() ;
        bmobQuery.addWhereEqualTo("uid", PreferenceManager.getInstance(this).getUid());
        bmobQuery.addWhereEqualTo("url", url);
        bmobQuery.findObjects(mContext, new FindListener<GankBmob>() {
            @Override
            public void onSuccess(List<GankBmob> list) {
                if(list.size() > 0){
                    gankBmob.setObjectId(list.get(0).getObjectId());
                    String snack_message_collect_already = mContext.getString(R.string.snack_message_collect_already);
                    SnackbarUtil.showSnackBar(getWindow().getDecorView(),snack_message_collect_already);
                }else{
                    gankBmob.save(mContext, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG,"------doCollect onSuccess:") ;
                            SnackbarUtil.showSnackBar(getWindow().getDecorView(),getResources().getString(R.string.snack_message_collect_success)) ;
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.d(TAG,"------doCollect onFailure:"+i+","+s) ;
                            SnackbarUtil.checkErrorCode(mContext,i,getWindow().getDecorView(),"collect");
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                SnackbarUtil.checkErrorCode(mContext,i,getWindow().getDecorView(),"collect");
            }
        });
    }

    /**
     * un collect
     */
    private void doUnCollect(){
        GankBmob gankBmob = new GankBmob() ;
        gankBmob.setObjectId(objectId);
        gankBmob.delete(this, new DeleteListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"------doUnCollect onSuccess:") ;
                SnackbarUtil.showSnackBar(getWindow().getDecorView(),getResources().getString(R.string.snack_message_uncollect_success)) ;
                isDel = true ;
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d(TAG,"------doUnCollect onFailure:"+i+","+s) ;
                SnackbarUtil.checkErrorCode(mContext,i,getWindow().getDecorView(),"uncollect");
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) mWebView.destroy();
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
    }

    private class WebGankChromeClient extends WebChromeClient {

        @Override public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressbar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressbar.setVisibility(View.GONE);
            } else {
                mProgressbar.setVisibility(View.VISIBLE);
            }
        }


        @Override public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    private class WebGankClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }
    }
}
