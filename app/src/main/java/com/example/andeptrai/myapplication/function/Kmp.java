package com.example.andeptrai.myapplication.function;

import android.util.Log;

public class Kmp {
    private static int[] kMap(String s) {
        s=s.toLowerCase();

        int j = 0;
        int[] k = new int[s.length()];
        for (int i = 1; i < s.length(); i++) {
            while (j > 0 && s.charAt(i) != s.charAt(j)) {
                j = k[j - 1];
            }
            if (s.charAt(j) == s.charAt(j)) j++;
            k[i] = j;

        }
        return k;
    }

    public static boolean isMatch(String s, String pattern) {
        s=s.toLowerCase();
        pattern = pattern.toLowerCase();

        int[] k = kMap(pattern);
        int j = 0;
        for (int i = 0; i < s.length(); i++) {
            while (j > 0 && s.charAt(i) != pattern.charAt(j)) {
                j = k[j - 1];
            }
            if (s.charAt(i) == pattern.charAt(j)) j++;
            if (j == pattern.length()) {
                Log.d("AAA","success: "+ s+"_ " + pattern );
                return true;
            }

        }
        Log.d("AAA","fail: "+ s+"_ " + pattern );
        return false;

    }
}
