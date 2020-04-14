package com.benmohammad.rxsmoke.home.feed;

import com.benmohammad.rxsmoke.base.MvpView;
import com.benmohammad.rxsmoke.home.adapter.QuestionAdapterRow;

import java.util.List;

public interface FeedView extends MvpView {

    void onQuestionsLoaded(List<QuestionAdapterRow> rows);
}
