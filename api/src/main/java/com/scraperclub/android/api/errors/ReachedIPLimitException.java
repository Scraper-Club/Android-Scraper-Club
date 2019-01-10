package com.scraperclub.android.api.errors;

public class ReachedIPLimitException extends ScraperAPIException {
    public ReachedIPLimitException() {
        super();
    }

    public ReachedIPLimitException(String message) {
        super(message);
    }

    public ReachedIPLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReachedIPLimitException(Throwable cause) {
        super(cause);
    }
}
