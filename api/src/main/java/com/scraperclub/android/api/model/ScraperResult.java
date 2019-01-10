package com.scraperclub.android.api.model;

public class ScraperResult {

    private ScraperUrl url;
    private String result;

    public ScraperResult(ScraperUrl url, String result) {
        this.url = url;
        this.result = result;
    }

    public int getId() {
        return url.getId();
    }

    public ScraperUrl getUrl() {
        return url;
    }

    public String getResult() {
        return result;
    }
}
