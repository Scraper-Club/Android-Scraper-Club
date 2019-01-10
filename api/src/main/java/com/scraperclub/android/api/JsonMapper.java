package com.scraperclub.android.api;
import com.scraperclub.android.api.http.HttpResponse;

import org.json.JSONObject;

import io.reactivex.functions.Function;

public class JsonMapper implements Function<HttpResponse, JSONObject> {
    @Override
    public JSONObject apply(HttpResponse httpResponse) throws Exception {
        return new JSONObject(httpResponse.getContent());
    }
}
