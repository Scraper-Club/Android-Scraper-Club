package com.scraperclub.android.scraping;

import android.content.Context;

import com.scraperclub.android.api.model.ScraperResult;
import com.scraperclub.android.api.model.ScraperUrl;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ScrapingWebView extends ScrapingWebViewBase implements ScrapingCore {

    private ScraperUrl currentUrl;

    public ScrapingWebView(Context context) {
        super(context);
        blockTouches();
        setInitialScale(100);
    }

    private SingleEmitter<ScraperResult> resultEmitter;

    @Override
    public Single<ScraperResult> startScraping(ScraperUrl urlInfo) {
        Single<ScraperResult> result = Single.create(emitter -> {
            currentUrl = urlInfo;
            String url = urlInfo.getUrl().toString();
            this.setScrapDelay(urlInfo.getWaitTime() * 1000);
            this.setScrollTimes(urlInfo.getScrollTimes());
            this.setScrollDelay(urlInfo.getScrollDelay() * 1000);
            super.loadUrl(url);
            resultEmitter = emitter;
        });
        return result.subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void stopScrapingImmediately() {
        resultEmitter = null;
    }

    public void scrapCompleted(String result){
        ScraperResult scraperResult = new ScraperResult(currentUrl,result);
        currentUrl = null;
        if(resultEmitter!=null && !resultEmitter.isDisposed()){
            resultEmitter.onSuccess(scraperResult);
        }
    }

    @Override
    protected void scrapFailed(String errorMessage) {
        int urlId = currentUrl.getId();
        currentUrl = null;
        if(resultEmitter!=null && !resultEmitter.isDisposed()){
            resultEmitter.onError(new ScrapingFailedException(errorMessage, urlId));
        }
    }
}
