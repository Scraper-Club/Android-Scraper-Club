package com.scraperclub.android.api.errors;

public class DeviceBlockedException extends ScraperAPIException {
    public DeviceBlockedException() {
        super();
    }

    public DeviceBlockedException(String message) {
        super(message);
    }

    public DeviceBlockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeviceBlockedException(Throwable cause) {
        super(cause);
    }
}
