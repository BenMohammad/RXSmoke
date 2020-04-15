package com.benmohammad.rxsmoke.rxevent;

import android.util.Pair;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxEventBus {

    @Inject
    public RxEventBus(){}

    private PublishSubject<Pair<String, Object>> eventSubject = PublishSubject.create();

    public Observable<Pair<String, Object>> toObservables() {
        return eventSubject;
    }

    public boolean hasObservers() {
        return eventSubject.hasObservers();
    }

    public void send(Pair<String, Object> event) {
        eventSubject.onNext(event);
    }
}
