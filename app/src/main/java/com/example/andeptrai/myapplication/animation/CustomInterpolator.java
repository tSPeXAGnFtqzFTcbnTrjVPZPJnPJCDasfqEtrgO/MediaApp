package com.example.andeptrai.myapplication.animation;

import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.animation.Interpolator;

public class CustomInterpolator {

    private static CustomInterpolator customInterpolator;


    private float[] PRINCIPLE_DEFAULT_EASE = {.25f,.1f,.25f,1f};
    private float[] EASE_OUT = {0f,0f,.58f,1f};
    private float[] EASE_IN = {.42f,0f,1f,1f};
    private float[] LINEAR = {0f,0f,1f,1f};
    private float[] cubic = {.2f,1.22f,.58f,.03f};

    public static CustomInterpolator getInstance(){
        if(customInterpolator == null){
            customInterpolator = new CustomInterpolator();
        }
        return customInterpolator;
    }

    public Interpolator getInterpolatorCubic(){
        return PathInterpolatorCompat.create(cubic[0],
                cubic[1],
                cubic[2],
                cubic[3]);
    }
    public Interpolator getInterpolatorLinear(){
        return PathInterpolatorCompat.create(LINEAR[0],
                LINEAR[1],
                LINEAR[2],
                LINEAR[3]);
    }

}
