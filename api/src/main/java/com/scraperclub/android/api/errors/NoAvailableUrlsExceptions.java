package com.scraperclub.android.api.errors;

public class NoAvailableUrlsExceptions extends ScraperAPIException {
    public NoAvailableUrlsExceptions() {
        super();
    }

    public NoAvailableUrlsExceptions(String message) {
        super(message);
    }

    public NoAvailableUrlsExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAvailableUrlsExceptions(Throwable cause) {
        super(cause);
    }
}
