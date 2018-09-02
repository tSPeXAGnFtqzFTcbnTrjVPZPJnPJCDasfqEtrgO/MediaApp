package com.example.andeptrai.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.example.andeptrai.myapplication.DetailPlaylistActivity;
import com.example.andeptrai.myapplication.PlayerActivity;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.constant.Action;
import com.example.andeptrai.myapplication.function.GetSongName;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.model.Song;
import com.example.andeptrai.myapplication.services.ForegroundService;
import com.example.andeptrai.myapplication.utils.SetListPlay;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMusicPlaylistAdapter extends RecyclerView.Adapter<ListMusicPlaylistAdapter.Holder> {

    ArrayList<Song> mSongs;
    Context context;
    DetailPlaylistActivity detailPlaylistActivity;

    ArrayList<Boolean> checkList = new ArrayList<>();

    boolean isSelect = false;
    int numSelect = 0;
    int positionPlaylist;

    private OnLongClickListener longClickListener;
    private OnClickListener clickListener;

    public interface OnLongClickListener {
        void onLongClick(View view, int posion);
    }

    public interface OnClickListener {
        void onClick(View view, int position, int numSelect, ArrayList<Boolean> checkList);
    }

    public ListMusicPlaylistAdapter(ArrayList<Song> songs, int positionPlaylist, Context context,
                                    OnLongClickListener onLongClickListener, OnClickListener onClickListener) {
        this.mSongs = songs;
        this.context = context;
        longClickListener = onLongClickListener;
        clickListener = onClickListener;
        this.positionPlaylist = positionPlaylist;

        detailPlaylistActivity = (DetailPlaylistActivity) context;

        for (int i = 0; i < songs.size(); i++) {
            checkList.add(false);
        }
        ShowLog.logInfo("size", songs.size());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_music_fragment, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.txtvName.setText(GetSongName.getSongName(mSongs.get(position)));
        holder.txtvArtist.setText(mSongs.get(position).getArtistName());

        if (isSelect) {
            ShowLog.logVar("check", "" + mSongs.get(position).getNameEn() + "_" + checkList.get(position));

            holder.checkBox.setVisibility(View.VISIBLE);

            holder.checkBox.setChecked(checkList.get(position));
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.check_item)
        CheckBox checkBox;
        @BindView(R.id.txtv_name)
        TextView txtvName;
        @BindView(R.id.txtv_artist)
        TextView txtvArtist;


        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setTag(getLayoutPosition());

            checkBox.setClickable(false);
            itemView.setOnLongClickListener(view -> {
                int pos = getLayoutPosition();

                if (!isSelect) {
                    numSelect++;
                    isSelect = true;
                    checkList.set(pos, Boolean.TRUE);
                    notifyDataSetChanged();

                    longClickListener.onLongClick(view, pos);
                    clickListener.onClick(view, pos, numSelect, checkList);


                    return true;
                }

                return false;
            });
            itemView.setOnClickListener(view -> {
                int pos = getLayoutPosition();

                if (isSelect) {
                    if (checkList.get(pos)) {
                        checkList.set(pos, false);
                        checkBox.setChecked(checkList.get(pos));
                        numSelect--;
                    } else {
                        checkList.set(pos, true);
                        checkBox.setChecked(checkList.get(pos));
                        numSelect++;
                    }
                    clickListener.onClick(view, pos, numSelect, checkList);
                } else {
                    SetListPlay.playOneInPlaylist(positionPlaylist,pos);//new add

                    Intent intentService = new Intent(context, ForegroundService.class);
                    intentService.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    intentService.putExtra(ForegroundService.POS_KEY, 0);//new fix

                    intentService.setAction(Action.START_FORE.getName());

                    context.startService(intentService);

                    Intent intent = new Intent(context, PlayerActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    private void setGone() {
        isSelect = false;
        numSelect = 0;
        for (int i = 0; i < checkList.size(); i++) {
            checkList.set(i, Boolean.FALSE);
        }
        notifyDataSetChanged();
    }


    public void callAddToPlaylist() {
        ShowLog.logInfo("adapter", "add");
    }

    public void callEdit() {
        ShowLog.logInfo("adapter", "Edit");
    }

    public void callRemove() {
        ShowLog.logInfo("adapter", "remove");
    }

    public void callApply() {
        ShowLog.logInfo("adapter", "apply");
        setGone();
    }

    public void setClick() {

    }

}
