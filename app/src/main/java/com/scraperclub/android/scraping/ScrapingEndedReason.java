package com.scraperclub.android.scraping;

public class ScrapingEndedReason {
    public static final int NO_URLS = 2000;
    public static final int IP_LIMIT = 2002;
    public static final int FAILURE = 2003;
    public static final int USER_STOP = 2004;

    public static final String REASON_KEY = "reason";
}
