package com.leaf.gankio.ui.base;

import android.app.Activity;
import android.app.Application;
import android.util.DisplayMetrics;

import com.leaf.gankio.utils.ActivityManagerUtils;
import com.leaf.gankio.utils.Constants;
import com.leaf.gankio.utils.CrashHandler;
import com.leaf.gankio.utils.PreferenceManager;

import cn.bmob.v3.Bmob;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/1 10:07
 * @TODO ： ...
 */

public class GankIoApp extends Application {

    public static float density;
    public static float screenWidth;
    public static float screenHeight;
    public static int screenWidthDip;
    public static int screenHeightDip;
    public static GankIoApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this ;
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        Bmob.initialize(this, Constants.BMOB_KEY);
    }

    public static synchronized GankIoApp getInstance() {
        if (mInstance == null) {
            mInstance = new GankIoApp();
        }
        return mInstance;
    }

    public static void getDisplayDp(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        screenWidthDip = dm.widthPixels; // 屏幕宽（dip，如：320dip）
        screenHeightDip = dm.heightPixels; // 屏幕宽（dip，如：533dip）
        screenWidth = (int) (dm.widthPixels * density + 0.5f); // 屏幕宽（px，如：480px）
        screenHeight = (int) (dm.heightPixels * density + 0.5f); // 屏幕高（px，如：800px）
    }

    public static int dpToPx(int dpValue) {
        return (int) (dpValue * density + 0.5f);
    }

    public static void logout(){
        ActivityManagerUtils.getInstance().removeAllActivity();
        PreferenceManager.getInstance(mInstance).clear();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void exit(){
        ActivityManagerUtils.getInstance().removeAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
