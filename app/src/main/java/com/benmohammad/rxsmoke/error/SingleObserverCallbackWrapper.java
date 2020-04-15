package com.benmohammad.rxsmoke.error;

import com.benmohammad.rxsmoke.base.MvpView;

import java.lang.ref.WeakReference;

import io.reactivex.observers.DisposableSingleObserver;

public abstract class SingleObserverCallbackWrapper<T> extends DisposableSingleObserver<T> {

    private WeakReference<MvpView> weakReference;

    public SingleObserverCallbackWrapper(MvpView view) {
        this.weakReference = new WeakReference<>(view);
    }

    @Override
    public void onSuccess(T t) {
        duringSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        MvpView view = weakReference.get();
    }

    protected abstract void duringSuccess(T t);
    protected abstract void duringFailure(String message);
}
