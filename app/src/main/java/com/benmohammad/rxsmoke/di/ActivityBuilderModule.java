package com.benmohammad.rxsmoke.di;

import com.benmohammad.rxsmoke.auth.LoginActivity;
import com.benmohammad.rxsmoke.home.HomeActivity;
import com.benmohammad.rxsmoke.home.HomeActivityModule;
import com.benmohammad.rxsmoke.splash.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBuilderModule {

    @PerActivity
    @ContributesAndroidInjector(modules = {HomeActivityModule.class})
    abstract HomeActivity bindHomeActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract SplashActivity bindSplashActivity();


    @PerActivity
    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();
}
