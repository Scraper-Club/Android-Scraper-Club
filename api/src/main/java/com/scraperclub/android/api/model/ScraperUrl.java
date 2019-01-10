package com.scraperclub.android.api.model;

import org.json.JSONObject;

import java.net.URL;

import io.reactivex.functions.Function;

public class ScraperUrl {

    private int id;
    private URL url;

    private ScraperUrlConfig config;

    private static JsonMapper jsonMapper = new JsonMapper();

    public ScraperUrl(int id, URL url, ScraperUrlConfig config) {
        this.id = id;
        this.url = url;
        this.config = config;
    }

    public int getId() {
        return id;
    }

    public URL getUrl() {
        return url;
    }

    public ScraperUrlConfig getConfig() {
        return config;
    }

    public int getWaitTime() {
        return config.getWaitTime();
    }

    public int getScrollDelay() {
        return config.getScrollDelay();
    }

    public int getScrollTimes() {
        return config.getScrollTimes();
    }

    public static JsonMapper getJsonMapper(){
        return jsonMapper;
    }

    private static class JsonMapper implements Function<JSONObject,ScraperUrl>{

        @Override
        public ScraperUrl apply(JSONObject jsonObject) throws Exception {
            ScraperUrlConfig config = ScraperUrlConfig.getJsonMapper()
                    .apply(jsonObject.getJSONObject("config"));
            return new ScraperUrl(
                    jsonObject.getInt("id"),
                    new URL(jsonObject.getString("url")),
                    config
            );
        }
    }

    @Override
    public String toString() {
        return this.url.toString();
    }
}
