package com.scraperclub.android.scraping;

public class ScrapingFailedException extends Exception {
    public ScrapingFailedException() {
    }

    public ScrapingFailedException(String message) {
        super(message);
    }

    public ScrapingFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScrapingFailedException(Throwable cause) {
        super(cause);
    }
}
