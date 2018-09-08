package com.example.andeptrai.myapplication.function;

public class ConvertTime {
    public static String getTimeByMiliseconds(long milisec){
        long h = 0;
        long m = 0;
        long s = 0;
        s = milisec/1000;
        m = s/60;
        s%=60;
        h = m/60;
        m%=60;

        String ans = "";
        if(h>0){
            ans = ans.concat(String.valueOf(h));
            ans= ans.concat(":");
        }

        if(m<10){
            ans = ans.concat("0");
        }
        ans = ans.concat(String.valueOf(m));

        ans= ans.concat(":");
        if(s<10){
            ans = ans.concat("0");
        }
        ans = ans.concat(String.valueOf(s));

        return ans;
    }
}
