package com.benmohammad.rxsmoke.home.feed;

import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.benmohammad.rxsmoke.AppPreferences;
import com.benmohammad.rxsmoke.R;
import com.benmohammad.rxsmoke.base.BaseFragment;
import com.benmohammad.rxsmoke.common.CommonPresenter;
import com.benmohammad.rxsmoke.common.CommonView;
import com.benmohammad.rxsmoke.constants.AppConstants;
import com.benmohammad.rxsmoke.data.model.Owner;
import com.benmohammad.rxsmoke.home.profile.MyFeedFragment;
import com.benmohammad.rxsmoke.rxevent.AppEvents;
import com.benmohammad.rxsmoke.rxevent.RxEventBus;
import com.benmohammad.rxsmoke.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class ProfileFragment extends BaseFragment implements HasSupportFragmentInjector, CommonView {

    private static final String FRAG_TAG_MY_FEED = "myFeedFragment";

    @Inject
    public ProfileFragment(){}

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Inject
    RxEventBus eventBus;

    @Inject
    AppPreferences preferences;

    @Inject
    CommonPresenter<CommonView> presenter;

    @Inject
    MyFeedFragment myFeedFragment;

    @BindView(R.id.my_feeds_container)
    View myFeedsContainer;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.screen_mask_with_loader)
    View mask;

    @BindView(R.id.login_empty_state_panel)
    View loginPanel;

    @BindView(R.id.button_login)
    View buttonLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        setViewUnbinder(ButterKnife.bind(this, rootView));
        presenter.onAttach(this);
        return rootView;
    }

    @Override
    protected void setUpView(View view) {
        toolbar.setNavigationIcon(Utils.getTintedVectorAsset(getActivity(), R.drawable.ic_close_black_24dp, R.color.black));
        toolbar.setNavigationOnClickListener(v -> eventBus.send(new Pair<>(AppEvents.BACK_ARROW_CLICKED, " ")));
        addChildFragment();
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventBus.send(new Pair<>(AppEvents.LOGIN_CLICKED, null));
            }
        });
    }

    private void addChildFragment() {
        MyFeedFragment myFeedFrag = getMyFeedFragment();
        if(myFeedFrag == null) {
            myFeedFrag = myFeedFragment;
            myFeedFrag.setArguments(getFilterArgBundle());
            getChildFragmentManager().beginTransaction()
                    .add(myFeedsContainer.getId(), myFeedFrag, FRAG_TAG_MY_FEED).commit();
        }
    }

    private MyFeedFragment getMyFeedFragment() {
        return (MyFeedFragment) getChildFragmentManager().findFragmentByTag(FRAG_TAG_MY_FEED);
    }

    private Bundle getFilterArgBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.ARG_FILTER_TYPE, AppConstants.MY_FEED);
        return bundle;
    }

    private void showOptionMenu(View view, PopupMenu.OnMenuItemClickListener onMenuItemClickListener) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.action_menu_logout, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(onMenuItemClickListener);
    }


    private PopupMenu.OnMenuItemClickListener menuItemClickListener(RxEventBus eventBus) {
        return item -> {
            switch(item.getItemId()) {
                case R.id.action_logout:
                eventBus.send(new Pair<>(AppEvents.LOGOUT_CLICKED, null));
                break;
                default:
                    break;
            }
            return true;
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.cleanUp();
        presenter.onDetach();
    }

    private void handleProfileClicked() {
        if(preferences.isLoggedIn()) {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_logout);
            toolbar.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.action_logout_menu) {
                    showOptionMenu(toolbar, menuItemClickListener(eventBus));
                }
                return false;
            });
            presenter.loadUser();
        } else {
            loginPanel.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void showUser(Owner owner) {
        preferences.setUserId(owner.id());
        toolbar.setTitle(owner.name());
        myFeedFragment.loadQuestions();
    }

    @Override
    public void onLoggedOut() {
        preferences.setIsLoggedIn(false);
        preferences.delete();
        eventBus.send(new Pair<>(AppEvents.LOGOUT_COMPLETED, null));
    }


    public void cleanUp() {
        myFeedFragment.cleanUp();
    }

    public void logOutUser() {
        mask.setVisibility(View.VISIBLE);
        presenter.invalidateAccessToken(preferences.getAccessToken());
    }


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }
}
