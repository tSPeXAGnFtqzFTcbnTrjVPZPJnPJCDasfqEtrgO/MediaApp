package com.example.andeptrai.myapplication.function;

import android.content.Context;
import android.util.Log;

public class ShowLog {
    private static boolean isDebugging = true;
    private static String TAG = "AAA";

    public static void logFunc(Context context) {
        if (isDebugging) {
            Log.i(TAG, context.getClass().getSimpleName() + "-" +
                    context.getClass().getDeclaredMethods()[0].getName());
        }

    }

    public static void logVar(String name, Object o) {
        if (isDebugging) {
            Log.d(TAG, name + ": " + o);
        }
    }

    public static void logInfo(String key, Object o) {
        if (isDebugging) {
            Log.i(TAG, key + ": " + o);
        }
    }
}
