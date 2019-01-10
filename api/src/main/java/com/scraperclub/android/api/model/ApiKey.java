package com.scraperclub.android.api.model;

import org.json.JSONObject;

import io.reactivex.functions.Function;

public class ApiKey {
    String value;

    private static JsonMapper jsonMapper;

    public ApiKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static JsonMapper getJsonMapper(){
        if(jsonMapper==null) jsonMapper = new JsonMapper();
        return jsonMapper;
    }

    private static class JsonMapper implements Function<JSONObject, ApiKey> {
        @Override
        public ApiKey apply(JSONObject jsonObject) throws Exception {
            return new ApiKey(jsonObject.getString("token"));
        }
    }
}
