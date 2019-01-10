package com.scraperclub.android.scraping.experimental;

import com.scraperclub.android.api.ScraperAPI;
import com.scraperclub.android.api.model.ScraperResult;

import io.reactivex.Completable;

public class UploadToServerHandler implements ScrapingResultHandler {

    private ScraperAPI api;

    public UploadToServerHandler(ScraperAPI api) {
        this.api = api;
    }

    @Override
    public Completable handleScrapingResult(ScraperResult result) {
        return api.uploadResult(result);
    }
}
