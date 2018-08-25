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

import com.example.andeptrai.myapplication.ListMusicActivity;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.Services.ForegroundService;
import com.example.andeptrai.myapplication.constant.Action;
import com.example.andeptrai.myapplication.function.GetSongName;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.model.Song;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMusicAdapter extends RecyclerView.Adapter<ListMusicAdapter.Holder> {
    ArrayList<Song> mSongs;
    Context context;
    ListMusicActivity listMusicActivity;

    ArrayList<Boolean> checkList = new ArrayList<>();
    ;
    boolean isSelect = false;
    int numSelect = 0;

    private OnLongClickListener longClickListener;
    private OnClickListener clickListener;

    public interface OnLongClickListener {
        void onLongClick(View view, int posion);
    }

    public interface OnClickListener {
        void onClick(View view, int position);
    }

    public ListMusicAdapter(ArrayList<Song> songs, Context context,
                            OnLongClickListener onLongClickListener, OnClickListener onClickListener) {
        this.mSongs = songs;
        this.context = context;
        longClickListener = onLongClickListener;
        clickListener = onClickListener;

        listMusicActivity = (ListMusicActivity) context;

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
                longClickListener.onLongClick(view, pos);

                if (!isSelect) {
                    isSelect = true;
                    checkList.set(pos, Boolean.TRUE);
                    notifyDataSetChanged();

                    return true;
                }

                return false;
            });
            itemView.setOnClickListener(view -> {
                int pos = getLayoutPosition();
                clickListener.onClick(view, pos);
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
                } else {
                    Intent intent = new Intent(context, ForegroundService.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(ForegroundService.POS_KEY, pos);
                    intent.setAction(Action.START_FORE.getName());

                    context.startService(intent);
                }
            });

//            itemView.setOnLongClickListener(v -> {
//                int pos = getLayoutPosition();
//                ShowLog.logVar("pos", pos);
//                if (!isSelect) {
//                    isSelect = true;
//                    checkList.set(pos, Boolean.TRUE);
//                    notifyDataSetChanged();
//
//                    return true;
//                }
//
//                return false;
//            });
//            itemView.setOnClickListener(v -> {
//                int pos = getLayoutPosition();
//                ShowLog.logVar("pos", pos);
//
//                if (isSelect) {
//                    if (checkList.get(pos)) {
//                        checkList.set(pos, false);
//                        checkBox.setChecked(checkList.get(pos));
//                        numSelect--;
//                    } else {
//                        checkList.set(pos, true);
//                        checkBox.setChecked(checkList.get(pos));
//                        numSelect++;
//                    }
//                } else {
//                    Intent intent = new Intent(context, ForegroundService.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra(ForegroundService.POS_KEY, pos);
//                    intent.setAction(Action.START_FORE.getName());
//
//                    context.startService(intent);
//                }
//            });

        }
    }

    public void setGone() {
        isSelect = false;
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
    }

    public void setClick() {

    }

}
