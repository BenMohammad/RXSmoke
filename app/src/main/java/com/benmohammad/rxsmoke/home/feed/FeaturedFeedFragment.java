package com.benmohammad.rxsmoke.home.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.benmohammad.rxsmoke.constants.AppConstants;
import com.benmohammad.rxsmoke.home.adapter.QuestionAdapterRow;
import com.benmohammad.rxsmoke.home.adapter.QuestionsAdapter;
import com.benmohammad.rxsmoke.home.adapter.QuestionsAdapterRowDataSet;

import java.util.List;

import javax.inject.Inject;

public class FeaturedFeedFragment extends FeedFragment implements FeedView {

    @Inject
    public FeaturedFeedFragment(){}

    @Inject
    FeedViewPresenter<FeedView> presenter;

    private String filterType = "activity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            filterType = getArguments().getString(AppConstants.ARG_FILTER_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setUpView(View view) {
        super.setUpView(view);
        presenter.init(dataSet, filterType);
        loadFeeds();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.cleanUp();
        presenter.onDetach();
    }
}
