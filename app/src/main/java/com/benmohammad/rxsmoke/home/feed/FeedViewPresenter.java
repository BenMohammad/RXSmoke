package com.benmohammad.rxsmoke.home.feed;

import com.benmohammad.rxsmoke.base.MvpPresenter;
import com.benmohammad.rxsmoke.base.MvpView;
import com.benmohammad.rxsmoke.home.adapter.QuestionsAdapterRowDataSet;

public interface FeedViewPresenter<V extends MvpView> extends MvpPresenter<V> {

    void init(QuestionsAdapterRowDataSet dataSet, String filterType);
    void loadQuestions();
    void loadNextPage();
    void cleanUp();
}
