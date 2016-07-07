package com.leaf.gankio.ui.meizi;

import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.leaf.gankio.R;
import com.leaf.gankio.entity.Classify;
import com.leaf.gankio.entity.MeiziBmob;
import com.leaf.gankio.http.RetrofitClient;
import com.leaf.gankio.http.api.DownloadAPI;
import com.leaf.gankio.ui.base.BaseActivity;
import com.leaf.gankio.utils.ActionBarUtil;
import com.leaf.gankio.utils.Constants;
import com.leaf.gankio.utils.FileUtil;
import com.leaf.gankio.utils.GlideBuilderTool;
import com.leaf.gankio.utils.PreferenceManager;
import com.leaf.gankio.utils.ShareUtil;
import com.leaf.gankio.utils.SnackbarUtil;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/8 21:15
 * @TODO ： 妹纸大图
 */

public class ZoomMeiziAty extends BaseActivity {

    private String TAG = "ZoomMeiziAty";
    @Bind(R.id.img_photoView)
    PhotoView imgPhotoView;
    PhotoViewAttacher mAttacher;
    private String title = "";
    private String url = "";
    private boolean isLike = false;// 是否喜欢
    private Classify.ClassifyResultsBean resultsBean;
    private String oId;
    private MenuItem menuItem;
    public static final String TRANSIT_PIC = "ZoomMeizi";
    public static boolean isUnLikeOpre = false;

    @Override
    protected int setLayoutResource() {
        return R.layout.activity_zoom_meizi;
    }

    @Override
    protected void initActionBar() {

        //ViewCompat.setTransitionName(imgPhotoView, TRANSIT_PIC);
        boolean fromMine = getIntent().getBooleanExtra("like", false);
        if (!fromMine) {
            resultsBean = (Classify.ClassifyResultsBean) getIntent().getSerializableExtra("meizi");
        } else {
            isLike = true;
            MeiziBmob meiziBmob = (MeiziBmob) getIntent().getSerializableExtra("meizi");
            resultsBean = new Classify.ClassifyResultsBean();
            resultsBean.setPublishedAt(meiziBmob.getPublishedAt());
            resultsBean.setDesc(meiziBmob.getDesc());
            resultsBean.setType(meiziBmob.getType());
            resultsBean.setUrl(meiziBmob.getUrl());
        }
        title = resultsBean.getPublishedAt().split("T")[0];
        url = resultsBean.getUrl();
        ActionBarUtil.initLeftBackActionBar(title, this);
    }

    @Override
    protected void initView() {
        GlideBuilderTool.getInstance().loadImgOfThird(this, url, imgPhotoView, R.mipmap.ic_big_default_load_bg);
        //mAttacher = new PhotoViewAttacher(imgPhotoView);
    }

    @Override
    protected void requestData() {
        Log.d(TAG, "------requestData:");
        operatequery();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //创建menu视图
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.zoom_meizi_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.action_like);
        setIsLike(menu.findItem(R.id.action_like), isLike);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_like:
                if (isLike) {
                    operateUnlike();
                } else {
                    operateLike();
                }
                break;
            case R.id.action_share:
                download(true);
                break;
            case R.id.action_download:
                download(false);
                break;
        }
        return true;
    }

    /**
     * 如果为true 显示unlike图片
     * 为false 显示like图标
     *
     * @param item
     * @param isLike
     */
    private void setIsLike(MenuItem item, boolean isLike) {
        int drawableId = isLike ? R.mipmap.ico_like_press:R.mipmap.ico_like_nor ;
        item.setIcon(drawableId);
    }

    /**
     * like 即插入数据
     */
    private void operateLike() {
        final MeiziBmob meiziBmob = new MeiziBmob(resultsBean.getPublishedAt(), resultsBean.getDesc(), resultsBean.getType(), resultsBean.getUrl(), PreferenceManager.getInstance(this).getUid());
        meiziBmob.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "------operateLike onSuccess:" + meiziBmob.getObjectId());
                oId = meiziBmob.getObjectId();
                menuItem.setEnabled(true);
                isLike = !isLike;
                setIsLike(menuItem, isLike);
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d(TAG, "------operateLike onFailure:" + i + "," + s);
                menuItem.setEnabled(true);
                SnackbarUtil.checkErrorCode(mContext, i, getWindow().getDecorView().getRootView(), null);
            }

            @Override
            public void onStart() {
                Log.d(TAG, "------operateLike onStart:");
                menuItem.setEnabled(false);
            }
        });
    }

    /**
     * 查询like状态
     */
    private void operatequery() {
        BmobQuery<MeiziBmob> bmobQuery = new BmobQuery<MeiziBmob>();
        bmobQuery.addWhereEqualTo("uid", PreferenceManager.getInstance(this).getUid());
        bmobQuery.addWhereEqualTo("url", url);
        bmobQuery.findObjects(this, new FindListener<MeiziBmob>() {
            @Override
            public void onSuccess(List<MeiziBmob> list) {
                Log.d(TAG, "------operatequery:" + list.size());
                if (list.size() > 0) {
                    oId = list.get(0).getObjectId();
                    isLike = true;
                    setIsLike(menuItem, isLike);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "------query onError:" + i + "," + s);
                SnackbarUtil.checkErrorCode(mContext, i, getWindow().getDecorView().getRootView(), null);
                if (i == 9016) {
                    String snack_message_net_error = mContext.getString(R.string.snack_message_net_error);
                    SnackbarUtil.showNetworkErrorSnackBar(mContext, getWindow().getDecorView(), snack_message_net_error, mContext.getString(R.string.action_settings));
                }
            }
        });
    }

    /**
     * unlike 即删除
     */
    private void operateUnlike() {
        MeiziBmob meiziBmob = new MeiziBmob();
        meiziBmob.setObjectId(oId);
        meiziBmob.delete(this, new DeleteListener() {

            @Override
            public void onStart() {
                Log.d(TAG, "------operateUnlike onStart:");
                menuItem.setEnabled(false);
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "------operateUnlike onSuccess:");
                menuItem.setEnabled(true);
                oId = "";
                isLike = !isLike;
                setIsLike(menuItem, isLike);
                isUnLikeOpre = true;
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d(TAG, "------operateUnlike onFailure:" + i + "," + s);
                menuItem.setEnabled(true);
                SnackbarUtil.checkErrorCode(mContext, i, getWindow().getDecorView().getRootView(), null);
            }
        });
    }

    /**
     * 下载
     */
    private void download(final boolean isShare) {
        Subscription s = RetrofitClient.getInstance().createService(DownloadAPI.class).downloadMeizhi(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(new Func1<ResponseBody, Uri>() {
                    @Override
                    public Uri call(ResponseBody responseBody) {
                        int lastSlashIndex = url.lastIndexOf("/");
                        String fileName = url.substring(lastSlashIndex + 1);
                        Uri uri = FileUtil.writeResponseBodyToDisk(getApplicationContext(), responseBody, fileName);
                        if (null != uri) {
                            return uri;
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "-----------download onError:" + e.getMessage());
                        SnackbarUtil.checkThrowableException(mContext, e, imgPhotoView);
                    }

                    @Override
                    public void onNext(Uri uri) {
                        Log.d(TAG, "-----------download onNext:" + uri);
                        if (!isShare) {
                            File dir = new File(Constants.OwnCacheDirectory, "MeiZhi");
                            String msg = String.format(getResources().getString(R.string.download_success_tips), dir.getAbsolutePath());
                            Snackbar.make(imgPhotoView, msg, Snackbar.LENGTH_INDEFINITE).setAction(R.string.snack_action_msg, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                        } else {
                            ShareUtil.shareImage(mContext, uri, getResources().getString(R.string.share_meizhi_to));
                        }
                    }
                });
        addSubscription(s);
    }


}
