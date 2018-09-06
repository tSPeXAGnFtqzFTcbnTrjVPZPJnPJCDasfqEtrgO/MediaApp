package com.example.andeptrai.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.andeptrai.myapplication.adapter.ListMusicPlaylistAdapter;
import com.example.andeptrai.myapplication.constant.Action;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.loader.PlaylistSongLoader;
import com.example.andeptrai.myapplication.model.Song;
import com.example.andeptrai.myapplication.services.ForegroundService;
import com.example.andeptrai.myapplication.utils.AndtUtils;
import com.example.andeptrai.myapplication.utils.SetListPlay;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

public class DetailPlaylistActivity extends AppCompatActivity {

    public static final String keyPositionPlaylist = "keyPositionPlaylist";

    @BindView(R.id.btn_playall)
    TextView btnPlayAll;
    @BindView(R.id.recycle_song)
    RecyclerView recyclerSong;
    @BindView(R.id.bottom_menu)

    FrameLayout frameLayout;
    @BindView(R.id.btn_apply)
    ImageButton btnApply;
    @BindView(R.id.btn_remove)
    ImageButton btnRemove;
    @BindView(R.id.btn_edit)
    ImageButton btnEdit;
    @BindView(R.id.btn_add)
    ImageButton btnAdd;


    ListMusicPlaylistAdapter musicAdapter;

    ListMusicPlaylistAdapter.OnLongClickListener onLongClickListener;
    ListMusicPlaylistAdapter.OnClickListener onClickListener;

    ArrayList<Song> songs = new ArrayList<>();

    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_playlist);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent!=null){
            position = intent.getIntExtra(keyPositionPlaylist,0 );
        }

        init();
        action();
        setClick();
    }

    private void init() {

        btnAdd.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);

        onLongClickListener = (view, posion) -> {
            ShowLog.logVar("position long in frag", posion);
            showBottomMenu(true);
        };
        onClickListener = (view, position, numSelect, checkList) -> {

            songs.clear();


            for (int i = 0; i < Instance.playlists.get(this.position).getSongs().size(); i++) {
                if (checkList.get(i)) {
                    //songs.add(Instance.songList.get(i).getId());
                    songs.add(Instance.playlists.get(this.position).getSongs().get(i));
                }
            }

            ShowLog.logInfo("click fragment", position);
        };
        musicAdapter = new ListMusicPlaylistAdapter(Instance.playlists.get(position).getSongs(),position, DetailPlaylistActivity.this, onLongClickListener, onClickListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailPlaylistActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerSong.setLayoutManager(layoutManager);
        recyclerSong.setAdapter(musicAdapter);

        ShowLog.logInfo("detail pp", Instance.playlists.get(position).getSongs().size() );

        if (musicAdapter.getItemCount() < 0) {
            btnPlayAll.setVisibility(View.GONE);
        }


    }

    private void action() {

    }

    public void showBottomMenu(boolean show) {
        if (show) {
            frameLayout.setVisibility(View.VISIBLE);
        } else {
            frameLayout.setVisibility(View.GONE);
        }
    }

    private void setClick() {
        btnPlayAll.setOnClickListener(v -> {
            SetListPlay.playPlaylist(position);

            Intent intent = new Intent(DetailPlaylistActivity.this, ForegroundService.class);
            intent.putExtra(ForegroundService.POS_KEY, 0);
            intent.setAction(Action.START_FORE.getName());
            startService(intent);

            startActivity(new Intent(DetailPlaylistActivity.this, PlayerActivity.class));
        });

        btnApply.setOnClickListener(v -> {
            musicAdapter.callApply();
            showBottomMenu(false);
        });
        btnRemove.setOnClickListener(v -> {
            AlertDialog.Builder alBuilder = new AlertDialog.Builder(DetailPlaylistActivity.this);
            alBuilder.setMessage("Delete");
            alBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    asyncDeleteSong();
                }
            });
            alBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alBuilder.show();
        });
    }


    private void asyncDeleteSong() {
        long[] ids = new long[songs.size()];
        for (int i = 0; i < songs.size(); i++) {
            ids[i] = songs.get(i).getIdInPlaylist();
        }

        ShowLog.logInfo("prepare complete",null );

        Completable.fromCallable((Callable<Void>) () -> {
            AndtUtils.deleteSongPlaylist(getApplicationContext(),
                    Instance.playlists.get(position).getmId(),
                    ids,
                    position);
            return null;
        }).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                if (songs.size() == Instance.playlists.get(position).getSongs().size()) {

                } else {
                    Instance.playlists.get(position).pushFirstTime(
                            PlaylistSongLoader.getSongFromPlaylist(getApplicationContext(),
                                    Instance.playlists.get(position).getmId()));

                    ShowLog.logInfo("num song after del",Instance.playlists.get(position).getSongs().size() );
                    showBottomMenu(false);
                    musicAdapter.callApply();
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
