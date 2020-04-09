package com.benmohammad.rxsmoke.common;

import com.benmohammad.rxsmoke.base.MvpView;
import com.benmohammad.rxsmoke.data.model.Owner;


public interface CommonView extends MvpView {

    void showUser(Owner owner);
    void onLoggedOut();
}
