package com.scraperclub.android.api;

import android.os.Build;

import com.scraperclub.android.api.http.HttpRequest;
import com.scraperclub.android.api.model.ApiKey;
import com.scraperclub.android.api.model.DeviceStatistic;
import com.scraperclub.android.api.model.ScraperResult;
import com.scraperclub.android.api.model.ScraperUrl;
import com.scraperclub.android.api.model.UserCredentials;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public final class ScraperHttpAPI extends ScraperHttpAPIBase {

    private HttpResponseParser parseHttp;
    private JsonMapper toJson;
    private ScraperServerResponseParser parseServerResponse;

    public ScraperHttpAPI() {
        super();
        toJson = new JsonMapper();
        parseHttp = new HttpResponseParser();
        parseServerResponse = new ScraperServerResponseParser();
    }

    private Single<JSONObject> makeHttpRequest(HttpRequest request){
        return request
                .doRequest()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(parseHttp)
                .map(toJson);
    }

    @Override
    public Single<DeviceStatistic> getDeviceStatistic() {
        return makeHttpRequest(getDeviceStatisticRequest)
                .doOnSuccess(parseServerResponse)
                .map(DeviceStatistic.getJsonMapper());
    }

    @Override
    public Single<ScraperUrl> getNextUrl() {
        getNexrUrlRequest.setMethod("POST");
        getNexrUrlRequest.setConnectionUrl(ScraperAPIUrls.getNextUrl());
        return makeHttpRequest(getNexrUrlRequest)
                .doOnSuccess(parseServerResponse)
                .map(ScraperUrl.getJsonMapper());
    }

    @Override
    public Single<ScraperUrl> getNextUrl(String pool) {
        getNexrUrlRequest.setMethod("GET");
        getNexrUrlRequest.setConnectionUrl(ScraperAPIUrls.getNextUrl(pool));
        return makeHttpRequest(getNexrUrlRequest)
                .doOnSuccess(parseServerResponse)
                .map(ScraperUrl.getJsonMapper());
    }

    @Override
    public Completable uploadResult(ScraperResult result) {
        uploadHtmlRequest.setConnectionUrl(ScraperAPIUrls.upload(result.getId()));
        uploadHtmlRequest.setRequestBody(result.getResult());

        return makeHttpRequest(uploadHtmlRequest)
                .doOnSuccess(parseServerResponse)
                .flatMapCompletable(response->Completable.create(
                        emitter -> {
                            if(!emitter.isDisposed())emitter.onComplete();
                        }
                ));
    }

    @Override
    public Completable checkDevice() {
        return makeHttpRequest(checkDeviceRequest)
                .doOnSuccess(parseServerResponse)
                .flatMapCompletable(response->Completable.create(
                        emitter -> {
                            if(!emitter.isDisposed())emitter.onComplete();
                        }
                ));
    }

    @Override
    public Completable registerDevice() {
        Map<String,String> params = new HashMap<>();
        params.put("manufacturer", Build.MANUFACTURER);
        params.put("model",Build.MODEL);
        params.put("api",Build.VERSION.SDK_INT + "");
        params.put("android_version",Build.VERSION.RELEASE);
        params.put("user_agent","test_useragent");
        registerDeviceRequest.addRequestParams(params);
        return makeHttpRequest(registerDeviceRequest)
                .doOnSuccess(parseServerResponse)
                .flatMapCompletable(response->Completable.create(
                        emitter -> {
                            if(!emitter.isDisposed())emitter.onComplete();
                        }
                ));
    }

    @Override
    public Completable verifyApiKey(ApiKey apiKey) {

        Map<String,String> params = new HashMap<>();
        params.put("api_key",apiKey.getValue());
        verifyApiKeyRequest.addRequestParams(params);

        return makeHttpRequest(verifyApiKeyRequest)
                .doOnSuccess(parseServerResponse)
                .flatMapCompletable(response->Completable.create(
                        emitter -> {
                            if(!emitter.isDisposed())emitter.onComplete();
                        }
                ));
    }

    @Override
    public Single<ApiKey> login(UserCredentials credentials) {
        Map<String,String> params = new HashMap<>();
        params.put("login",credentials.getEmail());
        params.put("secret",credentials.getSecret());
        loginRequest.addRequestParams(params);

        return makeHttpRequest(loginRequest)
                .doOnSuccess(parseServerResponse)
                .map(ApiKey.getJsonMapper());
    }
}
