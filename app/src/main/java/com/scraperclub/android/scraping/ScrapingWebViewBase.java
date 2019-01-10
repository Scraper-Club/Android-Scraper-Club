package com.scraperclub.android.scraping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class ScrapingWebViewBase extends WebView {
    public static final String TAG = "ScrapingWebViewBase";

    protected String MAGIC = "SCRAP";

    protected Timer timeoutTimer;
    protected int scrapTimeout = 30000;

    protected int scrapDelay = 3000;
    protected int scrollDelay = 500;
    protected int scrollTimes = 3;

    protected CompositeDisposable timeoutDisposable = new CompositeDisposable();

    public ScrapingWebViewBase(Context context) {
        super(context);
        setupSettings();
        setupClients();
        timeoutTimer = new Timer();
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void setupSettings(){
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(settings.getUserAgentString().replaceAll("(?i)mobile",""));
    }
    protected void setupClients(){
        setWebChromeClient(new ScrapingChromeClient());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            setWebViewClient(new ScrapingWebClientNew());
        }else{
            setWebViewClient(new ScrapingWebClientOld());
        }
    }

    public void blockTouches(){
        setOnTouchListener((a,b)->true);
    }

    public void setScrapTimeout(int scrapTimeout) {
        this.scrapTimeout = scrapTimeout;
    }

    public void setScrapDelay(int scrapDelay) {
        this.scrapDelay = scrapDelay;
    }

    public void setScrollDelay(int scrollDelay) {
        this.scrollDelay = scrollDelay;
    }

    public void setScrollTimes(int scrollTimes) {
        this.scrollTimes = scrollTimes;
    }



    protected class ScrapingWebClient extends WebViewClient {
        boolean redirecting = false;
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            Log.d(TAG, "Should override " + url);
            redirecting = true;
            super.shouldOverrideUrlLoading(view, url);
            view.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String address) {
            Log.d(TAG, "onPageFinished: " + address);
            if (redirecting){
                redirecting = false;
                return;
            }
            StringBuilder javascript = new StringBuilder();
            for (int i = 1; i <= scrollTimes; i++) {
                javascript.append("setTimeout(function(){window.scrollTo(0,document.body.scrollHeight);}," + (scrollDelay * i) + ");");
            }
            javascript.append("setTimeout(function(){console.log('" + MAGIC + "'+document.documentElement.outerHTML);}," + (scrapDelay + (scrollTimes * scrollDelay)) + ");");
            view.loadUrl("javascript:" + javascript.toString());
            Log.d(TAG, javascript.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected class ScrapingWebClientNew extends ScrapingWebClient{

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            switch (error.getErrorCode()){
                case WebViewClient.ERROR_CONNECT:
                    Log.d(TAG,"ERROR_CONNECT");
                    scrapFailed("connection error");
                    break;
                case WebViewClient.ERROR_TIMEOUT:
                    Log.d(TAG,"TIMEOUT_ERROR");
                    scrapFailed("Timeout error");
                    break;
                case WebViewClient.ERROR_HOST_LOOKUP:
                    Log.d(TAG,"ERROR_HOST_LOOKUP");
                    startTimer();
                    break;
            }
        }
    }

    protected class ScrapingWebClientOld extends ScrapingWebClient{
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            switch (errorCode){
                case WebViewClient.ERROR_CONNECT:
                    Log.d(TAG,"ERROR_CONNECT");
                    scrapFailed("connection error");
                    break;
                case WebViewClient.ERROR_TIMEOUT:
                    Log.d(TAG,"TIMEOUT_ERROR");
                    scrapFailed("Timeout error");
                    break;
                case WebViewClient.ERROR_HOST_LOOKUP:
                    Log.d(TAG,"ERROR_HOST_LOOKUP");
                    startTimer();
                    break;
            }
        }
    }

    protected void startTimer(){
        int timeout = 2 * (scrapDelay + (scrollTimes * scrollDelay)) / 1000;
        timeoutDisposable.clear();
        timeoutDisposable.add(
                Observable.interval(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(count -> timeout - count)
                        .subscribe(
                                next->{
                                    Log.d(TAG, "startTimer: " + next);
                                    if(next == 0){
                                        scrapFailed("Timeout error");
                                        stopTimer();
                                    }
                                }
                        )
        );
    }

    protected void stopTimer(){
        timeoutDisposable.clear();
    }

    protected class ScrapingChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if(consoleMessage.message().startsWith(MAGIC)){
                String msg = consoleMessage.message().substring(MAGIC.length());
                timeoutDisposable.clear();
                scrapCompleted(msg);
            }
            return super.onConsoleMessage(consoleMessage);
        }
    }

    protected abstract void scrapCompleted(String result);
    protected abstract void scrapFailed(String error);
}
