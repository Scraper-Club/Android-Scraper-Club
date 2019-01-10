package com.scraperclub.android.views;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.scraperclub.android.api.model.DeviceStatistic;
import com.scraperclub.android.R;
import com.scraperclub.android.StartupActivity;
import com.scraperclub.android.presenter.MainPresenter;
import com.scraperclub.android.presenter.NavigationCommand;
import com.scraperclub.android.scraping.ScrapingActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class MainViewBase extends MainActivityBase implements MainPresenter.View {

    @Override
    public void displayDeviceStats(DeviceStatistic deviceStatistic) {
        currentIpTextView.setText(deviceStatistic.getCurrentIP());
        totalScrapsTextView.setText(String.format(Locale.US, "%d", deviceStatistic.getTotalScrapes()));
        lastHourScrapsTextView.setText(String.format(Locale.US, "%d", deviceStatistic.getLastHourScrapes()));
        availableUrlsTextView.setText(String.format(Locale.US, "%d", deviceStatistic.getAvailableUrls()));

        privatePoolURls.setText(String.format(Locale.US, "%d", deviceStatistic.getPrivatePool()));
    }

    @Override
    public void displayServerStatus(boolean online) {
        if(online){
            serverStatusImg.setImageResource(R.drawable.ic_round_green_24dp);
            serverStatusLabel.setText(R.string.server_online);
        }else {
            serverStatusImg.setImageResource(R.drawable.ic_round_red_24dp);
            serverStatusLabel.setText(R.string.server_offline);
        }
    }

    @Override
    public void appendLogMessage(String message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[HH:mm:ss] ");
        logTextView.append(
                simpleDateFormat.format(new Date()) + message + "\n"
        );
        logScrollView.scrollTo(0,logTextView.getHeight());
    }

    @Override
    public void displayApiKey(String apiKey) {
        apiKeyTextView.setText(apiKey);
    }

    @Override
    public void navigate(NavigationCommand command) {
        command.navigate();
    }

    @Override
    public void disableScraping() {
        enableScraping.setChecked(false);
        enableScrapingPrivate.setChecked(false);
    }

    private NavigationCommand logoutCommand = new NavigationCommand() {
        @Override
        public void navigate() {
            setResult(StartupActivity.LOGOUT);
            finish();
        }
    };

    private final int SCRAP_PUBLIC_CODE = 1001;
    private NavigationCommand scrapPublicCommand = new NavigationCommand() {
        @Override
        public void navigate() {
            Intent scrap = new Intent(MainViewBase.this, ScrapingActivity.class);
            scrap.putExtra(ScrapingActivity.SCRAP_POOL,ScrapingActivity.PUBLIC_POOL);
            startActivityForResult(scrap, SCRAP_PUBLIC_CODE);
        }
    };

    private final int SCRAP_PRIVATE_CODE = 1000;
    private NavigationCommand scrapPrivateCommand = new NavigationCommand() {
        @Override
        public void navigate() {
            Intent scrap = new Intent(MainViewBase.this, ScrapingActivity.class);
            scrap.putExtra(ScrapingActivity.SCRAP_POOL,ScrapingActivity.PRIVATE_POOL);
            startActivityForResult(scrap, SCRAP_PRIVATE_CODE);
        }
    };

    @Override
    public NavigationCommand getCommand(NavigationAction action) {
        switch (action){
            case LOGOUT:
                return logoutCommand;
            case SCRAP_PUBLIC:
                return scrapPublicCommand;
            case SCRAP_PRIVATE:
                return scrapPrivateCommand;

                default:
                    throw new RuntimeException("Action not implemented " + action);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case SCRAP_PRIVATE_CODE:
                scrapPrivateCommand.onResult(resultCode, data);
                break;
            case SCRAP_PUBLIC_CODE:
                scrapPublicCommand.onResult(resultCode, data);
                break;
        }
    }

}
