package com.scraperclub.android.api.errors;

public class DeviceNotRegisteredException extends ScraperAPIException {
    public DeviceNotRegisteredException() {
        super();
    }

    public DeviceNotRegisteredException(String message) {
        super(message);
    }

    public DeviceNotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeviceNotRegisteredException(Throwable cause) {
        super(cause);
    }
}
