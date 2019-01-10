package com.scraperclub.android.scraping.experimental;

import com.scraperclub.android.api.model.ScraperResult;

import io.reactivex.Completable;

public interface ScrapingResultHandler {
    Completable handleScrapingResult(ScraperResult result);
}
