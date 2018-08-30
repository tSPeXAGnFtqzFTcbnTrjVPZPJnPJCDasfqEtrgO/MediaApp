package com.example.andeptrai.myapplication.function;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MusicPlayer {


    public static void addToPlaylist(Context context, long[] ids, long playlistId) {
        int size = ids.length;
        int base = 0;
        ContentValues[] values = new ContentValues[size];

        String projection = "max(" + MediaStore.Audio.Playlists.Members.PLAY_ORDER + ")";
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri,
                    new String[]{projection},
                    null,
                    null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                base = cursor.getInt(0) + 1;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        for(int i=0;i<size;i++){
            values[i]=new ContentValues();
            values[i].put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, base + i);
            values[i].put(MediaStore.Audio.Playlists.Members.AUDIO_ID,ids[i]);

            ShowLog.logInfo("putting",ids[i] );
        }
        context.getContentResolver().bulkInsert(uri,values );
    }

    public static long createPlaylist(Context context, String name) {

        if (name != null && name.length() > 0) {
            String where = MediaStore.Audio.Playlists.NAME + " = ? ";
            String[] args = {name};

            Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                    null, where, args, null);

            if (cursor == null)  {
                return -1;
            }

            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues(1);
                values.put(MediaStore.Audio.Playlists.NAME, name);
                Uri uri = context.getContentResolver().insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                        values);
                return Long.parseLong(uri.getLastPathSegment());

            }
        }
        return -1;

    }
}
