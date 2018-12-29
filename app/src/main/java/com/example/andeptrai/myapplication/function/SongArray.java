package com.example.andeptrai.myapplication.function;

import com.example.andeptrai.myapplication.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SongArray {
    public static void sort(ArrayList<Song> songList){
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getNameEn().compareTo(t1.getNameEn());
            }
        });
    }
}
