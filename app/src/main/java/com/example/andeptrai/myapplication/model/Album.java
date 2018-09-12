package com.example.andeptrai.myapplication.model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.example.andeptrai.myapplication.Instance;

import java.util.ArrayList;

public class Album {
    long mId,mNumSong;
    String mName;
    String pathImage;
    Bitmap bitmapAlbum;
    ArrayList<Song> songs = new ArrayList<>();

    public Album(Cursor cursor,ArrayList<Song> songs){
        mId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
        mName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
        mNumSong = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
        pathImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));

        bitmapAlbum = BitmapFactory.decodeFile(pathImage);

        this.songs.addAll(songs);


        if(bitmapAlbum!=null) {
            Instance.mapImageAlbum.put(mId, bitmapAlbum);
        }
    }

    public String getPathImage() {
        return pathImage;
    }

    public Bitmap getBitmapAlbum() {
        return bitmapAlbum;
    }

    public long getmId() {
        return mId;
    }

    public long getmNumSong() {
        return mNumSong;
    }

    public String getmName() {
        return mName;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
}
