package com.scraperclub.android;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesManager {

    private Context context;

    public static final String TOKEN_KEY = "token";
    public static final String WITHOUT_CHARGE_KEY = "ewc";
    public static final String FIRST_LAUNCH = "frst";

    private final String PREF_NAME = "scraper_pref";

    public PreferencesManager(Context context) {
        this.context = context;
    }

    public String getToken(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        if(preferences.contains(TOKEN_KEY))
            return preferences.getString(TOKEN_KEY,null);
        else return null;
    }

    public boolean isTokenAvailable() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.contains(TOKEN_KEY) && preferences.getString(TOKEN_KEY, null) != null;
    }

    public void setToken(String token){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        preferences.edit().putString(TOKEN_KEY,token).apply();
    }

    public boolean isFirstLaunch(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return !preferences.contains(FIRST_LAUNCH) && preferences.getBoolean(FIRST_LAUNCH, true);
    }

    public void dontShowDescription(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        preferences.edit().putBoolean(FIRST_LAUNCH,false).apply();
    }

    public void clearPrefs(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        preferences.edit().remove(TOKEN_KEY).remove(WITHOUT_CHARGE_KEY).remove(FIRST_LAUNCH).apply();
    }
}
