package com.benmohammad.rxsmoke.home;

import com.benmohammad.rxsmoke.common.CommonPresenter;
import com.benmohammad.rxsmoke.common.CommonPresenterImpl;
import com.benmohammad.rxsmoke.common.CommonView;
import com.benmohammad.rxsmoke.di.PerChildFragment;
import com.benmohammad.rxsmoke.di.PerFragment;
import com.benmohammad.rxsmoke.home.feed.ActivityFeedFragment;
import com.benmohammad.rxsmoke.home.feed.FeaturedFeedFragment;
import com.benmohammad.rxsmoke.home.feed.FeedView;
import com.benmohammad.rxsmoke.home.feed.FeedViewPresenter;
import com.benmohammad.rxsmoke.home.feed.FeedViewPresenterImpl;
import com.benmohammad.rxsmoke.home.feed.HotFeedFragment;
import com.benmohammad.rxsmoke.home.feed.MonthLyFeedFragment;
import com.benmohammad.rxsmoke.home.feed.ProfileFragment;
import com.benmohammad.rxsmoke.home.feed.WeekLyFeedFragment;
import com.benmohammad.rxsmoke.home.profile.MyFeedFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeActivityModule {



    @PerFragment
    @ContributesAndroidInjector
    abstract ActivityFeedFragment provideInterestingFeedFragmentFactory();

    @PerFragment
    @ContributesAndroidInjector
    abstract FeaturedFeedFragment provideFeaturedFeedFragmentFactory();

    @PerFragment
    @ContributesAndroidInjector
    abstract HotFeedFragment provideHotFeedFragmentFactory();

    @PerFragment
    @ContributesAndroidInjector
    abstract MonthLyFeedFragment provideMonthlyFeedFragmentFactory();

    @PerFragment
    @ContributesAndroidInjector
    abstract WeekLyFeedFragment provideWeekLyFeedFragmentFactory();

    @PerFragment
    @ContributesAndroidInjector()
    abstract ProfileFragment provideSelfFragmentFactory();

    @PerChildFragment
    @ContributesAndroidInjector
    abstract MyFeedFragment provideMyFeedFragmentFactory();

    @Binds
    abstract FeedViewPresenter<FeedView> provideFeedViewPresenter(FeedViewPresenterImpl<FeedView> feedViewPresenterIml);

    @Binds
    abstract CommonPresenter<CommonView> provideCommonPresenter(CommonPresenterImpl<CommonView> commonPresenterIml);
}
