package com.tzemanovic.muccoursework.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

/**
 * Created by Tomas Zemanovic on 11/12/2014.
 *
 * contains pairs of preferences keys and default values
 */
public class Preferences {
    public static final Pair<String, Integer> region = Pair.create("region", 0);

    public static int getInt(SharedPreferences sharedPreferences, Pair<String, Integer> keyAndDefVal) {
        return sharedPreferences.getInt(keyAndDefVal.first, keyAndDefVal.second);
    }

    public static int getInt(Context context, Pair<String, Integer> keyAndDefVal) {
        return getInt(PreferenceManager.getDefaultSharedPreferences(context), keyAndDefVal);
    }

    public static void putInt(SharedPreferences.Editor editor, Pair<String, Integer> keyAndDefVal, int value) {
        editor.putInt(keyAndDefVal.first, value);
        editor.commit();
    }
}
