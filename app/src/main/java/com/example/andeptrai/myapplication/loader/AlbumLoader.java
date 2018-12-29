package com.example.andeptrai.myapplication.loader;


import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.model.Album;
import com.example.andeptrai.myapplication.model.Song;

import java.util.ArrayList;

public class AlbumLoader {

    public static ArrayList<Album> load(Context context){
        

        ArrayList<Album> albums = new ArrayList<>();

        Cursor cursor = makeAlbumCursor(context);

        if(cursor == null) return albums;
        if(cursor.getCount() == 0){
            cursor.close();
            return albums;
        }

        if(cursor.moveToFirst()){
            do{
                long id,numSong;
                String name;
                String artist;
                String numSongForArtist;
                ArrayList<Song> songs;

                id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
                numSong = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
                artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));

                songs = AlbumSongLoader.load(context,id );

                ShowLog.logInfo("query album id", id );
                ShowLog.logInfo("query album name", name );
                ShowLog.logInfo("query album num of song", numSong );
                ShowLog.logInfo("query album artist", artist );

                albums.add(new Album(cursor,songs) );

            }while (cursor.moveToNext());
        }
        cursor.close();
        return albums;
    }
    private static Cursor makeAlbumCursor(Context context){
        return context.getContentResolver()
                .query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
    }
}
