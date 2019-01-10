package com.scraperclub.android.presenter;

import com.scraperclub.android.ScraperCore;
import com.scraperclub.android.scraping.ScrapingEndedReason;

abstract class MainPresenterBase implements MainPresenter {

    protected MainPresenter.View view;
    protected ScraperCore core;

    private NavigationCommand logout;
    protected NavigationCommand scrapPrivate;
    protected NavigationCommand scrapPublic;

    protected boolean privatePoolEnabled;
    protected boolean publicPoolEnabled;

    protected boolean allowPrivate = false;

    public MainPresenterBase(View view) {
        this.view = view;
        logout = view.getCommand(View.NavigationAction.LOGOUT);
        scrapPrivate = view.getCommand(View.NavigationAction.SCRAP_PRIVATE);
        scrapPublic = view.getCommand(View.NavigationAction.SCRAP_PUBLIC);
        core = ScraperCore.getInstance();
        view.displayApiKey(core.getToken().getValue());

        setListeners();
    }

    private NavigationCommand.Listener scrapCommandListener = (code, data) ->{
        switch (code) {
            case ScrapingEndedReason.NO_URLS:
                view.appendLogMessage("No available url for scraping");
                break;
            case ScrapingEndedReason.IP_LIMIT:
                view.appendLogMessage("IP Limit reached");
                break;
            case ScrapingEndedReason.FAILURE:
                if(data!=null) {
                    view.appendLogMessage("Scraping stopped: " + data.getStringExtra(ScrapingEndedReason.REASON_KEY));
                }
                view.appendLogMessage("Scraping stopped because of failure");
                view.disableScraping();
                break;
            case ScrapingEndedReason.USER_STOP:
                view.appendLogMessage("Scraping stopped by user");
                view.disableScraping();
                break;
        }
    };


    private void setListeners(){
        scrapPublic.setListener(scrapCommandListener);
        scrapPrivate.setListener(scrapCommandListener);
    }

    @Override
    public void privatePoolSwitchChanged(boolean state) {
        privatePoolEnabled = state;
        if(state)
            tryStartScrapingPrivate();
    }

    @Override
    public void publicPoolSwitchChanged(boolean state) {
        publicPoolEnabled = state;
        if(state)
            tryStartScrapingPublic();
    }

    @Override
    public void logout() {
        ScraperCore.getInstance().clearPrefs();
        view.navigate(logout);
    }

    @Override
    public void destroy() {
        scrapPrivate.setListener(null);
        scrapPublic.setListener(null);
        this.view = null;
        this.core = null;
    }

    abstract void tryStartScrapingPrivate();
    abstract void tryStartScrapingPublic();

}
