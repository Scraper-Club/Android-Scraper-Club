package com.scraperclub.android.api;

import com.scraperclub.android.api.model.ApiKey;
import com.scraperclub.android.api.model.DeviceStatistic;
import com.scraperclub.android.api.model.ScraperResult;
import com.scraperclub.android.api.model.ScraperUrl;
import com.scraperclub.android.api.model.UserCredentials;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface ScraperAPI {

    Single<DeviceStatistic> getDeviceStatistic();

    Single<ScraperUrl> getNextUrl();
    Completable uploadResult(ScraperResult result);

    Completable checkDevice();
    Completable registerDevice();

    Completable verifyApiKey(ApiKey apiKey);

    Single<ApiKey> login(UserCredentials credentials);

    void setDeviceId(String deviceId);
    void setApiKey(ApiKey apiKey);

    Single<ScraperUrl> getNextUrl(String pool);
}
