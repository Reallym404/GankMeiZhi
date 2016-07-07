package com.leaf.gankio.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.leaf.gankio.R;
import com.leaf.gankio.ui.base.BaseFragment;
import com.leaf.gankio.ui.mine.setting.MineSetting;
import com.leaf.gankio.utils.GlideBuilderTool;
import com.leaf.gankio.utils.PreferenceManager;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.functions.Action1;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/2 15:24
 * @TODO ： 我的
 */

public class MineFmt extends BaseFragment {

    @Bind(R.id.mine_user_icon)
    //CircleImageView mineUserIcon;
    ImageView mineUserIcon;
    @Bind(R.id.mine_nick)
    TextView mineNick;
    @Bind(R.id.mine_desc)
    TextView mineDesc;
    @Bind(R.id.mine_user_arrow)
    ImageView mineUserArrow;
    @Bind(R.id.mine_info_lay)
    RelativeLayout mineInfoLay;
    @Bind(R.id.mine_like_lay)
    RelativeLayout mineLikeLay;
    @Bind(R.id.mine_collect_lay)
    RelativeLayout mineCollectLay;
    @Bind(R.id.mine_setting_lay)
    RelativeLayout mineSettingLay;

    @Override
    protected int setLayoutResource() {
        return R.layout.fmt_mine;
    }

    @Override
    protected void initView() {
        final String avatar = PreferenceManager.getInstance(activity).getAvatarLarge();
        String screenName = PreferenceManager.getInstance(activity).getScreenName();
        String description = PreferenceManager.getInstance(activity).getDescription();
        GlideBuilderTool.getInstance().loadCircleAvatar(activity, avatar, mineUserIcon);
        mineNick.setText(screenName);
        mineDesc.setText(description == null ? getResources().getString(R.string.no_description_tips) : description);

        RxView.clicks(mineCollectLay).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(activity,MineCollect.class) ;
                        startActivity(intent);
                    }
                }) ;

        RxView.clicks(mineLikeLay).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(activity,MineMeizhi.class) ;
                        startActivity(intent);
                    }
                }) ;

        RxView.clicks(mineSettingLay).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(activity,MineSetting.class) ;
                        startActivity(intent);
                    }
                }) ;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(false);
    }


}
