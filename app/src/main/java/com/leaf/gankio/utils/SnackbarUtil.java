package com.leaf.gankio.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.gson.JsonSyntaxException;
import com.leaf.gankio.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/3 21:23
 * @TODO ： 提示信息
 */

public class SnackbarUtil {

    public static Snackbar showSnackBar(View view, String message) {
        Snackbar snackbar=Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
        return snackbar;
    }

    /**
     * 检查异常
     * @param mContext
     * @param mThrowable
     * @param mRootView
     */
    public static void checkThrowableException(Context mContext, Throwable mThrowable, View mRootView) {
        String snack_action_to_setting = mContext.getString(R.string.action_settings);
        if ((mThrowable instanceof UnknownHostException)) {
            String snack_message_net_error = mContext.getString(R.string.snack_message_net_error);
            SnackbarUtil.showNetworkErrorSnackBar(mContext, mRootView, snack_message_net_error, snack_action_to_setting);
        } else if (mThrowable instanceof JsonSyntaxException) {
            String snack_message_data_error = mContext.getString(R.string.snack_message_data_error);
            SnackbarUtil.showNetworkErrorSnackBar(mContext, mRootView, snack_message_data_error, snack_action_to_setting);
        } else if (mThrowable instanceof SocketTimeoutException) {
            String snack_message_time_out = mContext.getString(R.string.snack_message_timeout_error);
            SnackbarUtil.showNetworkErrorSnackBar(mContext, mRootView, snack_message_time_out, snack_action_to_setting);
        } else if (mThrowable instanceof ConnectException) {
            String snack_message_net_error = mContext.getString(R.string.snack_message_net_error);
            SnackbarUtil.showNetworkErrorSnackBar(mContext, mRootView, snack_message_net_error, snack_action_to_setting);
        } else {
            String snack_message_unknown_error = mContext.getString(R.string.snack_message_unknown_error);
            SnackbarUtil.showSnackBar(mRootView,snack_message_unknown_error);
        }
    }

    /**
     * 根据错误吗检查异常
     * @param mContext
     * @param erroeCode
     * @param mRootView
     */
    public static void checkErrorCode(Context mContext, int erroeCode, View mRootView,String collect) {
        String snack_action_to_setting = mContext.getString(R.string.action_settings);
        if ((erroeCode == 9016)) {
            String snack_message_net_error = mContext.getString(R.string.snack_message_net_error);
            SnackbarUtil.showNetworkErrorSnackBar(mContext, mRootView, snack_message_net_error, snack_action_to_setting);
        }else if(erroeCode == 500){  // bmob 系统繁忙
            String snack_message_unknown_error = mContext.getString(R.string.snack_message_unknown_error);
            SnackbarUtil.showSnackBar(mRootView,snack_message_unknown_error);
        }else{
            if("collect".equals(collect)){
                String snack_message_collect_failure = mContext.getString(R.string.snack_message_collect_failure);
                SnackbarUtil.showSnackBar(mRootView,snack_message_collect_failure);
            }else if("uncollect".equals(collect)){
                String snack_message_uncollect_failure = mContext.getString(R.string.snack_message_uncollect_failure);
                SnackbarUtil.showSnackBar(mRootView,snack_message_uncollect_failure);
            }else{
                String snack_message_unknown_error = mContext.getString(R.string.snack_message_unknown_error);
                SnackbarUtil.showSnackBar(mRootView,snack_message_unknown_error);
            }

        }
    }

    /**
     * 弹出snackbar 提示错误 并添加点击事件 跳转到设置
     *
     * @param context
     * @param view
     * @param message
     * @param action
     */
    public static void showNetworkErrorSnackBar(final Context context, View view, String message, String action) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        context.startActivity(intent);
                    }
                })
                .show();

    }
}
