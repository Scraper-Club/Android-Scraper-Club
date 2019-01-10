package com.scraperclub.android.scraping.experimental;

import com.scraperclub.android.api.model.ScraperResult;
import com.scraperclub.android.api.model.ScraperUrl;
import com.scraperclub.android.scraping.ScrapingCore;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ScrapingProcessor{
    public enum State{
        IDLE, SCRAPING, HANDLING_RESULT
    }
    protected ScrapingResultHandler resultHandler;
    protected ScrapingCore core;

    private StateMachine stateMachine;

    public ScrapingProcessor(ScrapingCore core, ScrapingResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        this.core = core;
        stateMachine = new StateMachine();
    }

    public State getCurrentState() {
        return stateMachine.getCurrentState();
    }

    public void setListener(StateMachine.OnStateChangedListener listener) {
        stateMachine.setListener(listener);
    }

    public Completable handleScrapingResult(ScraperResult result) {
        return resultHandler.handleScrapingResult(result)
                .doOnSubscribe(__->stateMachine.setUploading(true))
                .doOnTerminate(()->stateMachine.setUploading(false));
    }

    public Single<ScraperResult> startScraping(ScraperUrl url) {
        return core.startScraping(url)
                .doOnSubscribe(__->stateMachine.setScraping(true))
                .doFinally(()->stateMachine.setScraping(false));
    }
}
