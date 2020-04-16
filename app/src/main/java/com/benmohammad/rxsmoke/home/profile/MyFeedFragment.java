package com.benmohammad.rxsmoke.home.profile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.benmohammad.rxsmoke.constants.AppConstants;
import com.benmohammad.rxsmoke.home.adapter.QuestionAdapterRow;
import com.benmohammad.rxsmoke.home.adapter.QuestionsAdapter;
import com.benmohammad.rxsmoke.home.adapter.QuestionsAdapterRowDataSet;
import com.benmohammad.rxsmoke.home.feed.FeedFragment;
import com.benmohammad.rxsmoke.home.feed.FeedView;
import com.benmohammad.rxsmoke.home.feed.FeedViewPresenter;

import java.util.List;

import javax.inject.Inject;

public class MyFeedFragment extends FeedFragment implements FeedView {

    @Inject
    public MyFeedFragment(){}

    @Inject
    FeedViewPresenter<FeedView> presenter;

    public static MyFeedFragment newInstance() {
        MyFeedFragment fragment = new MyFeedFragment();
        return fragment;
    }

    public void loadQuestions() {
        presenter.loadQuestions();
    }

    private String filterType = "activity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            filterType = getArguments().getString(AppConstants.ARG_FILTER_TYPE);
        }
    }

    @Override
    protected void setUpView(View view) {
        super.setUpView(view);
        presenter.init(dataSet, filterType);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
    }

    @Override
    protected void loadNextPage() {
        presenter.loadNextPage();
    }

    @Override
    protected void loadFeeds() {
        presenter.loadQuestions();
    }

    @Override
    protected QuestionsAdapter getAdapter() {
        return new QuestionsAdapter();
    }

    @Override
    protected QuestionsAdapterRowDataSet getAdapterDataSet(QuestionsAdapter adapter) {
        return QuestionsAdapterRowDataSet.createWithEmptyData(adapter);
    }

    @Override
    public void onQuestionsLoaded(List<QuestionAdapterRow> rows) {
        adapter.notifyDataSetChanged();
    }

    public void cleanUp() {
        if(dataSet != null) {
            dataSet.clearDataSet();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.cleanUp();
        presenter.onDetach();
    }
}
