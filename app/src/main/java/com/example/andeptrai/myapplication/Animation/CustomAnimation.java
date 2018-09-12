package com.example.andeptrai.myapplication.Animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

public class CustomAnimation {
    static CustomInterpolator customInterpolator = CustomInterpolator.getInstance();

    AnimatorSet animatorSet;
    ObjectAnimator animatorRotate;
    public CustomAnimation(View v) {
        animatorRotate = ObjectAnimator.ofFloat(
                v, "rotation", 0, 360);
        animatorSet = new AnimatorSet();
    }

    public void stop(){
        //animatorRotate.setObjectValues(0);
        animatorSet.cancel();
    }
    public void setAnim(View v) {

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v,
                "scaleY", 0, 1);


        animatorRotate.setDuration(8000);
        animatorRotate.setInterpolator(customInterpolator.getInterpolatorLinear());
        animatorY.setDuration(500);
        animatorY.setInterpolator(customInterpolator.getInterpolatorCubic());

        animatorRotate.setRepeatCount(ValueAnimator.INFINITE);
        animatorRotate.setRepeatMode(ValueAnimator.RESTART);


        animatorSet.play(animatorRotate);//.with(animatorY);
        animatorSet.start();


    }
}
