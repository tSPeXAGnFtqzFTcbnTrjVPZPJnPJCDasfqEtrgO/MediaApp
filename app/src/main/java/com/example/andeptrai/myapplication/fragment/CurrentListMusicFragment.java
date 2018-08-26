package com.example.andeptrai.myapplication.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.andeptrai.myapplication.Instance;
import com.example.andeptrai.myapplication.PlayerActivity;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.Services.ForegroundService;
import com.example.andeptrai.myapplication.adapter.SearchAdapter;
import com.example.andeptrai.myapplication.constant.ActionBroadCast;
import com.example.andeptrai.myapplication.function.RxSearch;
import com.example.andeptrai.myapplication.function.ShowLog;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CurrentListMusicFragment extends Fragment {
    @BindView(R.id.list_search)
    RecyclerView listSearch;

    SearchAdapter adapterSearch;
    @BindView(R.id.searchView)
    SearchView searchView;

    Context mContext;

    int pos;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_frag2, container, false);
        ButterKnife.bind(this, view);
        init();
        setUpSearch();
        return view;
    }


    private void init() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapterSearch = new SearchAdapter(Instance.songList, getContext());
        listSearch.setLayoutManager(layoutManager);
        listSearch.setAdapter(adapterSearch);


    }

    private void setUpSearch() {
        RxSearch.fromSearchView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                //.filter(s -> !s.isEmpty())
                .distinctUntilChanged()
                .switchMap((Function<String, ObservableSource<String>>) s -> Observable.just(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        ShowLog.logInfo("query search", s);
                        adapterSearch.getFilter().filter(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        ShowLog.logInfo("searchview", "complete");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        register();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregister();
    }

    private void register() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionBroadCast.UPDATE_LIST_SHUFFLE.getName());
        mContext.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregister() {
        mContext.unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == null) {
                return;
            }
            String action = intent.getAction();

            if (action.equals(ActionBroadCast.UPDATE_LIST_SHUFFLE.getName())) {
                boolean isShuffle = intent.getBooleanExtra(PlayerActivity.UPDATE_SHUFFLE_KEY, false);
                searchView.setQuery("", false);

                adapterSearch.shuffle(isShuffle);

            } else if (action.equals(ActionBroadCast.CURSEEK.getName())) {
                int t = intent.getIntExtra(ForegroundService.SONG_ID, pos);
                if (t != pos) {
                    pos = t;
                    if(pos<adapterSearch.getItemCount()){
                        listSearch.scrollToPosition(pos);
                    }
                }
            }
        }
    };

}
