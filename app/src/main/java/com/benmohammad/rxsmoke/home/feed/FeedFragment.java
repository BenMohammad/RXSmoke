package com.benmohammad.rxsmoke.home.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.benmohammad.rxsmoke.R;
import com.benmohammad.rxsmoke.base.BaseFragment;
import com.benmohammad.rxsmoke.home.adapter.QuestionAdapterRow;
import com.benmohammad.rxsmoke.home.adapter.QuestionsAdapter;
import com.benmohammad.rxsmoke.home.adapter.QuestionsAdapterRowDataSet;
import com.benmohammad.rxsmoke.rxevent.RxEventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class FeedFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.feeds_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.feeds_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Inject
    RxEventBus eventBus;

    private LinearLayoutManager manager;
    protected QuestionsAdapter adapter;
    protected QuestionsAdapterRowDataSet dataSet;

    private RecyclerView.OnScrollListener onScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    int lastVisibleItem = manager.findLastVisibleItemPosition();
                    if(lastVisibleItem > -1) {
                        QuestionAdapterRow row = dataSet.get(lastVisibleItem);
                        if(row.isTypeLoadMore()) {
                            loadNextPage();
                        }
                    }
                }
            };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        setViewUnbinder(ButterKnife.bind(this, rootView));
        attachPresenter();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void setUpView(View view) {
        recyclerView.removeOnScrollListener(onScrollListener);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(adapter != null) {
            adapter.handleDestroy();
        }

        adapter = getAdapter();
        if(dataSet != null) {
            dataSet.handleDestroy();
        }
        if(adapter != null) {
            if(dataSet == null) {
                dataSet =  getAdapterDataSet(adapter);
            }
            adapter.setData(dataSet);
            adapter.setEventBus(eventBus);
        }
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(onScrollListener);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setEnabled(false);
    }

    @Override
    public void showLoading() {
        setRefreshLayout(false);
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onDestroyView() {
        if(adapter != null) {
            adapter.handleDestroy();
            adapter = null;
        }

        if(recyclerView != null) {
            recyclerView.removeOnScrollListener(onScrollListener);
        }

        if(refreshLayout != null) {
            refreshLayout.setOnRefreshListener(null);
        }
        super.onDestroyView();
    }

    private void setRefreshLayout(boolean refresh) {
        if(refreshLayout != null) {
            refreshLayout.setRefreshing(refresh);
        }
    }

    protected abstract void attachPresenter();
    protected abstract void loadNextPage();
    protected abstract void loadFeeds();
    protected abstract QuestionsAdapter getAdapter();
    protected abstract QuestionsAdapterRowDataSet getAdapterDataSet(QuestionsAdapter adapter);
}
