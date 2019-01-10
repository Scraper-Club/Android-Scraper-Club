package com.scraperclub.android.scraping.experimental;

import com.scraperclub.android.api.model.ScraperUrl;

import io.reactivex.Single;

public interface TargetUrlProvider {
    Single<ScraperUrl> getNext();
}
