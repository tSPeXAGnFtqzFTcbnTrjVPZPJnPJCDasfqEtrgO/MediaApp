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
import android.widget.SearchView;

import com.example.andeptrai.myapplication.Instance;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.adapter.SearchAdapter;
import com.example.andeptrai.myapplication.function.RxSearch;
import com.example.andeptrai.myapplication.function.ShowLog;

import java.util.ArrayList;
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

public class Fragment2 extends Fragment {
    @BindView(R.id.list_search)
    RecyclerView listSearch;
    ArrayList<String> searchs = new ArrayList<>();
    SearchAdapter adapterSearch;
    @BindView(R.id.searchView)
    SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_frag2,container,false );
        ButterKnife.bind(this,view);
        init();
        setUpSearch();
        return view;
    }


    private void  init(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapterSearch = new SearchAdapter(Instance.songList,getContext());
        listSearch.setLayoutManager(layoutManager);
        listSearch.setAdapter(adapterSearch);


    }

    private void setUpSearch(){
        RxSearch.fromSearchView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS )
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
                        ShowLog.logInfo("searchview","complete" );
                    }
                });
    }

}
