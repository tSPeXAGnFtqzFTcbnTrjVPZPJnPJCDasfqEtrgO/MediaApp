package com.example.andeptrai.myapplication.adapter.helper;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int from, int to);
    void onItemDismiss(int position);
}
