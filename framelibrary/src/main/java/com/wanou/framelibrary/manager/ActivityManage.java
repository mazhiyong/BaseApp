package com.wanou.framelibrary.manager;


import android.app.Activity;

import com.wanou.framelibrary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wodx521
 * @date on 2018/8/10
 */
public class ActivityManage {
    private static List<Activity> activities = new ArrayList<>();
    private static ActivityManage activityManage;

    private ActivityManage() {
    }

    public static ActivityManage getInstance() {
        if (activityManage == null) {
            activityManage = new ActivityManage();
        }
        return activityManage;
    }


    public void addActivity(Activity activity) {

       LogUtils.d(activity.getLocalClassName());
       activities.add(activity);
    }

    public void removeActivity(Activity activity) {
       LogUtils.e(activity.getLocalClassName());
        activities.remove(activity);
    }

    public void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }

    public void finishActivity(Activity activity) {
        if (activity != null && activities.contains(activity)) {
            activities.remove(activity);
            activity.finish();
        }
    }


}


