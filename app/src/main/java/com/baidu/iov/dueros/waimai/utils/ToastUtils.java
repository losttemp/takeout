package com.baidu.iov.dueros.waimai.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class ToastUtils {

    private static Toast toast;

    public static void show(Context context, CharSequence text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        }
        TextView tv = toast.getView().findViewById(Resources.getSystem().getIdentifier("message", "id", "android"));
        tv.setText(text);
//        tv.setBackground(context.getDrawable(R.drawable.toast_bg));
        tv.setTextSize(px2sp(context, 36));
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static int px2sp(Context context, int pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static void customTime(Context context, CharSequence text, int duration) {
        //控制toast显示的时间
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        }
        TextView tv = toast.getView().findViewById(Resources.getSystem().getIdentifier("message", "id", "android"));
        tv.setText(text);
        tv.setTextSize(px2sp(context, 36));
        showMyToast(duration);
    }

    public static void showMyToast(final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.show();
                }
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                }
                timer.cancel();
            }
        }, cnt);
    }
}
