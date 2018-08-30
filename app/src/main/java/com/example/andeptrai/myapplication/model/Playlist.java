package com.example.andeptrai.myapplication.model;

public class Playlist {
    private int mId,mCount;
    private String mName;

    public Playlist(int mId, int mCount, String mName) {
        this.mId = mId;
        this.mCount = mCount;
        this.mName = mName;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
