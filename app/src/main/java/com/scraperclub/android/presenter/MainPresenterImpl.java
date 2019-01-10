package com.scraperclub.android.presenter;

import com.scraperclub.android.api.errors.UnauthorizedException;
import com.scraperclub.android.api.model.DeviceStatistic;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MainPresenterImpl extends MainPresenterBase {

    private int availablePublicUrl;
    private int availablePrivateUrl;



    private CompositeDisposable compositeDisposable;

    public MainPresenterImpl(View view) {
        super(view);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    void tryStartScrapingPrivate() {
        if(availablePrivateUrl > 0 && privatePoolEnabled){
            view.navigate(scrapPrivate);
        }
    }

    @Override
    void tryStartScrapingPublic() {
        if(availablePublicUrl > 0 && publicPoolEnabled){
            view.navigate(scrapPublic);
        }
    }

    private Observable<DeviceStatistic> startUpdateStats() {
        return Observable.merge(
                    Observable.just(core.getApi().getDeviceStatistic()),
                    Observable.interval(5, TimeUnit.SECONDS).map(__ -> core.getApi().getDeviceStatistic())
                )
                .map(Single::blockingGet)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(err -> {
                    view.displayServerStatus(false);
                    availablePublicUrl = 0;
                    availablePrivateUrl = 0;
                    if (err instanceof UnauthorizedException) {
                        logout();
                    }
                    err.printStackTrace();
                })
                .retryWhen(handler -> handler.delay(5, TimeUnit.SECONDS));
    }

    public void destroy(){
        compositeDisposable.clear();
        super.destroy();
    }

    @Override
    public void startUpdate() {
        compositeDisposable.add(
            startUpdateStats()
                    .subscribe(
                            statistic->{
                                view.displayDeviceStats(statistic);
                                view.displayServerStatus(true);
                                availablePublicUrl = statistic.getAvailableUrls();
                                availablePrivateUrl = statistic.getPrivatePool();
                                if(availablePrivateUrl>0) tryStartScrapingPrivate();
                                if(availablePublicUrl>0) tryStartScrapingPublic();

                                if(allowPrivate != statistic.isAllowPrivate()){
                                    allowPrivate = statistic.isAllowPrivate();
                                    if(allowPrivate)
                                        view.changeMode(View.Mode.PRIVATE);
                                    else
                                        view.changeMode(View.Mode.PUBLIC);
                                }
                            }
                    )
        );
    }

    @Override
    public void stopUpdate() {
        compositeDisposable.clear();
    }
}
