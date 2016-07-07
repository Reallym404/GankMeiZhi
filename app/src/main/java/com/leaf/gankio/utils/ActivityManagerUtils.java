package com.leaf.gankio.utils;

import android.app.Activity;

import java.util.ArrayList;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/2 16:01
 * @TODO ：
 */
public class ActivityManagerUtils {

	private static ActivityManagerUtils activityManagerUtils;
    private ArrayList<Activity> activityList = new ArrayList<Activity>();

    private ActivityManagerUtils() {

    }

    public static ActivityManagerUtils getInstance() {
        if (null == activityManagerUtils) {
            activityManagerUtils = new ActivityManagerUtils();
        }
        return activityManagerUtils;
    }

    /**
     * 方法名：  getTopActivity	<br>
     * 方法描述：获取栈顶
     * 修改备注：<br>
     * 创建时间： 2015-12-31上午10:00:17<br>
     * @return
     */
    public Activity getTopActivity() {
        return activityList.get(activityList.size() - 1);
    }

    public void addActivity(Activity ac) {
        activityList.add(ac);
    }

    /**

     * 结束所有activity

     */
    public void removeAllActivity() {
        for (Activity ac : activityList) {
            if (null != ac) {
                if (!ac.isFinishing()) {
                    ac.finish();
                }
                ac = null;
            }
        }
        activityList.clear();
    }

}
