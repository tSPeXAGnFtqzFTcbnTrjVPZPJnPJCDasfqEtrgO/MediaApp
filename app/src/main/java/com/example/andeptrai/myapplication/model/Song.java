package com.example.andeptrai.myapplication.model;

import android.database.Cursor;
import android.provider.MediaStore;

import com.example.andeptrai.myapplication.function.ConvertLanguage;

import java.io.Serializable;

public class Song implements Serializable {
    private String nameVi,path,artistName,albumName,nameEn;
    private String nameSearch;
    private long id,artistId,albumId;
    private int position;

    public Song(Cursor cursor,int pos) {
        position = pos;
        nameVi = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        nameVi = nameVi.trim();
        nameEn  = ConvertLanguage.convert(nameVi);
        nameSearch = nameEn.replaceAll(" ","" );

        id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));

        path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        artistName = cursor.getString(cursor
                .getColumnIndex(MediaStore.Audio.Media.ARTIST));
        artistId = cursor.getInt(cursor
                .getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
        albumName = cursor.getString(cursor
                .getColumnIndex(MediaStore.Audio.Media.ALBUM));
        albumId = cursor.getInt(cursor
                .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

    }

    public String getNameVi() {
        return nameVi;
    }

    public String getPath() {
        return path;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public long getId() {
        return id;
    }

    public long getArtistId() {
        return artistId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameSearch() {
        return nameSearch;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
