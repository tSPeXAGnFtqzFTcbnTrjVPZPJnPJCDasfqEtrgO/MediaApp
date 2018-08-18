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
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bq.markerseekbar.MarkerSeekBar;
import com.example.andeptrai.myapplication.Services.ForegroundService;
import com.example.andeptrai.myapplication.adapter.ViewPagerAdapter;
import com.example.andeptrai.myapplication.constant.Action;
import com.example.andeptrai.myapplication.constant.ActionBroadCast;
import com.example.andeptrai.myapplication.fragment.Fragment1;
import com.example.andeptrai.myapplication.fragment.Fragment2;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.indicator.MyIndicatorView;
import com.example.andeptrai.myapplication.indicator.PageException;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivity extends AppCompatActivity {

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


    ArrayList<Fragment> fragments = new ArrayList<>();
    ViewPagerAdapter pagerAdapter;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    String nameSong = "";
    int curTime = 0;
    int totalTime = 0;
    boolean isRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        init();
        setUpBroadCast();
        action();

    }

    private void init() {

        fragments.add(new Fragment1());
        fragments.add(new Fragment2());

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
    private void setUpBroadCast(){
        register();
    }
    private void action(){
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                unRegister();
                return false;
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar _seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent intent = new Intent(PlayerActivity.this,ForegroundService.class);
                intent.setAction(Action.UPDATE.toString());
                intent.putExtra(ForegroundService.updateProgress,seekBar.getProgress() );
                startService(intent);

                register();

            }
        });
    }


    private void update(){
        seekBar.setMax(totalTime);
        seekBar.setProgress(curTime);
        txtvDuration.setText(simpleDateFormat.format(curTime));
        txtvDurationTotal.setText(simpleDateFormat.format(totalTime ));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction()!=null && intent.getAction().equals(ActionBroadCast.CURSEEK.getName())){
                totalTime = intent.getIntExtra(ForegroundService.totalTimeKey,totalTime );
                curTime = intent.getIntExtra(ForegroundService.curTimeKey,curTime );
                nameSong  = intent.getStringExtra(ForegroundService.nameSong);
                update();
            }
        }
    };

    private void register(){
        if(!isRegister){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ActionBroadCast.CURSEEK.getName());
            registerReceiver(broadcastReceiver,intentFilter );
            isRegister=true;
        }
    }
    private void unRegister(){
        if(isRegister){
            unregisterReceiver(broadcastReceiver);
            isRegister = false;
        }
    }

    @Override
    protected void onStop() {
        unRegister();
        super.onStop();
    }

}
