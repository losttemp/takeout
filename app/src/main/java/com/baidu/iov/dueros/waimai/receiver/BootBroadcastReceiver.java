package com.baidu.iov.dueros.waimai.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.iov.dueros.waimai.utils.CacheUtils;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            CacheUtils.saveAddrTime(0);
        }
    }
}
