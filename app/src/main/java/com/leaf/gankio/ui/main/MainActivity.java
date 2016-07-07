package com.leaf.gankio.ui.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.leaf.gankio.R;
import com.leaf.gankio.entity.SinaTokenInfo;
import com.leaf.gankio.http.RetrofitClient;
import com.leaf.gankio.http.api.SinaUserAPI;
import com.leaf.gankio.ui.base.BaseActivity;
import com.leaf.gankio.ui.gank.GankHistoryFmt;
import com.leaf.gankio.ui.meizi.MeizhiFmt;
import com.leaf.gankio.ui.mine.MineFmt;
import com.leaf.gankio.utils.ActionBarUtil;
import com.leaf.gankio.utils.PreferenceManager;

import java.util.HashMap;

import butterknife.Bind;
import rx.Observable;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @Bind(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    private FragmentManager fragmentManager;
    private MeizhiFmt meizhiFmt;
    private MineFmt mineFmt;
    private GankHistoryFmt gankHistoryFmt ;
    private int titleId[] = {R.string.meizhi,R.string.history,R.string.mine} ;
    private long lastBackKeyDownTick = 0;
    public static final long DOUBLE_BACK_DURATION = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getFragmentManager() ;
        initBottomBar();
    }

    /**
     * @return
     */
    private Observable<SinaTokenInfo> getSinaTokenInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        String access_token = PreferenceManager.getInstance(mContext).readAccessToken().getToken();
        map.put("access_token", access_token);
        return RetrofitClient.getInstance().createService(SinaUserAPI.class).getSinaTokenInfo(map);
    }

    @Override
    protected int setLayoutResource() {
        return R.layout.activity_main;
    }

    private void initBottomBar() {
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.meizhi,R.string.meizhi))
                .addItem(new BottomNavigationItem(R.mipmap.classify,R.string.history))
                .addItem(new BottomNavigationItem(R.mipmap.mine,R.string.mine))
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this) ;
        onTabSelected(0) ;
    }

    /**
     * 隐藏各页面
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (meizhiFmt != null) {
            transaction.hide(meizhiFmt);
        }
        if (gankHistoryFmt != null) {
            transaction.hide(gankHistoryFmt);
        }
        if (mineFmt != null) {
            transaction.hide(mineFmt);
        }
        
    }

    /**
     * 设置选中菜单
     * @param navigateType
     */
    public void setBottomNavigationBarSelected(int navigateType) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (navigateType) {
            case 0:
                hideFragments(transaction);
                if (null == meizhiFmt) {
                    meizhiFmt = new MeizhiFmt();
                    transaction.add(R.id.frameContent, meizhiFmt);
                } else {
                    transaction.show(meizhiFmt);
                }
                break;
            case 1:
                hideFragments(transaction);
                if (null == gankHistoryFmt) {
                    gankHistoryFmt = new GankHistoryFmt();
                    transaction.add(R.id.frameContent, gankHistoryFmt);
                } else {
                    transaction.show(gankHistoryFmt);
                }
                break;
            case 2:
                hideFragments(transaction);
                if (null == mineFmt) {
                    mineFmt = new MineFmt();
                    transaction.add(R.id.frameContent, mineFmt);
                } else {
                    transaction.show(mineFmt);
                }
                break;
        }
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        setBottomNavigationBarSelected(position);
        ActionBarUtil.setActionBarTitle(titleId[position],this);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onBackPressed() {
        long currentTick = System.currentTimeMillis();
        if (currentTick - lastBackKeyDownTick > DOUBLE_BACK_DURATION) {
            Toast.makeText(this,R.string.exit_tips,Toast.LENGTH_SHORT).show();
            lastBackKeyDownTick = currentTick;
        } else {
            finish();
            System.exit(0);
        }
    }
}
