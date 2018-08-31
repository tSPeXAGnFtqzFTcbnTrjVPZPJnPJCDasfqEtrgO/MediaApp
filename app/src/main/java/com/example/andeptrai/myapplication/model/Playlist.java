package com.example.andeptrai.myapplication.model;

import android.content.Context;

import com.example.andeptrai.myapplication.function.MusicPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class Playlist {
    private int mId,mCount;
    private String mName;
    private ArrayList<Song> songs = new ArrayList<>();

    HashMap<Long,Integer> markId = new HashMap<>();
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

    public void addSong(Song song){
        if(markId.containsKey(song.getId())){
            songs.set(markId.get(song.getId()),song );
        }else{
            markId.put(song.getId(),songs.size() );
            songs.add(song);
        }
    }
    public void addSongArray(Context context, ArrayList<Song> songs){
        long[] ids = new long[songs.size()];
        for(int i = 0;i<songs.size();i++){
            addSong(songs.get(i));
            ids[i]=songs.get(i).getId();

        }
        MusicPlayer.addToPlaylist(context,ids , mId);
    }
}
