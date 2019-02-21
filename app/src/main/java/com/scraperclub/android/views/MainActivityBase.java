package com.scraperclub.android.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.scraperclub.android.R;
import com.scraperclub.android.ScraperApp;

public abstract class MainActivityBase extends AppCompatActivity{
    protected ImageView serverStatusImg;
    protected TextView serverStatusLabel;

    protected TextView apiKeyTextView;
    protected TextView totalScrapsTextView;
    protected TextView lastHourScrapsTextView;
    protected TextView availableUrlsTextView;
    protected TextView currentIpTextView;

    protected Switch enableScraping;

    protected TextView logTextView;

    protected Switch enableScrapingPrivate;
    protected TextView privatePoolLabel;
    protected TextView privatePoolURls;

    protected TextView serverUrl;

    protected ScrollView logScrollView;
    protected TextView header;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_stats);
        serverStatusLabel = findViewById(R.id.serverStatusLabel);
        serverStatusImg = findViewById(R.id.serverStatusImg);

        apiKeyTextView = findViewById(R.id.apiKeyTextView);
        currentIpTextView = findViewById(R.id.currentIpTextView);
        totalScrapsTextView = findViewById(R.id.totalScrapsTextView);
        lastHourScrapsTextView = findViewById(R.id.todayScrapsTextView);
        availableUrlsTextView = findViewById(R.id.availableUrlsTextView);

        enableScraping = findViewById(R.id.enable_scraping_switch);

        logTextView = findViewById(R.id.log_text_view);

        enableScrapingPrivate = findViewById(R.id.scrapPrivateSwitch);
        privatePoolLabel = findViewById(R.id.privateUrlsLabel);
        privatePoolURls = findViewById(R.id.privateUrlsTextView);

        logScrollView = findViewById(R.id.log_scroll_view);
        header = findViewById(R.id.header_view);
        serverUrl = findViewById(R.id.stats_server_url);

        serverUrl.setText(ScraperApp.getInstance().getServerUrl());
    }
}
