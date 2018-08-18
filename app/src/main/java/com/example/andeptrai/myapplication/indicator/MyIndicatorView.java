package com.example.andeptrai.myapplication.indicator;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.function.ShowLog;


public class MyIndicatorView extends View implements IndicatorInterface, ViewPager.OnPageChangeListener {

    private final long DEFAULT_DURATION_ANIMATION = 200;
    private final float DEFAULT_RADIUS_SELECT = 20;
    private final float DEFAULT_RADIUS_UNSELECT = 10;
    private final int DEFAULT_DISTANCE_DOT = 40;

    private ViewPager pager;
    private Dot[] dots;

    private long mAnimateDuration = DEFAULT_DURATION_ANIMATION;
    private float mRadiusSelect = DEFAULT_RADIUS_SELECT;
    private float mRadiusUnselect = DEFAULT_RADIUS_UNSELECT;
    private int mDistanceDot = DEFAULT_DISTANCE_DOT;

    private int mColorSelect;
    private int mColorUnselect;
    private int mCurrentPosition;
    private int mBeforePosition;

    private ValueAnimator mAnimatorZoomIn;
    private ValueAnimator mAnimatorZoomOut;

    private int length = -1;


    public MyIndicatorView(Context context) {
        super(context);
    }

    public MyIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        ShowLog.logInfo("AAA", "cons 2" + length);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyIndicatorView,0,0);

        try{
        mRadiusSelect = typedArray.getFloat(R.styleable.MyIndicatorView_indi_radius_select, DEFAULT_RADIUS_SELECT);
        mRadiusUnselect = typedArray.getFloat(R.styleable.MyIndicatorView_indi_radius_unselect, DEFAULT_RADIUS_UNSELECT);
        mDistanceDot = typedArray.getDimensionPixelSize(R.styleable.MyIndicatorView_indi_distance, DEFAULT_DISTANCE_DOT);
        mColorSelect = typedArray.getColor(R.styleable.MyIndicatorView_indi_color_select, Color.parseColor("#3640E1"));
        mColorUnselect = typedArray.getColor(R.styleable.MyIndicatorView_indi_color_unselect, Color.parseColor("#FFFFFF"));

        }finally {
            typedArray.recycle();
        }



    }

    public MyIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ShowLog.logInfo("AAA", "cons 3" + length);


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("AAA", "on pageselect" + length);
        mBeforePosition = mCurrentPosition;
        mCurrentPosition = position;

        if (mBeforePosition == mCurrentPosition) {
            mBeforePosition = (mCurrentPosition + 1) % dots.length;
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mAnimateDuration);

        mAnimatorZoomIn = ValueAnimator.ofFloat(mRadiusUnselect, mRadiusSelect);
        mAnimatorZoomIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int positionPerform = mCurrentPosition;

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float newRadius = (float) valueAnimator.getAnimatedValue();
                changeNewRadius(positionPerform, newRadius);

            }
        });

        mAnimatorZoomOut = ValueAnimator.ofFloat(mRadiusSelect, mRadiusUnselect);
        mAnimatorZoomOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int positionPerform = mBeforePosition;

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float newRadius = (float) valueAnimator.getAnimatedValue();
                changeNewRadius(positionPerform, newRadius);

            }
        });

        animatorSet.play(mAnimatorZoomIn).with(mAnimatorZoomOut);
        animatorSet.start();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Log.d("AAA", "on layout" + length);

        float yCenter = getHeight() / 2;

        float d = mDistanceDot + 2 * mRadiusUnselect;

        float firstDot = (getWidth() / 2) - (dots.length - 1) * d / 2;

        for (int i = 0; i < dots.length; i++) {
            dots[i].setCenter(i == 0 ? firstDot : firstDot + d * i, yCenter);
            dots[i].setCurRadius(i == mCurrentPosition ? mRadiusSelect : mRadiusUnselect);
            dots[i].setColor(i == mCurrentPosition ? mColorSelect : mColorUnselect);
            dots[i].setAlpha(i == mCurrentPosition ? 255 : (int)(mRadiusUnselect * 255 / mRadiusSelect));
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d("AAA", "on mesure" + length);

        float desertHeight = 2 * mRadiusSelect;

        int width, height;

        int widMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widMode == MeasureSpec.AT_MOST) {
            width = widthSize;
        } else {
            width = 0;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min((int)desertHeight, heightSize);
        } else {
            height = (int)desertHeight;
        }

        Log.d("AAA", "on mesure" + width+" " +height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.d("AAA", "onDraw" + length);

        if (length != -1) {
            Log.d("AAA", "on Draw");
            for (Dot dot : dots) {
                dot.draw(canvas);
            }
        } else {
            Log.d("AAA", "dot null");
        }
    }

    @Override
    public void setViewPager(ViewPager pager) throws PageException {
        Log.d("AAA", "on setview page" + length);
        this.pager = pager;
        pager.addOnPageChangeListener(this);
        initDot(pager.getAdapter().getCount());
        onPageSelected(0);

        Log.d("AAA", "on setview page" + length);

    }

    private void initDot(int count) throws PageException {
        if (count < 2) throw new PageException();

        length = count;

        dots = new Dot[count];
        for (int i = 0; i < count; i++) {
            dots[i] = new Dot();
        }
        Log.d("AAA", "length" + length);
    }

    @Override
    public void setAnimateDuration(long duration) {
        this.mAnimateDuration = duration;
    }
    @Override
    public void setRadiusSelect(int radius) {
        this.mRadiusSelect = radius;
    }

    @Override
    public void setRadiusUnselect(int radius) {
        this.mRadiusUnselect = radius;
    }

    @Override
    public void setDistanceDot(int distance) {
        this.mDistanceDot = distance;
    }

    private void changeNewRadius(int positionPerform, float radius) {
        if (dots[positionPerform].getCurRadius() != radius) {
            dots[positionPerform].setCurRadius(radius);
            dots[positionPerform].setAlpha((int)(radius * 255 / mRadiusSelect));

            invalidate();//re-draw => call onDraw of class View
        }
    }
}
