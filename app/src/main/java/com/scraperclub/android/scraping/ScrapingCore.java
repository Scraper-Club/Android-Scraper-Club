package com.scraperclub.android.scraping;

import com.scraperclub.android.api.model.ScraperResult;
import com.scraperclub.android.api.model.ScraperUrl;

import io.reactivex.Single;

public interface ScrapingCore {
    Single<ScraperResult> startScraping(ScraperUrl url);
    void stopScrapingImmediately();
}
