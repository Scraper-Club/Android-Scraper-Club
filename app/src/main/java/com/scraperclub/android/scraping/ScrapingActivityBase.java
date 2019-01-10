package com.scraperclub.android.scraping;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class ScrapingActivityBase extends AppCompatActivity implements ScrapingView {

    protected ScrapingWebView scrapingCore;
    protected ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scrapingCore = new ScrapingWebView(this);
        setContentView(scrapingCore);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Stop",(d, b)-> stopScrapingImmediately());

        dialog.setTitle("Add your targets on ScraperClub.com from laptop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startScraping();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("DEBUG_SCRAP", "onPause: ");
        stopScrapingImmediately();
    }

    @Override
    public void onBackPressed() {
        stopScrapingImmediately();
    }


    public abstract void finishedNoUrls();
    public void finishedByUser(){
        finishScraping(ScrapingEndedReason.USER_STOP);
    }
    public void finishedIPLimit(){
        finishScraping(ScrapingEndedReason.IP_LIMIT);
    }
    public void finishFailure(){
        finishScraping(ScrapingEndedReason.FAILURE);
    }

    protected abstract void finishScraping(int reason);
}
