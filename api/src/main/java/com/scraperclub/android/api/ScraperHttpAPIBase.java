package com.scraperclub.android.api;

import com.scraperclub.android.api.http.FormUrlEncodedHttpRequest;
import com.scraperclub.android.api.http.HttpRequest;
import com.scraperclub.android.api.http.TextHtmlHttpRequest;
import com.scraperclub.android.api.model.ApiKey;

import java.util.ArrayList;
import java.util.List;

abstract class ScraperHttpAPIBase implements ScraperAPI{

    TextHtmlHttpRequest uploadHtmlRequest;
    FormUrlEncodedHttpRequest loginRequest, registerDeviceRequest, verifyApiKeyRequest;
    HttpRequest getDeviceStatisticRequest, getNexrUrlRequest, checkDeviceRequest, badUrlRequest;

    private List<HttpRequest> requests;

    protected ScraperHttpAPIBase() {
        requests = new ArrayList<>();

        uploadHtmlRequest = new TextHtmlHttpRequest();
        requests.add(uploadHtmlRequest);

        loginRequest = new FormUrlEncodedHttpRequest();
        loginRequest.setConnectionUrl(ScraperAPIUrls.login());
        requests.add(loginRequest);

        verifyApiKeyRequest = new FormUrlEncodedHttpRequest();
        verifyApiKeyRequest.setConnectionUrl(ScraperAPIUrls.verifyApiKey());
        requests.add(verifyApiKeyRequest);


        getDeviceStatisticRequest = new HttpRequest();
        getDeviceStatisticRequest.setConnectionUrl(ScraperAPIUrls.deviceStatistics());
        requests.add(getDeviceStatisticRequest);

        getNexrUrlRequest = new HttpRequest();
        getNexrUrlRequest.setConnectionUrl(ScraperAPIUrls.getNextUrl());
        requests.add(getNexrUrlRequest);

        registerDeviceRequest = new FormUrlEncodedHttpRequest();
        registerDeviceRequest.setConnectionUrl(ScraperAPIUrls.registerDevice());
        requests.add(registerDeviceRequest);

        checkDeviceRequest = new HttpRequest();
        checkDeviceRequest.setConnectionUrl(ScraperAPIUrls.checkDevice());
        requests.add(checkDeviceRequest);

        badUrlRequest = new HttpRequest();
        requests.add(badUrlRequest);
    }

    public void setDeviceId(String deviceId){
        for(HttpRequest request : requests){
            request.addHeader("Device-Id",deviceId);
        }
    }

    public void setApiKey(ApiKey apiKey) {
        for(HttpRequest request : requests){
            request.addHeader("Authorization","Token " + apiKey.getValue());
        }
    }
}
