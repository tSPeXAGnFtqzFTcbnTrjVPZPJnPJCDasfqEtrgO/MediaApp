package com.example.andeptrai.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andeptrai.myapplication.DetailAlbumActivity;
import com.example.andeptrai.myapplication.Instance;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.adapter.AlbumFragmentAdapter;
import com.example.andeptrai.myapplication.function.ShowLog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumFragment extends Fragment{

    @BindView(R.id.recycle_album)
    RecyclerView recyleAlbum;

    AlbumFragmentAdapter albumAdapter;
    Context context;

    AlbumFragmentAdapter.OnClickItem onClickItem;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_album_fragment, container, false);

        ButterKnife.bind(this, view);

        init();
        return view;
    }

    private void init(){
        onClickItem = (view, position) -> {
            Intent intent= new Intent(getContext(), DetailAlbumActivity.class);
            intent.putExtra(DetailAlbumActivity.keyPositionAlbum,position );
            startActivity(intent);

        };

        albumAdapter = new AlbumFragmentAdapter(getContext(), Instance.albums,onClickItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyleAlbum.setLayoutManager(layoutManager);
        recyleAlbum.setAdapter(albumAdapter);
        ShowLog.logInfo("album fm",albumAdapter.getItemCount() );
    }
}
