package com.benmohammad.rxsmoke.home;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.benmohammad.rxsmoke.R;
import com.benmohammad.rxsmoke.auth.LoginActivity;
import com.benmohammad.rxsmoke.constants.AppConstants;
import com.benmohammad.rxsmoke.home.feed.ActivityFeedFragment;
import com.benmohammad.rxsmoke.home.feed.FeaturedFeedFragment;
import com.benmohammad.rxsmoke.home.feed.HotFeedFragment;
import com.benmohammad.rxsmoke.home.feed.MonthLyFeedFragment;
import com.benmohammad.rxsmoke.home.feed.ProfileFragment;
import com.benmohammad.rxsmoke.home.feed.WeekLyFeedFragment;
import com.benmohammad.rxsmoke.rxevent.AppEvents;
import com.benmohammad.rxsmoke.rxevent.RxEventBus;
import com.benmohammad.rxsmoke.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final String FRAG_TAG_PROFILE = "profileFragment";

    @Inject
    RxEventBus eventBus;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.profile_view_container)
    View profileViewContainer;

    @BindView(R.id.root)
    ViewGroup rootView;

    @Inject
    FeaturedFeedFragment featuredFeedFragment;

    @Inject
    HotFeedFragment hotFeedFragment;

    @Inject
    ActivityFeedFragment activityFeedFragment;

    @Inject
    MonthLyFeedFragment monthLyFeedFragment;

    @Inject
    WeekLyFeedFragment weekLyFeedFragment;

    @Inject
    ProfileFragment selfFragment;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private HomePagerAdapter homePagerAdapter;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpSubscription();
        setUpViews();
        setUpViewPager();
        addFragmentsToContainer();
    }

    private void setUpSubscription() {
        disposables.add(eventBus.toObservables()
                .onErrorReturn(throwable -> {
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    handleEventData(event);
                })
        );
    }

    private void handleEventData(Pair<String, Object> event) {
        if(event.first.equalsIgnoreCase(AppEvents.PROFILE_MENU_CLICKED)) {
            handleProfileMenuClicked();
        } else if (event.first.equalsIgnoreCase(AppEvents.QUESTION_TAG_CLICKED)) {
            handleQuestionsTagClicked((String) event.second);
        } else if (event.first.equalsIgnoreCase(AppEvents.BACK_ARROW_CLICKED)) {
            handleBackPressed();
        } else if (event.first.equalsIgnoreCase(AppEvents.LOGIN_CLICKED)) {
            handleLogOutCompleted();
        } else if (event.first.equalsIgnoreCase(AppEvents.LOGOUT_CLICKED)) {
            handleLogOutClicked();
        } else if (event.first.equalsIgnoreCase(AppEvents.LOGOUT_COMPLETED)) {
            handleLogOutCompleted();
        }
    }

    private void addFragmentsToContainer() {
        ProfileFragment seenFrag = getProfileFrag();
        if(seenFrag == null) {
            seenFrag = selfFragment;
            getSupportFragmentManager().beginTransaction()
                    .add(profileViewContainer.getId(), seenFrag, FRAG_TAG_PROFILE).commit();
        }
    }

    private ProfileFragment getProfileFrag() {
        return (ProfileFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_PROFILE);
    }

    private void setUpViewPager() {
        activityFeedFragment.setArguments(getFilteredArgBundle(AppConstants.ACTIVITY));
        featuredFeedFragment.setArguments(getFilteredArgBundle(AppConstants.VOTES));
        hotFeedFragment.setArguments(getFilteredArgBundle(AppConstants.HOT));
        monthLyFeedFragment.setArguments(getFilteredArgBundle(AppConstants.MONTH));
        weekLyFeedFragment.setArguments(getFilteredArgBundle(AppConstants.WEEK));
        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(),
                activityFeedFragment,
                featuredFeedFragment,
                hotFeedFragment,
                monthLyFeedFragment,
                weekLyFeedFragment,
                getResources().getStringArray(R.array.home_tabs));

        viewPager.setAdapter(homePagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }

    private void setUpViews() {
        profileViewContainer.setVisibility(View.VISIBLE);
        toolbar.inflateMenu(R.menu.menu_profile);
        toolbar.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId() == R.id.action_profile) {
                eventBus.send(new Pair<>(AppEvents.PROFILE_MENU_CLICKED, null));

            }
            return false;
        });
    }

    private void handleProfileMenuClicked() {
        Utils.captureTransitionSlide(rootView);
        profileViewContainer.setVisibility(View.VISIBLE);
        selfFragment.handleProfileClicked();
    }

    private void handleLogOutCompleted() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(AppConstants.IS_JUST_LOGGED_OUT, true);
        startActivity(intent);
        finish();
    }

    private void handleLogOutClicked() {
        selfFragment.logOutUser();
    }

    private void handleBackPressed() {
        if(profileViewContainer.getVisibility() == View.VISIBLE) {
            Utils.captureTransitionSlide(rootView);
            profileViewContainer.setVisibility(View.INVISIBLE);
            getProfileFrag().cleanUp();
        } else {
            super.onBackPressed();
        }
    }

    private void handleQuestionsTagClicked(String tag) {
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, tag, Toast.LENGTH_SHORT);

        View view = toast.getView();

        view.getBackground().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(getResources().getColor(R.color.white));
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        handleBackPressed();
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }

    private Bundle getFilteredArgBundle(String filterType) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.ARG_FILTER_TYPE, filterType);
        return bundle;
    }
}
