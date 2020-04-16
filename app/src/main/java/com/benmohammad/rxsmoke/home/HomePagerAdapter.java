package com.benmohammad.rxsmoke.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.benmohammad.rxsmoke.home.feed.ActivityFeedFragment;
import com.benmohammad.rxsmoke.home.feed.FeaturedFeedFragment;
import com.benmohammad.rxsmoke.home.feed.HotFeedFragment;
import com.benmohammad.rxsmoke.home.feed.MonthLyFeedFragment;
import com.benmohammad.rxsmoke.home.feed.WeekLyFeedFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private final FeaturedFeedFragment featuredFeedFragment;
    private final HotFeedFragment hotFeedFragment;
    private final ActivityFeedFragment interestingFeedFragment;
    private final MonthLyFeedFragment monthLyFeedFragment;
    private final WeekLyFeedFragment weekLyFeedFragment;
    private final String[] names;

    HomePagerAdapter(FragmentManager fm, ActivityFeedFragment interestingFeedFragment,
                     FeaturedFeedFragment featuredFeedFragment, HotFeedFragment hotFeedFragment,
                     MonthLyFeedFragment monthLyFeedFragment, WeekLyFeedFragment weekLyFeedFragment,
                     String[] names) {
        super(fm);
        this.interestingFeedFragment = interestingFeedFragment;
        this.featuredFeedFragment = featuredFeedFragment;
        this.hotFeedFragment = hotFeedFragment;
        this.monthLyFeedFragment = monthLyFeedFragment;
        this.weekLyFeedFragment = weekLyFeedFragment;
        this.names = names;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return interestingFeedFragment;
        } else if(position == 1) {
            return featuredFeedFragment;
        } else if (position == 2) {
            return hotFeedFragment;
        } else if (position == 3) {
            return monthLyFeedFragment;
        } else if (position == 4) {
            return weekLyFeedFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "tabs";
        if(position == 0) {
            title = names[0];
        } else if (position == 1) {
            title = names[1];
        } else if (position == 2) {
            title = names[2];
        } else if (position == 3) {
            title = names[3];
        } else if (position == 4) {
            title = names[4];
        }
        return title;
    }
}
