package com.example.andeptrai.myapplication.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Dot {
    private Paint mPaint;
    private float mCurRadius;
    private PointF mCenter;

    Dot(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mCenter = new PointF();

    }

    public void setColor(int color){
        mPaint.setColor(color);
    }
    public void setAlpha(int alpha){
        mPaint.setAlpha(alpha);
    }
    public void setCenter(float x,float y){
        mCenter.set(x,y);
    }

    public float getCurRadius() {
        return mCurRadius;
    }

    public void setCurRadius(float mCurRadius) {
        this.mCurRadius = mCurRadius;

    }

    public Paint getmPaint() {
        return mPaint;
    }

    public PointF getmCenter() {
        return mCenter;
    }

    public void draw(Canvas canvas){

        canvas.drawCircle(mCenter.x,mCenter.y ,mCurRadius ,mPaint );

    }

}
