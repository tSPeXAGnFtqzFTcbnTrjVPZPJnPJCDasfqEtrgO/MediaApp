package com.example.andeptrai.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andeptrai.myapplication.Instance;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.adapter.PlaylistFragmentAdapter;
import com.example.andeptrai.myapplication.dialog.ShowRenamePlaylistDialog;
import com.example.andeptrai.myapplication.function.ShowLog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayListFragment extends Fragment {

    @BindView(R.id.recycle_playlist)
    RecyclerView recyclerPlaylist;

    PlaylistFragmentAdapter playlistFragmentAdapter;
    PlaylistFragmentAdapter.OnClickItem onClickItem;
    PlaylistFragmentAdapter.OnClickMenu onClickMenu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_playlist_fragment,container,false );
        ButterKnife.bind(this,view );

        setClickInterface();
        init();

        return view;
    }

    private void init(){

        ShowLog.logInfo("size pp", Instance.playlists.size() );
        playlistFragmentAdapter = new PlaylistFragmentAdapter(Instance.playlists,getContext(),onClickItem,onClickMenu);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerPlaylist.setLayoutManager(layoutManager);
        recyclerPlaylist.setAdapter(playlistFragmentAdapter);

    }
    private void setClickInterface() {
        onClickItem = (view, position) -> {

        };

        onClickMenu = (menuId, playlistId,position) -> {
            switch (menuId){
                case R.id.menu_play:{
                    ShowLog.logInfo("menu click","play" );
                    break;
                }
                case R.id.menu_remove:{
                    ShowLog.logInfo("menu click","remove" );
                    break;
                }
                case R.id.menu_rename:{

                    ShowRenamePlaylistDialog.newInstance(playlistId)
                            .setOnComplete(newName -> {
                                Instance.playlists.get(position).setmName(newName);
                                playlistFragmentAdapter.notifyDataSetChanged();
                            })
                            .show(getActivity().getSupportFragmentManager(),"s" );
                    ShowLog.logInfo("menu click","rename" );
                    break;
                }
            }
        };
    }


}
