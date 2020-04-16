package com.benmohammad.rxsmoke.di;

import com.benmohammad.rxsmoke.AppConfig;
import com.benmohammad.rxsmoke.AppPreferences;
import com.benmohammad.rxsmoke.constants.AppConstants;
import com.benmohammad.rxsmoke.data.api.StackExchangeApi;
import com.benmohammad.rxsmoke.data.model.MyAdapterFactory;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class ApiModule {

    private static final String BASE_URL = "https://api.stackexchange.com";

    @Provides
    @Singleton
    Moshi provideMoshi() {
        return new Moshi.Builder().add(MyAdapterFactory.create()).build();
    }

    @Provides
    @Singleton
    Retrofit provideCall(Moshi moshi, AppPreferences preferences, AppConfig appConfig) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();
                        Request.Builder builder;
                        if(preferences.isLoggedIn()) {
                            HttpUrl url = originalHttpUrl.newBuilder()
                                    .addQueryParameter(AppConstants.KEY, appConfig.accessKey())
                                    .addQueryParameter(AppConstants.ACCESS_TOKEN, preferences.getAccessToken())
                                    .build();

                            builder = original.newBuilder()
                                    .header("Content-Type", "application/json")
                                    .url(url);

                        } else {
                            HttpUrl url = originalHttpUrl.newBuilder()
                                    .addQueryParameter(AppConstants.KEY, appConfig.accessKey())
                                    .build();

                            builder = original.newBuilder()
                                    .header("Content-Type", "application/json")
                                    .url(url);
                        }
                        Request request = builder.build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public StackExchangeApi provideApi(Retrofit retrofit) {
        return retrofit.create(StackExchangeApi.class);
    }

}
