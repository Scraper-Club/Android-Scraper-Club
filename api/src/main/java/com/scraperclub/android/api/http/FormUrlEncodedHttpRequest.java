package com.scraperclub.android.api.http;

import java.util.Map;

import io.reactivex.Single;

public class FormUrlEncodedHttpRequest extends HttpRequest {

    StringBuilder requestBodyBuilder;

    public FormUrlEncodedHttpRequest() {
        super();
        super.addHeader("Content-Type","application/x-www-form-urlencoded");
    }

    public void addRequestParams(Map<String,String> params){
        if(requestBodyBuilder == null)
            requestBodyBuilder = new StringBuilder();

        for(Map.Entry<String,String> entry: params.entrySet()){
            requestBodyBuilder.append(
                    String.format("%s=%s&",
                            entry.getKey(),
                            entry.getValue())
            );
        }
    }

    @Override
    public Single<HttpResponse> doRequest() {
        super.setRequestBody(requestBodyBuilder.toString());
        requestBodyBuilder = null;
        return super.doRequest();
    }
}
