package com.scraperclub.android.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import com.scraperclub.android.presenter.MainPresenter;
import com.scraperclub.android.presenter.MainPresenterImpl;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends MainViewBase {
    public static String FIRST_TIME_KEY = "first_time";
    private CompositeDisposable compositeDisposable;

    protected MainPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenterImpl(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        enableScraping.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked) enableScrapingPrivate.setChecked(false);
            presenter.publicPoolSwitchChanged(isChecked);
        }));
        enableScrapingPrivate.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked) enableScraping.setChecked(false);
            presenter.privatePoolSwitchChanged(isChecked);
        }));
        compositeDisposable = new CompositeDisposable();
        Intent intent = getIntent();
        if(intent!=null && intent.getBooleanExtra(FIRST_TIME_KEY,false)){
            compositeDisposable.add(
                    Observable.interval(1, TimeUnit.SECONDS)
                            .map(count -> 10 - count)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                count->{
                                    if(count==0){
                                        enableScraping.setChecked(true);
                                        compositeDisposable.clear();
                                    }else{
                                        appendLogMessage(String.format(Locale.US,"Scraping will start in %d seconds.", count));
                                    }
                                }
                    )
            );
        }

        header.setMovementMethod(LinkMovementMethod.getInstance());
        header.setTextSize(20f);
        header.setGravity(Gravity.CENTER);
        header.setText("To Add your scraping targets go to ScraperClub.com on your laptop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopUpdate();
        compositeDisposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter=null;
    }

    @Override
    public void changeMode(MainPresenter.View.Mode newMode) {
        switch (newMode){
            case PUBLIC:
                enableScrapingPrivate.setChecked(false);
                enableScrapingPrivate.setVisibility(View.GONE);
                privatePoolLabel.setVisibility(View.GONE);
                privatePoolURls.setVisibility(View.GONE);
                break;
            case PRIVATE:
                enableScrapingPrivate.setVisibility(View.VISIBLE);
                privatePoolLabel.setVisibility(View.VISIBLE);
                privatePoolURls.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Logout")
                .setOnMenuItemClickListener(
                        i -> {
                            presenter.logout();
                            return true;
                        });
        return super.onCreateOptionsMenu(menu);
    }
}
