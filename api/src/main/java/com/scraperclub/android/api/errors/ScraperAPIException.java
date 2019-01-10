package com.scraperclub.android.api.errors;

public class ScraperAPIException extends Exception {
    public ScraperAPIException() {
        super();
    }

    public ScraperAPIException(String message) {
        super(message);
    }

    public ScraperAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScraperAPIException(Throwable cause) {
        super(cause);
    }
}
