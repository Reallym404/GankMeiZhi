package com.leaf.gankio.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.leaf.gankio.R;
import com.leaf.gankio.ui.base.BaseFragment;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/2 15:23
 * @TODO ： 分类
 */

public class ClassifyFmt extends BaseFragment {
    @Override
    protected int setLayoutResource() {
        return R.layout.fmt_classify;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
