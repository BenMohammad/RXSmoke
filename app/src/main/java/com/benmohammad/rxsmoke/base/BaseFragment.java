package com.benmohammad.rxsmoke.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benmohammad.rxsmoke.R;

import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

public abstract class BaseFragment extends Fragment implements MvpView {

    @Nullable
    private Unbinder viewUnbinder;

    private BaseActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            AndroidSupportInjection.inject(this);
        }
        if(activity instanceof BaseActivity) {
            this.mActivity = (BaseActivity) activity;
        }
        super.onAttach(activity);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            AndroidSupportInjection.inject(this);
        }
        if(context instanceof  BaseActivity) {
            this.mActivity = (BaseActivity) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView(view);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        if(viewUnbinder != null) {
            viewUnbinder.unbind();
        }
        mActivity = null;
        super.onDestroyView();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(int resId) {

    }

    @Override
    public void onError(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getBaseActivity())
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .setCancelable(true);
        dialogBuilder.show();
    }

    public BaseActivity getBaseActivity(){
        return mActivity;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public void hideKeyboard() {

    }

    protected abstract void setUpView(View view);

    public void setViewUnbinder(Unbinder unbinder) {
        this.viewUnbinder = unbinder;
    }

    public interface Callback {
        void onFragmentAttached();
        void onFragmentDetached(String tag);
    }
}
