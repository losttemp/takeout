package com.baidu.iov.dueros.waimai.utils;

import android.app.Activity;

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
