package com.scraperclub.android;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesManager {

    private Context context;

    public PreferencesManager(Context context) {
        this.context = context;
    }

    public String getToken(){
        SharedPreferences preferences = context.getSharedPreferences(ScraperApp.PREFERENCES_NAME,MODE_PRIVATE);
        if(preferences.contains(ScraperApp.PREFERENCES_TOKEN_KEY))
            return preferences.getString(ScraperApp.PREFERENCES_TOKEN_KEY,null);
        else return null;
    }

    public boolean isTokenAvailable() {
        SharedPreferences preferences = context.getSharedPreferences(ScraperApp.PREFERENCES_NAME, MODE_PRIVATE);
        return preferences.contains(ScraperApp.PREFERENCES_TOKEN_KEY) &&
                preferences.getString(ScraperApp.PREFERENCES_TOKEN_KEY, null) != null;
    }

    public void setToken(String token){
        SharedPreferences preferences = context.getSharedPreferences(ScraperApp.PREFERENCES_NAME,MODE_PRIVATE);
        preferences.edit().putString(ScraperApp.PREFERENCES_TOKEN_KEY,token).apply();
    }

    public boolean isFirstLaunch(){
        SharedPreferences preferences = context.getSharedPreferences(ScraperApp.PREFERENCES_NAME, MODE_PRIVATE);
        return !preferences.contains(ScraperApp.PREFERENCES_FIRST_LAUNCH) &&
                preferences.getBoolean(ScraperApp.PREFERENCES_FIRST_LAUNCH, true);
    }

    public void dontShowDescription(){
        SharedPreferences preferences = context.getSharedPreferences(ScraperApp.PREFERENCES_NAME,MODE_PRIVATE);
        preferences.edit().putBoolean(ScraperApp.PREFERENCES_FIRST_LAUNCH,false).apply();
    }

    public void clearPrefs(){
        SharedPreferences preferences = context.getSharedPreferences(ScraperApp.PREFERENCES_NAME,MODE_PRIVATE);
        preferences.edit()
                .remove(ScraperApp.PREFERENCES_TOKEN_KEY)
                .remove(ScraperApp.PREFERENCES_FIRST_LAUNCH)
                .apply();
    }
}
