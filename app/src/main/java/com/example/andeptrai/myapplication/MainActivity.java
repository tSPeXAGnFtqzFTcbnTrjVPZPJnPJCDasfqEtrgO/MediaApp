package com.example.andeptrai.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.andeptrai.myapplication.model.Song;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

public class MainActivity extends AppCompatActivity {

    public static String PACKAGE_NAME;
    private final int REQUEST_PERMISSION_READ_EXTERNAL = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getPackageName();

        prepareInstance();
        action();
    }

    private void action(){
        Intent intent = new Intent(MainActivity.this,PlayerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void prepareInstance() {
        Instance.songList.clear();

        getListMp3(Type.INTERNAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int check = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            if (check == PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_READ_EXTERNAL);
            }
        } else {
            getListMp3(Type.EXTERNAL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_READ_EXTERNAL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getListMp3(Type.EXTERNAL);
                }
                break;
            }
        }
    }


    private void getListMp3(Type type) {

        getObservableListMp3()
                .subscribeOn(Schedulers.io())
                .map(o -> {
                    Log.d("AAA", "thread" + Thread.currentThread().getName());
                    if (type.getName().equals(Type.EXTERNAL.getName())) {
                        return ScanFileMp3.queryFileExternal(getApplicationContext());
                    } else{
                        return ScanFileMp3.queryFileInternal(getApplicationContext());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Song>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Song> songs) {
                        if(songs!=null) {
                            Instance.songList.addAll(songs);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private Observable<Object> getObservableListMp3() {
        return Observable.defer(() -> Observable.just(1));
    }
}
