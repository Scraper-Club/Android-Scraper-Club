package com.scraperclub.android.api.model;

import org.json.JSONObject;

import io.reactivex.functions.Function;

public class ScraperUrlConfig {
    private int waitTime;
    private int scrollDelay;
    private int scrollTimes;

    private static JsonMapper jsonMapper = new JsonMapper();

    public ScraperUrlConfig(int waitTime, int scrollDelay, int scrollTimes) {
        this.waitTime = waitTime;
        this.scrollDelay = scrollDelay;
        this.scrollTimes = scrollTimes;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getScrollDelay() {
        return scrollDelay;
    }

    public int getScrollTimes() {
        return scrollTimes;
    }

    public static JsonMapper getJsonMapper() {
        return jsonMapper;
    }

    static class JsonMapper implements Function<JSONObject,ScraperUrlConfig>{

        @Override
        public ScraperUrlConfig apply(JSONObject jsonObject) throws Exception {
            return new ScraperUrlConfig(
                    jsonObject.getInt("wait_time"),
                    jsonObject.getInt("scroll_delay"),
                    jsonObject.getInt("scroll_count")
            );
        }
    }

}
