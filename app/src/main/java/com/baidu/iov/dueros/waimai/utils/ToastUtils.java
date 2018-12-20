package com.baidu.iov.dueros.waimai.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;


public class ToastUtils {

    private static Toast result;

    public static void show(Context context, CharSequence text, int duration) {
        if (result == null) {
            result = Toast.makeText(context.getApplicationContext(), text, duration);
        }
        TextView tv = result.getView().findViewById(Resources.getSystem().getIdentifier("message", "id", "android"));
        tv.setText(text);
//        tv.setBackground(context.getDrawable(R.drawable.toast_bg));
        tv.setTextSize(px2sp(context,36));
        result.setDuration(duration);
        result.setGravity(Gravity.CENTER, 0, 0);
        result.show();
    }

    public static int px2sp(Context context, int pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
