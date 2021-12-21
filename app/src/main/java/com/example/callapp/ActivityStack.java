package com.example.callapp;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Pattern
 */
public class ActivityStack {
    // volatile es una palabra reservada que indica que la variable puede ser modificada por
    // varios hilos a la vez
    private static volatile ActivityStack instance;
    private List<Activity> activities = new ArrayList<>();

    private ActivityStack() { }

    public static ActivityStack getInstance(){
        ActivityStack result  = instance;
        if (result != null) {
            return result;
        }
        synchronized (ActivityStack.class) {
            if (instance == null) {
                instance = new ActivityStack();
            }
            return instance;
        }
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void finishActivity(Class activityClass) {
        for (Activity activity : activities) {
            if (activity.getClass().equals(activityClass)) {
                finishActivity(activity);
            }
        }
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activities.remove(activity);
            activity.finish();
        }
    }
}
