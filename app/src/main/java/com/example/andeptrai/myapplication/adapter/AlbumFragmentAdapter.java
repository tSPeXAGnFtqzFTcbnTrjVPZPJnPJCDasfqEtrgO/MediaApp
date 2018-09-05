package com.example.andeptrai.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.model.Album;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumFragmentAdapter extends RecyclerView.Adapter<AlbumFragmentAdapter.Holder>{

    Context context;
    ArrayList<Album> albums;

    OnClickItem onClickItem;

    public interface OnClickItem {
        void onClick(View view,int position);
    }

    public AlbumFragmentAdapter(Context context, ArrayList<Album> albums, OnClickItem onClickItem) {
        this.context = context;
        this.albums = albums;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_fragment_album,parent,false );
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.imgAlbum.setImageBitmap(albums.get(position).getBitmapAlbum());
        holder.txtvName.setText(albums.get(position).getmName());
        holder.txtvAmountSong.setText("Total: " + albums.get(position).getmNumSong());

        ShowLog.logInfo("binding",albums.get(position).getmName() );
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.img_album)
        ImageView imgAlbum;
        @BindView(R.id.txtv_name)
        TextView txtvName;
        @BindView(R.id.txtv_amount_song)
        TextView txtvAmountSong;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v->{
                int position = getLayoutPosition();
                onClickItem.onClick(v, position);
            });
        }
    }
}
