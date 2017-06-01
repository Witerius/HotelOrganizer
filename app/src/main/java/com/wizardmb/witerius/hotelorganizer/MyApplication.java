package com.wizardmb.witerius.hotelorganizer;

import android.app.Application;

/**
 * Created by User on 29.03.2016.
 */
public final class MyApplication extends Application {

    private static boolean activityVisible;

   /* public static boolean isActivityVisible() {
        return activityVisible;
    }*/

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }
}
