package com.example.andeptrai.myapplication;

import android.graphics.Bitmap;

import com.example.andeptrai.myapplication.model.Album;
import com.example.andeptrai.myapplication.model.Playlist;
import com.example.andeptrai.myapplication.model.Song;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Instance {
    public static ArrayList<Song> baseSong = new ArrayList<>();
    public static ArrayList<Song> songList = new ArrayList<>();
    public static ArrayList<Song> songShuffleList = new ArrayList<>();
    public static ArrayList<Playlist> playlists = new ArrayList<>();
    public static ArrayList<Album> albums = new ArrayList<>();
    public static HashMap<Long,Bitmap> mapImageAlbum = new HashMap<>();
}
