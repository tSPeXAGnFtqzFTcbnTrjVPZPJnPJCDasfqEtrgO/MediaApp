package com.example.andeptrai.myapplication;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.andeptrai.myapplication.adapter.ListMusicAdapter;
import com.example.andeptrai.myapplication.adapter.SongAdapter;
import com.example.andeptrai.myapplication.adapter.ViewPagerAdapter;
import com.example.andeptrai.myapplication.fragment.ListMusicFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMusicActivity extends AppCompatActivity {
    @BindView(R.id.tab_view)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager pager;



    ArrayList<Fragment> fragments = new ArrayList<>();

    ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        fragments.add(new ListMusicFragment());
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);

    }

}
