package com.example.andeptrai.myapplication.model;

import android.content.Context;

import com.example.andeptrai.myapplication.Instance;
import com.example.andeptrai.myapplication.function.MusicPlayer;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.loader.PlaylistLoader;
import com.example.andeptrai.myapplication.loader.PlaylistSongLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class Playlist {

    private long mId;
    private int mCount;
    private String mName;
    private ArrayList<Song> songs = new ArrayList<>();
    private long totalDuration = 0;
    HashMap<Long,Integer> markId = new HashMap<>();

    public Playlist(long mId, int mCount, String mName) {
        this.mId = mId;
        this.mCount = mCount;
        this.mName = mName;
    }
    public Playlist(long mId, int mCount, String mName,long duration) {
        this.mId = mId;
        this.mCount = mCount;
        this.mName = mName;
        totalDuration = duration;
    }

    public long getmId() {
        return mId;
    }


    public int getmCount() {
        return mCount;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void addSong(Context context,Song song){
        if(markId.containsKey(song.getId())){
            songs.set(markId.get(song.getId()),song );
        }else{
            markId.put(song.getId(),songs.size() );
            songs.add(song);
            totalDuration+=song.getDuration();
            mCount++;

            long[] ids = new long[1];
            ids[0] = song.getId();

            MusicPlayer.addToPlaylist(context,ids , mId);

        }
    }
    public void addSongArray(Context context,ArrayList<Song> songs){
        //long[] ids = new long[songs.size()];
        for(int i = 0;i<songs.size();i++){
            addSong(context,songs.get(i));
          //ssss  ids[i]=songs.get(i).getId();
        }

    }


    public void pushFirstTime(ArrayList<Song> songs) {
        totalDuration = mCount = 0;
        for (Song song : songs) {
            markId.put(song.getId(), songs.size());
            this.songs.add(song);
            totalDuration += song.getDuration();
            mCount++;
        }
    }

}
