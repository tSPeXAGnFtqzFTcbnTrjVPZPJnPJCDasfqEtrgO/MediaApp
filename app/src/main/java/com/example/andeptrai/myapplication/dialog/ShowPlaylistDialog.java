package com.example.andeptrai.myapplication.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.andeptrai.myapplication.Instance;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.adapter.PlaylistAdapter;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.model.Playlist;
import com.example.andeptrai.myapplication.model.Song;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowPlaylistDialog extends DialogFragment {

    @BindView(R.id.recycle_playlist)
    RecyclerView recyclePlaylist;

    PlaylistAdapter playlistAdapter;
    ArrayList<Playlist> playlists = new ArrayList<>();

    PlaylistAdapter.ItemClick itemClick;

    private static final String keySongId = "key_song";
    private static final String keySongArr = "key_song_arr";

    long[] ids;
    private ArrayList<Song> mSongs;


    public static ShowPlaylistDialog newInstance(ArrayList<Song> songs){
        ShowPlaylistDialog dialog = new ShowPlaylistDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable(keySongArr,songs );
        dialog.setArguments(bundle);

        return dialog;

    }
    public static void newInstance(Song song) {

        long[] id = new long[1];
        id[0] = song.getId();
        newInstance(id);
    }

    public static ShowPlaylistDialog newInstance(long[] id) {
        ShowPlaylistDialog dialog = new ShowPlaylistDialog();

        Bundle bundle = new Bundle();
        bundle.putLongArray(keySongId, id);
        dialog.setArguments(bundle);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ShowLog.logInfo("onCreateView dl", null);
        View view = inflater.inflate(R.layout.layout_dialog_playlist, container,false);
        ButterKnife.bind(this, view);

        init();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if(dialog!=null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        }
    }

    private void init(){
        //ids = getArguments().getLongArray(keySongId);
        //int id = getArguments().getInt(keySongId);
        mSongs = (ArrayList<Song>) getArguments().getSerializable(keySongArr);
        setItemClick();


        ShowLog.logInfo("show pp dialog",Instance.playlists.size() );
        playlists.addAll(Instance.playlists);
        playlists.add(0,new Playlist(-1, 0, "New Playlist"));
        playlistAdapter = new PlaylistAdapter(playlists, getContext(), itemClick);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclePlaylist.setLayoutManager(layoutManager);
        recyclePlaylist.setAdapter(playlistAdapter);

    }

    private void setItemClick() {
        itemClick = (view, position) -> {
            ShowLog.logInfo("adapter pp click",position );
            if (position == 0) {
                ShowAddPlaylistDialog.newInstance(mSongs)
                        .show(getActivity().getSupportFragmentManager(),"add Playlist" );
            } else {
                Instance.playlists.get(position-1).addSongArray(getContext(),mSongs);
            }
            getDialog().dismiss();
        };
    }
}
