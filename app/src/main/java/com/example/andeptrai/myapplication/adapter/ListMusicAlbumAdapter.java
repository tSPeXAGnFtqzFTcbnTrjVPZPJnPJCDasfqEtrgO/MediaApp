package com.example.andeptrai.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.andeptrai.myapplication.PlayerActivity;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.constant.Action;
import com.example.andeptrai.myapplication.function.GetSongName;
import com.example.andeptrai.myapplication.model.Song;
import com.example.andeptrai.myapplication.services.ForegroundService;
import com.example.andeptrai.myapplication.utils.SetListPlay;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMusicAlbumAdapter extends RecyclerView.Adapter<ListMusicAlbumAdapter.Holder>{

    ArrayList<Song> songs;
    Context context;
    int positionAlbum;
    public OnClickItem onClickItem;

    public interface OnClickItem{
        void onClick(View view, int position);
    }

    public ListMusicAlbumAdapter(ArrayList<Song> songs, int positionAlbum, Context context, OnClickItem onClickItem) {
        this.songs = songs;
        this.context = context;
        this.onClickItem = onClickItem;
        this.positionAlbum = positionAlbum;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_detail_album, parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.txtvName.setText(GetSongName.getSongName(songs.get(position)));
        holder.txtvArtist.setText(songs.get(position).getArtistName());

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtv_name)
        TextView txtvName;
        @BindView(R.id.txtv_artist)
        TextView txtvArtist;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView );

            itemView.setOnClickListener(v->{
                int position = getLayoutPosition();
                onClickItem.onClick(v,position );
                SetListPlay.playOneInAlbums(positionAlbum,position );

                Intent intentService = new Intent(context, ForegroundService.class);
                intentService.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                intentService.putExtra(ForegroundService.POS_KEY, 0);//new fix

                intentService.setAction(Action.START_FORE.getName());

                context.startService(intentService);

                Intent intent = new Intent(context, PlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        }
    }
}
