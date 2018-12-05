package com.baidu.iov.dueros.waimai.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;


public class ToastUtils {

    private static Toast result;

    public static void show(Context context, CharSequence text, int duration) {
        if (result == null) {
            result = Toast.makeText(context, text, duration);
        }
        TextView tv = result.getView().findViewById(Resources.getSystem().getIdentifier("message", "id", "android"));
        tv.setText(text);
        tv.setBackground(context.getDrawable(R.drawable.toast_bg));
        tv.setTextSize(12);
        result.setDuration(duration);
        result.show();
    }


}
