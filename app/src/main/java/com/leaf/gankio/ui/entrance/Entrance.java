package com.leaf.gankio.ui.entrance;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.leaf.gankio.R;
import com.leaf.gankio.ui.base.BaseActivity;
import com.leaf.gankio.ui.login.AuthLogin;
import com.leaf.gankio.ui.main.MainActivity;
import com.leaf.gankio.utils.PreferenceManager;

import butterknife.Bind;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/3 21:47
 * @TODO ： 启动页
 */

public class Entrance extends BaseActivity {

    public String TAG = "Entrance";
    @Bind(R.id.img_entrance)
    ImageView imgEntrance;
    private boolean isLogin = false;

    @Override
    protected void initView() {
        isLogin = PreferenceManager.getInstance(mContext).getIsLogin();
        if (isLogin) {
            isLogin = !PreferenceManager.getInstance(mContext).checkReLoginTime();
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.entrance_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isLogin) {
                    launch(Entrance.this, Intent.FLAG_ACTIVITY_CLEAR_TOP, MainActivity.class);
                } else {
                    launch(Entrance.this, Intent.FLAG_ACTIVITY_CLEAR_TOP, AuthLogin.class);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imgEntrance.startAnimation(animation);
    }

    @Override
    protected int setLayoutResource() {
        return R.layout.activity_entrance;
    }


}
