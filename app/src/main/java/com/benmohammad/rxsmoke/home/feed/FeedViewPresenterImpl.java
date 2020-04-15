package com.benmohammad.rxsmoke.home.feed;

import com.benmohammad.rxsmoke.AppPreferences;
import com.benmohammad.rxsmoke.base.BasePresenter;
import com.benmohammad.rxsmoke.constants.AppConstants;
import com.benmohammad.rxsmoke.data.api.StackExchangeApi;
import com.benmohammad.rxsmoke.data.model.Question;
import com.benmohammad.rxsmoke.data.model.QuestionsResponse;
import com.benmohammad.rxsmoke.error.DisposableSubscriberCallbackWrapper;
import com.benmohammad.rxsmoke.home.adapter.QuestionAdapterRow;
import com.benmohammad.rxsmoke.home.adapter.QuestionsAdapterRowDataSet;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

public class FeedViewPresenterImpl<V extends FeedView> extends BasePresenter<V> implements FeedViewPresenter<V> {

    private StackExchangeApi api;
    private AppPreferences preferences;
    private PublishProcessor<Long> questionSubject = PublishProcessor.create();
    private CompositeDisposable disposables = new CompositeDisposable();
    private QuestionsAdapterRowDataSet rowDataSet;
    private String type;
    private long page = 1;
    private boolean isLoading = false;

    @Inject
    FeedViewPresenterImpl(StackExchangeApi api, AppPreferences preferences) {
        this.api = api;
        this.preferences = preferences;
    }

    @Override
    public void init(QuestionsAdapterRowDataSet dataSet, String filterType) {
        this.rowDataSet = dataSet;
        this.type = filterType;
        dataSet.addRow(QuestionAdapterRow.ofLoading());
        getMvpView().showLoading();
        Disposable disposable = questionSubject
                .onBackpressureDrop()
                .concatMap((Function<Long, Publisher<QuestionsResponse>>) page -> getObservable())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        removeLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriberCallbackWrapper<QuestionsResponse>(getMvpView()) {
                    @Override
                    protected void onNextAction(QuestionsResponse questionsResponse) {
                        isLoading = false;
                        getMvpView().hideLoading();
                        handleQuestionResponse(questionsResponse);
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
        disposables.add(disposable);
    }

    private void handleQuestionResponse(QuestionsResponse response) {
        removeLoading();
        List<QuestionAdapterRow> rows = new ArrayList<>();
        if(response != null && response.questions() != null && !response.questions().isEmpty()) {
            for(Question question : response.questions()) {
                rows.add(QuestionAdapterRow.ofQuestion(question));
            }
            if(response.hasMore() != null && response.hasMore()) {
                rows.add(QuestionAdapterRow.ofLoadMore());
            }
        }
        rowDataSet.addAllRows(rows);
        getMvpView().onQuestionsLoaded(rows);
    }

    private void removeLoading() {
        rowDataSet.removeLoading();
        rowDataSet.removeLoadMore();
    }

    @Override
    public void loadQuestions() {
        if(!isLoading) {
            isLoading = true;
            questionSubject.onNext(page);
        }
    }

    @Override
    public void loadNextPage() {
        if(!isLoading) {
            isLoading = true;
            page++;
            questionSubject.onNext(page);
        }
    }

    @Override
    public void cleanUp() {
        disposables.clear();
    }


    private Flowable<QuestionsResponse> getObservable() {
        if(type.equalsIgnoreCase(AppConstants.MY_FEED)) {
            return api.getUsersQuestionsFlowable(preferences.getUserId(), AppConstants.ACTIVITY, AppConstants.SITE, AppConstants.DESC, page, 10)
                    .subscribeOn(Schedulers.io());
        } else {
            return api.getQuestionsFlowable(type, AppConstants.SITE, AppConstants.DESC, page, 20)
                    .subscribeOn(Schedulers.io());
        }
    }

}
