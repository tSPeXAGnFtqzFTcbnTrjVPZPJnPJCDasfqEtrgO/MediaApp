package com.example.andeptrai.myapplication.Animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

public class CustomAnimation {
    static CustomInterpolator customInterpolator = CustomInterpolator.getInstance();

    public static void setAnim(View v) {
        AnimatorSet animatorSet = new AnimatorSet();


        ObjectAnimator animatorRotate = ObjectAnimator.ofFloat(
                v, "rotation", 0, 360);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v,
                "scaleY",0,1);


        animatorRotate.setDuration(500);
        animatorRotate.setInterpolator(customInterpolator.getInterpolatorLinear());
        animatorY.setDuration(500);
        animatorY.setInterpolator(customInterpolator.getInterpolatorCubic());

        animatorRotate.setRepeatCount(ValueAnimator.INFINITE);
        animatorRotate.setRepeatMode(ValueAnimator.RESTART);
        animatorSet.play(animatorRotate);//.with(animatorY);
        animatorSet.start();

    }
}
