package com.example.andeptrai.myapplication.model;

import android.database.Cursor;
import android.provider.MediaStore;

import com.example.andeptrai.myapplication.function.ConvertLanguage;

public class Song {
    private String nameVi,path,artistName,albumName,nameEn;
    private String nameSearch;
    private int id,artistId,albumId;

    public Song(Cursor cursor) {
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

    public int getId() {
        return id;
    }

    public int getArtistId() {
        return artistId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameSearch() {
        return nameSearch;
    }

}
