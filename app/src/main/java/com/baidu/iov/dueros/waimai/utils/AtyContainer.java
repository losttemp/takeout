package com.baidu.iov.dueros.waimai.utils;

import android.app.Activity;

import com.baidu.iov.dueros.waimai.ui.WaiMaiApplication;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;
import com.baidu.xiaoduos.syncclient.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class AtyContainer {
    private AtyContainer() {
    }

    private static AtyContainer instance = new AtyContainer();
    private List<Activity> activityStack = new ArrayList<>();
    private boolean init = false;

    public static AtyContainer getInstance() {
        return instance;
    }

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        if (!init && activityStack.size() > 0 && activityStack.contains(aty)) {
            activityStack.remove(aty);
        }
        if (!WaiMaiApplication.START && activityStack.size() == 0) {
            Entry.getInstance().onEvent(Constant.EVENT_VOICE_EXIT, EventType.TOUCH_TYPE);
        }
    }

    public void finishAllActivity() {
        init = true;
        if (activityStack != null) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
            init = false;
        }
    }
}
