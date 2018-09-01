package com.example.andeptrai.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.model.Playlist;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaylistFragmentAdapter extends RecyclerView.Adapter<PlaylistFragmentAdapter.Holder> {

    ArrayList<Playlist> playlists;
    Context context;
    OnClickItem onClickItem;
    OnClickMenu onClickMenu;


    public interface OnClickItem {
        void onClick(View view,int position);
    }
    public interface OnClickMenu{
        void onClick(int menuId,long playlistId,int position);
    }

    public PlaylistFragmentAdapter(ArrayList<Playlist> playlists, Context context, OnClickItem onClickItem,
                                   OnClickMenu onClickMenu) {
        this.playlists = playlists;
        this.context = context;
        this.onClickItem = onClickItem;
        this.onClickMenu = onClickMenu;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_fragment_playlist,parent,false );
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.txtvName.setText(playlists.get(position).getmName());
        holder.txtvAmountSong.setText(""+playlists.get(position).getmCount());
        holder.txtvTotalDuration.setText(""+playlists.get(position).getTotalDuration());

    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtv_name)
        TextView txtvName;
        @BindView(R.id.txtv_amount_song)
        TextView txtvAmountSong;
        @BindView(R.id.txtv_duration_total)
        TextView txtvTotalDuration;
        @BindView(R.id.btn_menu)
        ImageButton btn_menu;



        PopupMenu popupMenu;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView );

            popupMenu = new PopupMenu(context,btn_menu);
            popupMenu.inflate(R.menu.menu_popup_playlist);




            itemView.setOnClickListener(v->{
                int position = getLayoutPosition();
                onClickItem.onClick(itemView,position );
            });
            btn_menu.setOnClickListener(v->{
                int position = getLayoutPosition();
                setupItemClickPopupMenu(popupMenu,position);
                popupMenu.show();

            });
        }
    }

    private void setupItemClickPopupMenu(PopupMenu popupMenu,int position){
        popupMenu.setOnMenuItemClickListener(item -> {
            onClickMenu.onClick(item.getItemId(),playlists.get(position).getmId(),position );
            return true;
        });
    }

}
