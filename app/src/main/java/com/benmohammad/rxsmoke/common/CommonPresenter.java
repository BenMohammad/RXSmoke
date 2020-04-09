package com.benmohammad.rxsmoke.common;

import com.benmohammad.rxsmoke.base.MvpPresenter;
import com.benmohammad.rxsmoke.base.MvpView;

public interface CommonPresenter<V extends MvpView> extends MvpPresenter<V> {

    void loadUser();
    void invalidateAccessToken(String token);
    void init();
    void cleanUp();
}
