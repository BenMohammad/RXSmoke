package com.benmohammad.rxsmoke.error;

import com.benmohammad.rxsmoke.base.MvpView;

import java.lang.ref.WeakReference;

import io.reactivex.observers.DisposableObserver;

public abstract class DisposableObserverCallbackWrapper<T> extends DisposableObserver<T> {

    private WeakReference<MvpView> weakReference;

    public DisposableObserverCallbackWrapper(MvpView view) {
        this.weakReference = new WeakReference<>(view);
    }

    protected abstract void onSuccess(T t);
    protected abstract void onNextAction(T t);
    protected abstract void onCompleted();

    @Override
    public void onNext(T t) {
        onNextAction(t);
    }

    @Override
    public void onError(Throwable e) {
        MvpView view = weakReference.get();
    }

    @Override
    public void onComplete() {
        onCompleted();
    }
}
