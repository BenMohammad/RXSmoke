package com.benmohammad.rxsmoke;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private static final String PREFS_NAME = "app-prefs";
    private static final String IS_LOGGEDIN = "osLoggedIn";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String USER_ID = "userId";
    private final SharedPreferences prefs;

    public AppPreferences(Application app) {
        this.prefs = app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGGEDIN, Boolean.FALSE);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        prefs.edit().putBoolean(IS_LOGGEDIN, isLoggedIn).apply();;
    }

    public String getAccessToken() {
        return prefs.getString(ACCESS_TOKEN, "");
    }

    public void setAccessToken(String token) {
        prefs.edit().putString(ACCESS_TOKEN, token).apply();
    }

    public Long getUserId() {
        return prefs.getLong(USER_ID, 0L);
    }

    public void setUserId(Long token) {
        prefs.edit().putLong(USER_ID, token).apply();
    }

    public SharedPreferences.Editor editor() {
        return prefs.edit();
    }

    public void delete() {
        prefs.edit().remove(USER_ID).remove(ACCESS_TOKEN).apply();
    }
}
