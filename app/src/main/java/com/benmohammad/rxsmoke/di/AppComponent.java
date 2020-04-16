package com.benmohammad.rxsmoke.di;

import android.app.Application;

import com.benmohammad.rxsmoke.App;
import com.benmohammad.rxsmoke.home.HomeActivityModule;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        HomeActivityModule.class,
        AppModule.class,
        ApiModule.class,
        ActivityBuilderModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(App app);

    Moshi moshi();
}
