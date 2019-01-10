package com.scraperclub.android.scraping;

public class ScrapingFailedException extends Exception {

    private int urlId;

    public ScrapingFailedException(String message, int urlId) {
        super(message);
        this.urlId = urlId;
    }

    public ScrapingFailedException(Throwable cause, int urlId) {
        super(cause);
        this.urlId = urlId;
    }

    public int getUrlId() {
        return urlId;
    }
}
