package com.benmohammad.rxsmoke.error;

import android.util.Pair;

import com.benmohammad.rxsmoke.base.MvpView;
import com.benmohammad.rxsmoke.utils.ErrorUtils;
import com.squareup.moshi.Moshi;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.subscribers.DisposableSubscriber;

public abstract class DisposableSubscriberCallbackWrapper<T> extends DisposableSubscriber<T> {

    private WeakReference<MvpView> weakReference;

    public DisposableSubscriberCallbackWrapper(MvpView view) {
        this.weakReference = new WeakReference<>(view);
    }

    @Inject
    Moshi moshi;

    protected abstract void onNextAction(T t);
    protected abstract void onCompleted();

    @Override
    public void onNext(T t) {
        onNextAction(t);
    }

    @Override
    public void onError(Throwable t) {
        MvpView view = weakReference.get();
        Pair<Integer, String> valuePair = ErrorUtils.errorMessage(t, moshi);
        view.onError(valuePair.second);
    }

    @Override
    public void onComplete() {
        onCompleted();
    }
}
