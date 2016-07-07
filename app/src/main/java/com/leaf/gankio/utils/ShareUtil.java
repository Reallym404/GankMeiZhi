package com.leaf.gankio.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.leaf.gankio.R;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/17 12:26
 * @TODO ： 调用系统分享
 */

public class ShareUtil {

    public static void shareImage(Context context, Uri uri, String title) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(intent, title));
    }


    public static void share(Context context, String extraText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.action_share));
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(
                Intent.createChooser(intent, context.getString(R.string.action_share)));
    }
}
