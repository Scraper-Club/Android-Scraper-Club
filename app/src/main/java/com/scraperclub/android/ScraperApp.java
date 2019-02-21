package com.scraperclub.android;

import android.app.Application;
import android.content.SharedPreferences;

import com.scraperclub.android.api.APIUrlsResolver;
import com.scraperclub.android.api.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

public class ScraperApp extends Application {

    public static final String PREFERENCES_TOKEN_KEY = "token";
    public static final String PREFERENCES_FIRST_LAUNCH = "frst";
    public static final String PREFERENCES_SERVER_URL = "server";

    public static final String PREFERENCES_NAME = "scraper_pref";

    private static ScraperApp instance;

    private static ScraperApp getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        APIUrlsResolver.setServerURL(
                loadUrlFromPreferences()
        );
    }

    private URL loadUrlFromPreferences(){
        SharedPreferences preferences = getSharedPreferences(ScraperApp.PREFERENCES_NAME,MODE_PRIVATE);
        String serverUrlString = preferences.getString(PREFERENCES_SERVER_URL, BuildConfig.SERVER_URL);
        URL serverUrl;
        try {
            serverUrl = new URL(serverUrlString);
        } catch (MalformedURLException e) {
            try {
                serverUrl = new URL(BuildConfig.SERVER_URL);
            } catch (MalformedURLException e1) {
                throw new RuntimeException("Bad built-in Server URL");
            }
        }

        return serverUrl;
    }

    public void updateServerURL(URL newServerUrl){
        APIUrlsResolver.setServerURL(newServerUrl);
        SharedPreferences preferences = getSharedPreferences(ScraperApp.PREFERENCES_NAME,MODE_PRIVATE);
        preferences.edit().putString(PREFERENCES_SERVER_URL,newServerUrl.toString()).apply();
    }
}
