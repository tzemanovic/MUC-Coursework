package com.tzemanovic.muccoursework.helper;

import android.app.Activity;
import android.graphics.Typeface;

/**
 * Created by Tomas Zemanovic on 30/11/2014.
 */
public class FontLoader {

    private static Typeface constantiaFont;

    public static Typeface constantia(Activity activity) {
        if (constantiaFont == null) {
            constantiaFont = Typeface.createFromAsset(activity.getAssets(), "Constantia.ttf");
        }
        return constantiaFont;
    }
}
