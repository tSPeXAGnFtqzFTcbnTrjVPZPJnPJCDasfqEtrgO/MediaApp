package com.example.andeptrai.myapplication.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

public class AndtUtils {
    public static int countSongInPlaylist(Context context,long playlistId){
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external",playlistId);

        Cursor cursor = context.getContentResolver().query(uri,
                new String[]{BaseColumns._ID},
                MediaStore.Audio.Media.IS_MUSIC + "=1",
                null,
                null);

        int count = 0;
        if(cursor!=null && cursor.moveToFirst()){
            count =  cursor.getCount();
            cursor.close();
        }
        return count;
    }

}
