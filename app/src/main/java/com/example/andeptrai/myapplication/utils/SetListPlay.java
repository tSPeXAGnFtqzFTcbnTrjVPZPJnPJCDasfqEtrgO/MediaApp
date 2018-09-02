package com.example.andeptrai.myapplication.utils;

import com.example.andeptrai.myapplication.Instance;

import java.util.Collections;

public class SetListPlay {
    private static void clear(){
        Instance.songList.clear();
        Instance.songShuffleList.clear();
    }
    public static void playAll(){
        clear();

        Instance.songList.addAll(Instance.baseSong);
        Instance.songShuffleList.addAll(Instance.songList);

        Collections.shuffle(Instance.songShuffleList);
    }
    public static void playPlaylist(int position){
        clear();

        Instance.songList.addAll(Instance.playlists.get(position).getSongs());
        Instance.songShuffleList.addAll(Instance.songList);

        Collections.shuffle(Instance.songShuffleList);
    }
    public static void playOneInPlaylist(int posPlaylist,int position){
        clear();

        Instance.songList.add(Instance.playlists.get(posPlaylist).getSongs().get(position) );
        Instance.songShuffleList.addAll(Instance.songList);
    }
    public static void playOneInAll(int position){
        clear();

        Instance.songList.add(Instance.baseSong.get(position));
        Instance.songShuffleList.addAll(Instance.songList);
    }

}
