package com.example.andeptrai.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.model.Playlist;

import java.util.ArrayList;

public class PlaylistAdapter extends  RecyclerView.Adapter<PlaylistAdapter.Holder>{

    ArrayList<Playlist> playlists;
    Context context;

    ItemClick itemClick;

    public interface ItemClick{
        void onClick(View view,int position);
    }

    public PlaylistAdapter(ArrayList<Playlist> playlists, Context context,ItemClick itemClick) {
        this.playlists = playlists;
        this.context = context;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_dialog_playlist,parent,false );

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.txtvName.setText(playlists.get(position).getmName());
        ShowLog.logInfo("name playlist adapter",playlists.get(position).getmName());
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView txtvName;
        public Holder(View itemView) {
            super(itemView);
            txtvName = itemView.findViewById(R.id.name_playlist);

            itemView.setOnClickListener(view -> itemClick.onClick(view,getLayoutPosition()));
        }
    }
}
