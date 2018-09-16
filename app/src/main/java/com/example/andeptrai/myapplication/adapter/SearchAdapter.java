package com.example.andeptrai.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.andeptrai.myapplication.Instance;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.adapter.helper.ItemTouchHelperAdapter;
import com.example.andeptrai.myapplication.adapter.helper.ItemTouchHelperViewHolder;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.services.ForegroundService;
import com.example.andeptrai.myapplication.constant.Action;
import com.example.andeptrai.myapplication.function.Kmp;
import com.example.andeptrai.myapplication.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> implements Filterable,ItemTouchHelperAdapter {

    ArrayList<Song> songs;
    ArrayList<Song> baseSongs;
    ArrayList<Song> shuffleSongs;
    Context context;
    ItemFilter itemFilter = new ItemFilter();
    long curPlayId = -1;

    boolean isShuffle = false;

    public SearchAdapter(ArrayList<Song> songs, Context context) {

        this.songs = songs;
        this.baseSongs = songs;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_search, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (Locale.getDefault().getLanguage().equals("vi")) {
            holder.txtvName.setText(songs.get(position).getNameVi());
        } else {
            holder.txtvName.setText(songs.get(position).getNameEn());
        }
        holder.txtvArtist.setText(songs.get(position).getArtistName());

        ShowLog.logInfo("search apt",songs.get(position).getId()+"_"+curPlayId );
        if (songs.get(position).getId() != curPlayId) {
            holder.itemView.setBackgroundResource(R.drawable.background_item_music);
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffd000"));
        }


    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    @Override
    public boolean onItemMove(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(songs, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(songs, i, i - 1);
            }
        }

        notifyItemMoved(from, to);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        songs.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {


        @Override
        public void onItemSelected() {
            ShowLog.logInfo("adaptr", "itemselect");
            //itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            ShowLog.logInfo("adaptr", "itemclear");
            //itemView.setBackgroundResource(R.drawable.background_item_music);
        }

        @BindView(R.id.txtv_name)
        TextView txtvName;
        @BindView(R.id.txtv_artist)
        TextView txtvArtist;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            txtvName.setSelected(true);
            txtvArtist.setSelected(true);

            itemView.setOnClickListener(v -> {

                int pos = getLayoutPosition();
                int index = songs.get(pos).getPosition();

                ShowLog.logInfo("search adapter", songs.size() + "_" + index);

                Intent intent = new Intent(context, ForegroundService.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ForegroundService.POS_KEY, pos);
                intent.setAction(Action.START_FORE.getName());

                Log.d("AAA", "recycler " + index);
                context.startService(intent);
            });

        }

    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence.toString().trim().equals("")) {
                return null;
            }
            FilterResults results = new FilterResults();
            ArrayList<Song> nlist = new ArrayList<>();

            for (int i = 0; i < baseSongs.size(); i++) {
                if (Kmp.isMatch(baseSongs.get(i).getNameSearch(), charSequence.toString())) {
                    nlist.add(baseSongs.get(i));
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults == null) {
                songs = isShuffle ? shuffleSongs : baseSongs;
            } else {
                songs = (ArrayList<Song>) filterResults.values;
            }
            notifyDataSetChanged();
        }
    }


    public void setCurPlayId(long curPlayId) {
        if (this.curPlayId != curPlayId) {
            notifyDataSetChanged();
            this.curPlayId = curPlayId;
        }
    }

    public void shuffle(boolean isShuffle) {

        this.isShuffle = isShuffle;

        songs = baseSongs;
        shuffleSongs = baseSongs;

        ArrayList<Song> suffleArray = new ArrayList<>();
        suffleArray.addAll(baseSongs);

        if (isShuffle) {
            Collections.shuffle(suffleArray);
            songs = suffleArray;
            shuffleSongs = suffleArray;
            for (int i = 0; i < songs.size(); i++) {
                songs.get(i).setPosition(i);
            }
        }
        Instance.songShuffleList.clear();
        Instance.songShuffleList.addAll(songs);
        notifyDataSetChanged();
    }
}
