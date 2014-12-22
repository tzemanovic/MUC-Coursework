package com.tzemanovic.muccoursework.helper;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Tomas Zemanovic on 30/11/2014.
 */
public class FontLoader {

    private static Typeface constantiaFont;

    public static Typeface constantia(Context context) {
        if (constantiaFont == null) {
            constantiaFont = Typeface.createFromAsset(context.getAssets(), "Constantia.ttf");
        }
        return constantiaFont;
    }
}
