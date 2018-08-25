package com.example.andeptrai.myapplication.function;

import com.example.andeptrai.myapplication.model.Song;

import java.util.Locale;

public class GetSongName {
    public static String getSongName(Song song){
        if(Locale.getDefault().getLanguage().equals("vi")){
            return song.getNameVi();
        }else {
            return song.getNameEn();
        }
    }
}
