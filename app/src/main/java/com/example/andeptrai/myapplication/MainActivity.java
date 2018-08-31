package com.example.andeptrai.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.loader.PlaylistLoader;
import com.example.andeptrai.myapplication.loader.PlaylistSongLoader;
import com.example.andeptrai.myapplication.model.Playlist;
import com.example.andeptrai.myapplication.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

import static com.example.andeptrai.myapplication.Instance.playlists;

enum Type {
    EXTERNAL(),
    INTERNAL();

    String name;

    Type() {
        name = this.name();
    }

    public String getName() {
        return name;
    }
}

class DataZip {
    public ArrayList<Song> songs,songs1;
    public ArrayList<Playlist> playlists;

    public DataZip(ArrayList<Song> songs, ArrayList<Song> songs1, ArrayList<Playlist> playlists) {
        this.songs = songs;
        this.songs1 = songs1;
        this.playlists = playlists;
    }
}
public class MainActivity extends AppCompatActivity {

    public static String PACKAGE_NAME;
    private final int REQUEST_PERMISSION_READ_EXTERNAL = 123;
    private boolean readableExternal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getPackageName();

        prepareInstance();
        getAll();

        //action();
    }

    private void action(){
        Intent intent = new Intent(MainActivity.this,ListMusicActivity.class);
        //Intent intent = new Intent(MainActivity.this,PlayerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void getAll(){

        Observable
                .zip(getObservableListMp3(Type.EXTERNAL), getObservableListMp3(Type.INTERNAL), getObservablePlaylist(),
                        (Function3<ArrayList<Song>, ArrayList<Song>, ArrayList<Playlist>, Object>)
                                (songs, songs2, playlists) -> new DataZip(songs,songs2,playlists))
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        ShowLog.logInfo("onnext",null );
                        if(o instanceof DataZip){
                            DataZip dataZip = (DataZip) o;
                            if(dataZip.songs!=null) {
                                Instance.songList.addAll(dataZip.songs);
                            }
                            if(dataZip.songs1!=null) {
                                Instance.songList.addAll(dataZip.songs1);
                            }
                            Instance.songShuffleList.addAll(Instance.songList);
                            Collections.shuffle(Instance.songShuffleList);

                            playlists.clear();
                            playlists.addAll(dataZip.playlists);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                            action();
                    }
                });

    }
    private void prepareInstance() {
        Instance.songList.clear();

        //getListMp3(Type.INTERNAL);
        //getPlaylist();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            readableExternal = false;
            int check = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            if (check == PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_READ_EXTERNAL);
            }
        } else {
            //getListMp3(Type.EXTERNAL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_READ_EXTERNAL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readableExternal = true;
                    //getListMp3(Type.EXTERNAL);
                }
                break;
            }
        }
    }
//
//
//    private void getListMp3(Type type) {
//
//        getObservableListMp3()
//                .subscribeOn(Schedulers.io())
//                .map(o -> {
//                    Log.d("AAA", "thread" + Thread.currentThread().getName());
//                    if (type.getName().equals(Type.EXTERNAL.getName())) {
//                        return ScanFileMp3.queryFileExternal(getApplicationContext());
//                    } else{
//                        return ScanFileMp3.queryFileInternal(getApplicationContext());
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Song>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<Song> songs) {
//                        if(songs!=null) {
//                            Instance.songList.addAll(songs);
//                            Instance.songShuffleList.addAll(songs);
//                            Collections.shuffle(Instance.songShuffleList);
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//
//    }
//    private void getPlaylist(){
//        getObservablePlaylist()
//                .subscribeOn(Schedulers.io())
//                .map(o-> loadPlaylist())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ArrayList<Playlist>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(ArrayList<Playlist> playlists) {
//                        Instance.playlists.clear();
//                        Instance.playlists.addAll(playlists);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//
//    }
    private ArrayList<Playlist> loadPlaylist() {
        ArrayList<Playlist> playlists = PlaylistLoader.load(getApplicationContext());
        if (playlists != null) {
            for (Playlist playlist : playlists) {
                ArrayList<Song> songs = PlaylistSongLoader.getSongFromPlaylist(getApplicationContext(), playlist.getmId());
                ShowLog.logInfo("size " + playlist.getmName(), playlist.getmCount());
                for (Song song : songs) {
                    ShowLog.logInfo(playlist.getmName(), song.getNameVi());
                }
            }
        } else {
            ShowLog.logInfo("playlist", "null");
        }
        return playlists;
    }



    private Observable<ArrayList<Song>> getObservableListMp3(Type type) {

        if (type.getName().equals(Type.INTERNAL.getName())) {
            return Observable.just(ScanFileMp3.queryFileExternal(getApplicationContext()));
            //return Observable.defer(() -> Observable.just(ScanFileMp3.queryFileExternal(getApplicationContext())));
        } else{
            if(readableExternal) {
                return Observable.just(ScanFileMp3.queryFileInternal(getApplicationContext()));
                //return Observable.defer(() -> Observable.just(ScanFileMp3.queryFileInternal(getApplicationContext())));
            }
            else return Observable.defer(()->Observable.just(null)) ;
        }
//        return Observable.defer(() -> Observable.just());
    }
    private Observable<ArrayList<Playlist>> getObservablePlaylist(){
        return Observable.just(loadPlaylist());
        //return Observable.defer(()->Observable.just(loadPlaylist()));
    }
}
