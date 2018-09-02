package com.example.andeptrai.myapplication.loader;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;


import com.example.andeptrai.myapplication.model.Playlist;
import com.example.andeptrai.myapplication.utils.AndtUtils;

import java.util.ArrayList;

public class PlaylistLoader {


    public static ArrayList<Playlist> load(Context context){
        ArrayList<Playlist> playlists = new ArrayList<>();
        Cursor cursor = makeCursor(context);

        if(cursor!=null && cursor.moveToFirst()){
            do{
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                int count = AndtUtils.countSongInPlaylist(context,id );

                long duration = AndtUtils.getDurationPlaylist(context,id );
                Playlist playlist = new Playlist(id,count,name,duration);

                playlist.pushFirstTime(PlaylistSongLoader.getSongFromPlaylist(context,id ));
                playlists.add(playlist);
            }while (cursor.moveToNext());
            cursor.close();
        }

        return playlists;
    }

    private static Cursor makeCursor(Context context){
        return context.getContentResolver().query(
                MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                new String[]{BaseColumns._ID,
                        MediaStore.Audio.PlaylistsColumns.NAME},
                null,null,null );
    }
}
