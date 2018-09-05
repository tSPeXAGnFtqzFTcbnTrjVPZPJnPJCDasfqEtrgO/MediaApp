package com.example.andeptrai.myapplication.loader;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.andeptrai.myapplication.model.Song;

import java.util.ArrayList;

public class AlbumSongLoader {
    public static ArrayList<Song> load(Context context, long albumId){

        ArrayList<Song> songs = new ArrayList<>();

        Cursor cursor = makeCursor(context,albumId);

        if(cursor == null){
            return null;
        }
        if(cursor.getCount() == 0){
            cursor.close();
            return null;
        }


        if(cursor.moveToFirst()){
            do{
                songs.add(new Song(cursor,songs.size()));
            }while (cursor.moveToNext());
        }

        return songs;

    }
    private static Cursor makeCursor(Context context,long albumId){

        String where = MediaStore.Audio.Media.ALBUM_ID + " =? ";
        String[] argsClause = {String.valueOf(albumId)};

        return context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                where,
                argsClause,
                null);
    }
}
