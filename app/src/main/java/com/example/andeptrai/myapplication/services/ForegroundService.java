package com.example.andeptrai.myapplication.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.andeptrai.myapplication.Instance;
import com.example.andeptrai.myapplication.MainActivity;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.constant.Action;
import com.example.andeptrai.myapplication.constant.ActionBroadCast;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.model.Song;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ForegroundService extends Service {

    public final static String POS_KEY = "abc";
    public final static String CUR_TIME_KEY = "CUR_TIME_KEY";
    public final static String TOTAL_TIME_KEY = "TOTAL_TIME_KEY";
    public final static String NAME_SONG = "NAME_SONG";
    public final static String NAME_ARTIST = "NAME_ARTIST";
    public final static String SONG_ID = "SONG_ID";
    public final static String SHUFFLE_KEY = "SHUFFLE_KEY";
    public final static String REPEAT_KEY = "REPEAT_KEY";
    public final static String UPDATE_PROGRESS = "UPDATE_PROGRESS";
    public final static String PAUSE = "PAUSE";
    public final static String PLAY = "PLAY";
    public final static String STOP = "STOP";


    public static final int REQUEST_CODE = 123;
    private final String CHANNEL_ID = "111";
    private final int FORE_ID = 321;
    private static int pos;

    private boolean isShuffle = false;
    private boolean isRepeat = false;

    public boolean isStarting = false;

    Disposable disposable;
    MediaPlayer mediaPlayer;
    Song currentSong;

    RemoteViews remoteViews = new RemoteViews(MainActivity.PACKAGE_NAME, R.layout.content_notification);

    Notification notifiCustom;
    Intent noificationMainIntent, playIntent, pauseIntent, stopIntent, prevIntent, nextIntent, updateProgressIntent;
    Intent repeatIntent, shuffleIntent;
    PendingIntent mainPending, playPending, pausePending, stopPending, prevPending, nextPending;

    NotificationManager mNotificationManager;

    Intent intentUpdateBroadcast, intentPauseBroadcast, intentPlayBroadcast, intentStopBroadcast, intentNextBroadcast, intentPrevBroadcast;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("AAA", "create");
        isStarting = true;
        setupIntent();
        updateTime();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("AAA", "start");

        try {
            if (intent.getAction() == null) {
                Log.d("AAA", "Intent null");
                return START_NOT_STICKY;
            }
        } catch (NullPointerException e) {
            return START_NOT_STICKY;
        }

        String action = intent.getAction();
        pos = intent.getIntExtra(POS_KEY, pos);
        ShowLog.logInfo("fore pos:  ", pos);

        ShowLog.logInfo("action", action);

        if (action.compareTo(Action.START_FORE.getName()) == 0) {


            play(pos);
            startForeground(FORE_ID, notifiCustom);


        } else if (action.equals(Action.PLAY.getName())) {
            Log.d("AAA", "play");

            remoteViews.setImageViewResource(R.id.notifi_play, R.drawable.ic_pause);
            remoteViews.setOnClickPendingIntent(R.id.notifi_play, pausePending);

            mNotificationManager.notify(FORE_ID, notifiCustom);
            resume();

            sendBroadcast(intentPlayBroadcast);


        } else if (action.equals(Action.PAUSE.getName())) {
            Log.d("AAA", "pause");
            remoteViews.setImageViewResource(R.id.notifi_play, R.drawable.ic_play);

            remoteViews.setOnClickPendingIntent(R.id.notifi_play, playPending);

            mNotificationManager.notify(FORE_ID, notifiCustom);
            pause();

            sendBroadcast(intentPauseBroadcast);

        } else if (action.equals(Action.STOP.getName())) {
            Log.d("AAA", "stop");

            stop();

            sendBroadcast(intentStopBroadcast);
            disposable.dispose();
            stopSelf();


        } else if (action.equals(Action.NEXT.getName())) {
            Log.d("AAA", "NEXT");
            pos = (pos + 1) % Instance.songList.size();
            play(pos);

            //sendBroadcast(intentNextBroadcast);


        } else if (action.equals(Action.PREVIOUS.getName())) {
            Log.d("AAA", "PREV");
            if (pos == 0) {
                pos = Instance.songList.size();
            }
            pos--;
            play(pos);

            //sendBroadcast(intentPrevBroadcast);

        } else if (action.equals(Action.UPDATE.getName())) {
            ShowLog.logInfo("get update", intent.getIntExtra(UPDATE_PROGRESS, -1));
            if (mediaPlayer != null) {
                int progress = intent.getIntExtra(UPDATE_PROGRESS, mediaPlayer.getCurrentPosition());
                mediaPlayer.seekTo(progress);
            }
        } else if (action.equals(Action.REPEAT.getName())) {
            isRepeat = intent.getBooleanExtra(REPEAT_KEY, isRepeat);
            ShowLog.logInfo("repeat", isRepeat);
        } else if (action.equals(Action.SHUFFLE.getName())) {
            isShuffle = intent.getBooleanExtra(SHUFFLE_KEY, isShuffle);
            ShowLog.logInfo("shuffle", isShuffle);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("AAA", "Des");
        super.onDestroy();
    }

    private void play(int pos) {
        play(pos, false);
    }

    private void play(int pos, boolean isEnd) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

        }


        ShowLog.logInfo("path", isShuffle ? Instance.songShuffleList.get(pos).getPath() :
                Instance.songList.get(pos).getPath());
        int t = pos;
        do {


            if (isEnd && isRepeat) {//repeat if end of song (false when event next-prev-start
                if (pos == 0) {
                    pos = Instance.songList.size();
                }
                pos--;
            }

            if (isShuffle) {
                currentSong = Instance.songShuffleList.get(pos);
            } else {
                currentSong = Instance.songList.get(pos);

            }
            mediaPlayer = MediaPlayer.create(this, Uri.parse(currentSong.getPath()));
            if (mediaPlayer != null) {
                break;
            }

            pos = (pos + 1) % Instance.songList.size();


            ShowLog.logInfo("for mp", mediaPlayer);
        } while (t != pos);
        if (mediaPlayer != null) {
            ShowLog.logInfo("fore", currentSong.getNameVi());

            remoteViews.setTextViewText(R.id.notifi_title, currentSong.getNameVi());


            mNotificationManager.notify(FORE_ID, notifiCustom);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    ForegroundService.pos = (ForegroundService.pos + 1) % Instance.songList.size();
                    play(ForegroundService.pos, true);

                }
            });

            sendBroadcast(intentPlayBroadcast);
        }
    }

    private void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

    private void setupIntent() {

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        noificationMainIntent = new Intent(this, MainActivity.class);
        noificationMainIntent.setAction(Action.MAIN.getName());
        noificationMainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mainPending = PendingIntent.getActivity(this, REQUEST_CODE, noificationMainIntent, 0);

        playIntent = new Intent(this, ForegroundService.class);
        playIntent.setAction(Action.PLAY.getName());
        playPending = PendingIntent.getService(this, REQUEST_CODE, playIntent, 0);


        pauseIntent = new Intent(this, ForegroundService.class);
        pauseIntent.setAction(Action.PAUSE.getName());
        pausePending = PendingIntent.getService(this, REQUEST_CODE, pauseIntent, 0);

        stopIntent = new Intent(this, ForegroundService.class);
        stopIntent.setAction(Action.STOP.getName());
        stopPending = PendingIntent.getService(this, REQUEST_CODE, stopIntent, 0);

        prevIntent = new Intent(this, ForegroundService.class);
        prevIntent.setAction(Action.PREVIOUS.getName());
        prevPending = PendingIntent.getService(this, REQUEST_CODE, prevIntent, 0);

        nextIntent = new Intent(this, ForegroundService.class);
        nextIntent.setAction(Action.NEXT.getName());
        nextPending = PendingIntent.getService(this, REQUEST_CODE, nextIntent, 0);

        updateProgressIntent = new Intent(this, ForegroundService.class);
        updateProgressIntent.setAction(Action.UPDATE.getName());
        //updateProgressPending = PendingIntent.getService(this, REQUEST_CODE, updateProgressIntent, 0);

        repeatIntent = new Intent(this, ForegroundService.class);
        repeatIntent.setAction(Action.REPEAT.getName());
        //repeatPending = PendingIntent.getService(this, REQUEST_CODE, repeatIntent, 0);

        shuffleIntent = new Intent(this, ForegroundService.class);
        shuffleIntent.setAction(Action.SHUFFLE.getName());
        //shufflePending = PendingIntent.getService(this,REQUEST_CODE,shuffleIntent ,0 );

        NotificationChannel channel;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_HIGH);

            mNotificationManager.createNotificationChannel(channel);
        }

        notifiCustom = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)
                .setContentIntent(mainPending)
                .build();

        notifiCustom.bigContentView = remoteViews;

        remoteViews.setOnClickPendingIntent(R.id.notifi_next, nextPending);
        remoteViews.setOnClickPendingIntent(R.id.notifi_prev, prevPending);
        remoteViews.setOnClickPendingIntent(R.id.notifi_play, pausePending);
        remoteViews.setOnClickPendingIntent(R.id.notifi_stop, stopPending);


        intentUpdateBroadcast = new Intent();
        intentUpdateBroadcast.setAction(ActionBroadCast.CURSEEK.getName());

        intentStopBroadcast = new Intent();
        intentStopBroadcast.setAction(ActionBroadCast.STOP.getName());

        intentPauseBroadcast = new Intent();
        intentPauseBroadcast.setAction(ActionBroadCast.PAUSE.getName());

        intentPlayBroadcast = new Intent();
        intentPlayBroadcast.setAction(ActionBroadCast.PLAY.getName());

        intentNextBroadcast = new Intent();
        intentNextBroadcast.setAction(ActionBroadCast.NEXT.getName());

        intentPrevBroadcast = new Intent();
        intentPrevBroadcast.setAction(ActionBroadCast.PREV.getName());
    }


    private void updateTime() {

        disposable = io.reactivex.Observable.interval(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(lLong -> {
                    if (mediaPlayer != null) {
                        intentUpdateBroadcast.putExtra(REPEAT_KEY, isRepeat);
                        intentUpdateBroadcast.putExtra(SHUFFLE_KEY, isShuffle);
                        intentUpdateBroadcast.putExtra(SONG_ID, pos);
                        intentUpdateBroadcast.putExtra(NAME_SONG, currentSong.getNameVi());
                        intentUpdateBroadcast.putExtra(NAME_ARTIST, currentSong.getArtistName());
                        try {
                            intentUpdateBroadcast.putExtra(CUR_TIME_KEY, mediaPlayer.getCurrentPosition());
                            intentUpdateBroadcast.putExtra(TOTAL_TIME_KEY, mediaPlayer.getDuration());
                        } catch (IllegalStateException e) {
                            ShowLog.logInfo("error", e.getMessage());
                        }
                        sendBroadcast(intentUpdateBroadcast);
                    }
                });
    }

}