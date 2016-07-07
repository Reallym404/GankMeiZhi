package com.leaf.gankio.ui.mine.setting;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.leaf.gankio.BuildConfig;
import com.leaf.gankio.R;
import com.leaf.gankio.ui.base.BaseActivity;
import com.leaf.gankio.ui.base.GankIoApp;
import com.leaf.gankio.utils.ActionBarUtil;
import com.leaf.gankio.utils.Constants;
import com.leaf.gankio.utils.FileUtil;
import com.leaf.gankio.utils.GlideBuilderTool;
import com.leaf.gankio.utils.ShareUtil;
import com.leaf.gankio.utils.SnackbarUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/19 17:39
 * @TODO ： 设置
 */

public class MineSetting extends BaseActivity {


    @Bind(R.id.setting_version_text)
    TextView settingVersionText;
    @Bind(R.id.setting_cache_text)
    TextView settingCacheText;
    @Bind(R.id.setting_version_lay)
    RelativeLayout settingVersionLay;
    @Bind(R.id.setting_cache_lay)
    RelativeLayout settingCacheLay;
    @Bind(R.id.setting_suggestions_lay)
    RelativeLayout settingSuggestionsLay;
    @Bind(R.id.setting_score_lay)
    RelativeLayout settingScoreLay;
    @Bind(R.id.setting_share_lay)
    RelativeLayout settingShareLay;
    @Bind(R.id.setting_aboutme_lay)
    RelativeLayout settingAboutmeLay;
    @Bind(R.id.setting_logout_lay)
    RelativeLayout settingLogoutLay;

    @Override
    protected int setLayoutResource() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initActionBar() {
        ActionBarUtil.initLeftBackActionBar(R.string.action_settings, this);
    }

    @Override
    protected void initView() {

        long cacheSize = FileUtil.getFolderSize(GankIoApp.getInstance().getExternalCacheDir()) ;
        long meizhiSize = FileUtil.getFolderSize(new File(Constants.OwnCacheDirectory + "Cache")) ;

        settingVersionText.setText("V " + BuildConfig.VERSION_NAME);
        settingCacheText.setText((cacheSize /*+ meizhiSize*/) > 1024 ? FileUtil.getFormatFolderSize(cacheSize /*+ meizhiSize*/):"");

        RxView.clicks(settingCacheLay).throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .map(new Func1<Void, Boolean>() {
                    @Override
                    public Boolean call(Void aVoid) {
                        if (!"".equals(settingCacheText.getText().toString())) {
                            GlideBuilderTool.getInstance().clearMemory(mContext);
                            GlideBuilderTool.getInstance().clearDiskCache(mContext);
                            FileUtil.deleteFolderFile(new File(Constants.OwnCacheDirectory + "Cache"));
                            FileUtil.deleteFolderFile(GankIoApp.getInstance().getExternalCacheDir());
                            return true;
                        }
                        return true;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean b) {
                        if (b) {
                            SnackbarUtil.showSnackBar(getWindow().getDecorView(), getString(R.string.clear_cache_tips));
                        }
                        settingCacheText.setText("");

                    }
                });

        RxView.clicks(settingSuggestionsLay).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        doSuggest();
                    }
                });

        RxView.clicks(settingScoreLay).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        doSocre();
                    }
                });

        RxView.clicks(settingShareLay).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ShareUtil.share(mContext, getString(R.string.share_text));
                    }
                });

        RxView.clicks(settingAboutmeLay).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(mContext, AboutMe.class);
                        startActivity(intent);
                    }
                });

        RxView.clicks(settingLogoutLay).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        GankIoApp.logout();
                    }
                });
    }

    private void doSuggest() {
        Uri uri = Uri.parse("mailto:yeming_1001@163.com");
        final Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (infos == null || infos.size() <= 0) {
            Toast.makeText(this, 0, Toast.LENGTH_SHORT).show();
            SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.setting_suggestions_tip));
            return;
        }
        startActivity(intent);
    }

    private void doSocre() {
        try {
            Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.setting_socre_tip));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

}
