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
        requests.add(loginRequest);

        verifyApiKeyRequest = new FormUrlEncodedHttpRequest();
        requests.add(verifyApiKeyRequest);


        getDeviceStatisticRequest = new HttpRequest();
        requests.add(getDeviceStatisticRequest);

        getNexrUrlRequest = new HttpRequest();
        requests.add(getNexrUrlRequest);

        registerDeviceRequest = new FormUrlEncodedHttpRequest();
        requests.add(registerDeviceRequest);

        checkDeviceRequest = new HttpRequest();
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
