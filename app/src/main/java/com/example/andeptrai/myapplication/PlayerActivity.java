package com.example.andeptrai.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bq.markerseekbar.MarkerSeekBar;
import com.example.andeptrai.myapplication.services.ForegroundService;
import com.example.andeptrai.myapplication.adapter.ViewPagerAdapter;
import com.example.andeptrai.myapplication.constant.Action;
import com.example.andeptrai.myapplication.constant.ActionBroadCast;
import com.example.andeptrai.myapplication.fragment.DiscFragment;
import com.example.andeptrai.myapplication.fragment.CurrentListMusicFragment;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.indicator.MyIndicatorView;
import com.example.andeptrai.myapplication.indicator.PageException;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivity extends AppCompatActivity {


    public static enum Repeat {
        NONE,
        REPEAT_ALL,
        REPEAT_ONE;

        public int getType() {
            return this.ordinal();
        }
        public String getName(){
            return this.name();
        }
    }

    public static final String UPDATE_SHUFFLE_KEY = "UPDATE_LIST_SHUFFLE";

    @BindView(R.id.txtv_name)
    TextView txtvName;
    @BindView(R.id.txtv_artist)
    TextView txtvArtist;
    @BindView(R.id.indicator)
    MyIndicatorView indicatorView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.seek_bar)
    MarkerSeekBar seekBar;
    @BindView(R.id.txtv_duration)
    TextView txtvDuration;
    @BindView(R.id.txtv_duration_total)
    TextView txtvDurationTotal;

    @BindView(R.id.btn_play)
    ImageButton btnPlay;
    @BindView(R.id.btn_prev)
    ImageButton btnPrev;
    @BindView(R.id.btn_next)
    ImageButton btnNext;
    @BindView(R.id.btn_shuffle)
    ImageButton btnShuffle;
    @BindView(R.id.btn_repeat)
    ImageButton btnRepeat;

    Intent playIntent, pauseIntent, prevIntent, nextIntent, startFore;
    Intent updateIntent;
    Intent shuffleIntent, repeatIntent;
    Intent intentUpdateListShuffleBroadcast;

    ArrayList<Fragment> fragments = new ArrayList<>();
    ViewPagerAdapter pagerAdapter;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    String nameSong = "";
    String nameArtist = "";
    int curTime = 0;
    int totalTime = 0;
    int pos = 0;
    boolean isRegister = false;
    boolean isPlaying = true;
    boolean isShuffle = false;
    boolean isRepeat = false;
    boolean isStop = false;

    int typeRepeat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        init();
        setupIntent();
        setUpBroadCast();
        setClick();

        action();

    }

    private void init() {
        txtvName.setSelected(true);
        txtvArtist.setSelected(true);


        fragments.add(new DiscFragment());
        fragments.add(new CurrentListMusicFragment());


        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        try {
            indicatorView.setViewPager(viewPager);
        } catch (PageException e) {
            Log.e("AAA", e.getMessage());
            e.printStackTrace();
        }
        //    tabLayout.setupWithViewPager(viewPager);
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }

    private void setUpBroadCast() {
        register();
    }

    private void action() {
        seekBar.setOnTouchListener((view, motionEvent) -> {
            unRegister();
            return false;
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar _seekBar, int i, boolean b) {
                seekBar.getMarkerTextView().setText(simpleDateFormat.format(_seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateIntent.putExtra(ForegroundService.UPDATE_PROGRESS, seekBar.getProgress());
                startService(updateIntent);

                register();

            }
        });
    }


    private void update(boolean wasKilled) {
        seekBar.setMax(totalTime);
        seekBar.setProgress(curTime);
        txtvDuration.setText(simpleDateFormat.format(curTime));
        txtvDurationTotal.setText(simpleDateFormat.format(totalTime));

        if (!txtvName.getText().toString().equals(nameSong)) {
            txtvName.setText(nameSong);
            txtvArtist.setText(nameArtist);
        }

        if (wasKilled) {
            if (isShuffle) {
                btnShuffle.setImageResource(R.drawable.ic_shuffle_selected);
            } else {
                btnShuffle.setImageResource(R.drawable.ic_shuffle_unselected);
            }
            if (isRepeat) {
                btnRepeat.setImageResource(R.drawable.ic_repeat_selected);
            } else {
                btnRepeat.setImageResource(R.drawable.ic_repeat_unselected);
            }
        }
    }

    private void setupIntent() {
        startFore = new Intent(this, ForegroundService.class);
        startFore.setAction(Action.START_FORE.getName());

        playIntent = new Intent(this, ForegroundService.class);
        playIntent.setAction(Action.PLAY.getName());

        pauseIntent = new Intent(this, ForegroundService.class);
        pauseIntent.setAction(Action.PAUSE.getName());


        prevIntent = new Intent(this, ForegroundService.class);
        prevIntent.setAction(Action.PREVIOUS.getName());

        nextIntent = new Intent(this, ForegroundService.class);
        nextIntent.setAction(Action.NEXT.getName());

        updateIntent = new Intent(PlayerActivity.this, ForegroundService.class);
        updateIntent.setAction(Action.UPDATE.getName());

        repeatIntent = new Intent(PlayerActivity.this, ForegroundService.class);
        repeatIntent.setAction(Action.REPEAT.getName());

        shuffleIntent = new Intent(PlayerActivity.this, ForegroundService.class);
        shuffleIntent.setAction(Action.SHUFFLE.getName());

        intentUpdateListShuffleBroadcast = new Intent();
        intentUpdateListShuffleBroadcast.setAction(ActionBroadCast.UPDATE_LIST_SHUFFLE.getName());

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() == null) {
                return;
            }
            String action = intent.getAction();

            isStop = false;

            if (action.equals(ActionBroadCast.CURSEEK.getName())) {
                pos = intent.getIntExtra(ForegroundService.SONG_ID, pos);
                totalTime = intent.getIntExtra(ForegroundService.TOTAL_TIME_KEY, totalTime);
                curTime = intent.getIntExtra(ForegroundService.CUR_TIME_KEY, curTime);
                nameSong = intent.getStringExtra(ForegroundService.NAME_SONG);
                nameArtist = intent.getStringExtra(ForegroundService.NAME_ARTIST);

                boolean prevShuffle = isShuffle;
                boolean prevRepeat = isRepeat;
                isShuffle = intent.getBooleanExtra(ForegroundService.SHUFFLE_KEY, isShuffle);
                isRepeat = intent.getBooleanExtra(ForegroundService.REPEAT_KEY, isRepeat);

                update(prevRepeat != isRepeat || prevShuffle != isShuffle);
            } else if (action.equals(ActionBroadCast.PLAY.getName())) {
                btnPlay.setImageResource(R.drawable.ic_pause);
                isPlaying = true;
            } else if (action.equals(ActionBroadCast.PAUSE.getName())) {
                ShowLog.logInfo("inner", "pause");
                btnPlay.setImageResource(R.drawable.ic_play);
                isPlaying = false;
            } else if (action.equals(ActionBroadCast.STOP.getName())) {
                btnPlay.setImageResource(R.drawable.ic_play);

                isStop = true;
                isPlaying = false;
            }
        }
    };

    private void register() {
        if (!isRegister) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ActionBroadCast.CURSEEK.getName());
            intentFilter.addAction(ActionBroadCast.NEXT.getName());
            intentFilter.addAction(ActionBroadCast.PREV.getName());
            intentFilter.addAction(ActionBroadCast.PLAY.getName());
            intentFilter.addAction(ActionBroadCast.PAUSE.getName());
            intentFilter.addAction(ActionBroadCast.STOP.getName());

            registerReceiver(broadcastReceiver, intentFilter);
            isRegister = true;
        }
    }

    private void unRegister() {
        if (isRegister) {
            unregisterReceiver(broadcastReceiver);
            isRegister = false;
        }
    }

    @Override
    protected void onStop() {
        unRegister();
        super.onStop();
    }

    private void setClick() {
        btnPlay.setOnClickListener(v -> {
            ShowLog.logInfo("inner click", "play");
            if (isPlaying) {
                startService(pauseIntent);
            } else {
                if (isStop) {
                    startFore.putExtra(ForegroundService.POS_KEY, pos);
                    startService(startFore);
                } else {
                    startService(playIntent);
                }
            }
            isPlaying = !isPlaying;
        });
        btnPrev.setOnClickListener(v -> {
            ShowLog.logInfo("inner click", "prev");
            startService(prevIntent);
        });
        btnNext.setOnClickListener(v -> {
            ShowLog.logInfo("inner click", "next");
            startService(nextIntent);
        });
        btnShuffle.setOnClickListener(v -> {
            isShuffle = !isShuffle;

            if (isShuffle) {
                btnShuffle.setImageResource(R.drawable.ic_shuffle_selected);
            } else {
                btnShuffle.setImageResource(R.drawable.ic_shuffle_unselected);
            }

            intentUpdateListShuffleBroadcast.putExtra(UPDATE_SHUFFLE_KEY, isShuffle);
            sendBroadcast(intentUpdateListShuffleBroadcast);

            shuffleIntent.putExtra(ForegroundService.SHUFFLE_KEY, isShuffle);
            startService(shuffleIntent);
        });
        btnRepeat.setOnClickListener(v -> {
            //isRepeat = !isRepeat;

            typeRepeat = (typeRepeat + 1) % 3;

            isRepeat = (typeRepeat != 0);

            if (isRepeat) {
                if (typeRepeat == Repeat.REPEAT_ALL.getType()) {
                    btnRepeat.setImageResource(R.drawable.ic_repeat_selected);
                } else {
                    btnRepeat.setImageResource(R.drawable.ic_repeat_one);
                }
            } else {
                btnRepeat.setImageResource(R.drawable.ic_repeat_unselected);
            }

            repeatIntent.putExtra(ForegroundService.REPEAT_KEY, typeRepeat);
            startService(repeatIntent);
        });
    }
}
