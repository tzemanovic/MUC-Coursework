package com.tzemanovic.muccoursework;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas Zemanovic on 12/12/2014.
 */
public class App {

    // keeps references to all running activities
    private static List<Activity> activities;

    public static void addActivity(Activity activity) {
        if (activities == null) {
            activities = new ArrayList<Activity>();
        }
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    // when user wants to quit app finish all running activities
    public static void quit()
    {
        for (Activity activity : activities) {
            activity.finish();
        }
    }
}
