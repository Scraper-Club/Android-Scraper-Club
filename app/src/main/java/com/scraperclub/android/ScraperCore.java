package com.scraperclub.android;

import com.scraperclub.android.api.ScraperAPI;
import com.scraperclub.android.api.model.ApiKey;

public class ScraperCore {
    private static ScraperCore instance;

    private ScraperAPI api;
    private PreferencesManager preferencesManager;

    private ScraperCore(ScraperAPI api, PreferencesManager preferencesManager) {
        this.api = api;
        this.preferencesManager = preferencesManager;
    }

    public static ScraperCore getInstance() {
        return instance;
    }

    public static ScraperCore create(ScraperAPI api, PreferencesManager preferences){
        instance = new ScraperCore(api,preferences);
        return instance;
    }

    public ScraperAPI getApi() {
        return api;
    }

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public ApiKey getToken() {
        return new ApiKey(preferencesManager.getToken());
    }

    public boolean isTokenAvailable() {
        return preferencesManager.isTokenAvailable();
    }

    public void setApiKey(ApiKey apiKey) {
        preferencesManager.setToken(apiKey.getValue());
    }

    public void clearPrefs() {
        preferencesManager.clearPrefs();
    }
}
