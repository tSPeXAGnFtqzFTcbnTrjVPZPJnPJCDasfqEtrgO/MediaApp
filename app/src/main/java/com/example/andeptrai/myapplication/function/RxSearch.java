package com.example.andeptrai.myapplication.function;

import android.widget.SearchView;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxSearch {
    public static Observable<String> fromSearchView(SearchView searchView){
        PublishSubject<String> subject = PublishSubject.create();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                subject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                s=s.replace(" ", "");
                subject.onNext(s);
                return true;
            }
        });
        return subject;
    }
}
