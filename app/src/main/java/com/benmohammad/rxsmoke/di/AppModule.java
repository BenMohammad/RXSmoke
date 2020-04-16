package com.benmohammad.rxsmoke.di;

import android.app.Application;
import android.content.Context;

import com.benmohammad.rxsmoke.AppConfig;
import com.benmohammad.rxsmoke.AppPreferences;
import com.benmohammad.rxsmoke.R;
import com.benmohammad.rxsmoke.rxevent.RxEventBus;
import com.benmohammad.rxsmoke.utils.ErrorUtils;
import com.benmohammad.rxsmoke.utils.Utils;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
abstract class AppModule {

    @Binds
    abstract Context provideContext(Application application);

    @Provides
    @Singleton
    static RxEventBus provideRxEventBus() {
        return new RxEventBus();
    }


    @Provides
    @Singleton
    static AppPreferences provideAppPreferences(Application application) {
        return new AppPreferences(application);
    }

    @Provides
    @Singleton
    static AppConfig provideAppConfig(Application application) {
        return AppConfig.builder()
                .clientId(application.getString(R.string.client_id))
                .accessKey(application.getString(R.string.access_key))
                .clientSecretId(application.getString(R.string.client_secret_id))
                .redirectUri(application.getString(R.string.redirect_uri))
                .build();
    }

    @Provides
    @Singleton
    static Utils provideUtils() {
        return new Utils();
    }

    @Provides
    @Singleton
    static ErrorUtils provideErrorUtils() {
        return new ErrorUtils();
    }



}
