package com.scraperclub.android;

import android.app.Application;

import com.scraperclub.android.api.APIUrlsResolver;

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
    }
    
}
