package com.example.andeptrai.myapplication.indicator;

import android.support.v4.view.ViewPager;

public interface IndicatorInterface {
    void setViewPager(ViewPager pager) throws PageException;

    void setRadiusSelect(int radius);

    void setRadiusUnselect(int radius);

    void setDistanceDot(int distance);

    void setAnimateDuration(long duration);


}
