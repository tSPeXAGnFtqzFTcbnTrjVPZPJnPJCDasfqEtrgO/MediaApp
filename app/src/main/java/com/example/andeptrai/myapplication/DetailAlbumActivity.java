package com.example.andeptrai.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.andeptrai.myapplication.adapter.ListMusicAlbumAdapter;
import com.example.andeptrai.myapplication.constant.Action;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.services.ForegroundService;
import com.example.andeptrai.myapplication.utils.SetListPlay;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailAlbumActivity extends AppCompatActivity {

    public static final String keyPositionAlbum = "keyPositionAlbum";

    @BindView(R.id.btn_playall)
    TextView btnPlayAll;
    @BindView(R.id.recycle_song)
    RecyclerView recyclerSong;

    ListMusicAlbumAdapter listMusicAlbumAdapter;
    ListMusicAlbumAdapter.OnClickItem onClickItem;
    int posAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_album);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            posAlbum = intent.getIntExtra(keyPositionAlbum, 0);
        }

        setClick();
        init();

    }

    private void init() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listMusicAlbumAdapter = new ListMusicAlbumAdapter(
                Instance.albums.get(posAlbum).getSongs(), posAlbum,
                getApplicationContext(), onClickItem);

        recyclerSong.setLayoutManager(layoutManager);
        recyclerSong.setAdapter(listMusicAlbumAdapter);

        ShowLog.logInfo("num song album " + Instance.albums.get(posAlbum).getmName(),
                listMusicAlbumAdapter.getItemCount() );
    }

    private void setClick(){
        onClickItem = new ListMusicAlbumAdapter.OnClickItem() {
            @Override
            public void onClick(View view, int position) {

            }
        };
        btnPlayAll.setOnClickListener(v->{
            SetListPlay.playAllInAlbum(posAlbum);
            Intent intent = new Intent(DetailAlbumActivity.this, ForegroundService.class);
            intent.putExtra(ForegroundService.POS_KEY, 0);
            intent.setAction(Action.START_FORE.getName());
            startService(intent);

            startActivity(new Intent(DetailAlbumActivity.this, PlayerActivity.class));
        });
    }
}
