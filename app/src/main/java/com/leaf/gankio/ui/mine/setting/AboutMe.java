package com.leaf.gankio.ui.mine.setting;

import android.view.MenuItem;

import com.leaf.gankio.R;
import com.leaf.gankio.ui.base.BaseActivity;
import com.leaf.gankio.utils.ActionBarUtil;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/19 20:54
 * @TODO ： ...
 */

public class AboutMe extends BaseActivity {
    @Override
    protected int setLayoutResource() {
        return R.layout.activity_about_me;
    }

    @Override
    protected void initActionBar() {
        ActionBarUtil.initLeftBackActionBar(R.string.setting_aboutme_title,this);
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


}
