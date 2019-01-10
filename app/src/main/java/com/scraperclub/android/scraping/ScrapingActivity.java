package com.scraperclub.android.scraping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.scraperclub.android.api.ScraperAPI;
import com.scraperclub.android.api.errors.NoAvailableUrlsExceptions;
import com.scraperclub.android.api.errors.ReachedIPLimitException;
import com.scraperclub.android.api.model.ScraperResult;
import com.scraperclub.android.api.model.ScraperUrl;

import com.scraperclub.android.ScraperCore;
import com.scraperclub.android.scraping.experimental.ScrapingProcessor;
import com.scraperclub.android.scraping.experimental.StateMachine;
import com.scraperclub.android.scraping.experimental.UploadToServerHandler;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class ScrapingActivity extends ScrapingActivityBase{

    public static String SCRAP_POOL = "pool";
    public static final int PRIVATE_POOL = 0;
    public static final int PUBLIC_POOL = 1;
    private final String PRIVATE = "private";
    private final String PUBLIC = "public";

    private CompositeDisposable compositeDisposable;
    private ScraperAPI api = ScraperCore.getInstance().getApi();

    private PublishSubject<ScraperUrl> targetUrlProxy;
    private PublishSubject<ScraperResult> scrapedResultProxy;

    private ScrapingProcessor processor;

    private String pool;

    private boolean noUrls;
    private boolean idle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent data = getIntent();
        if(data == null || data.getIntExtra(SCRAP_POOL,-1) == -1){
            setResult(ScrapingEndedReason.FAILURE,new Intent().putExtra(ScrapingEndedReason.REASON_KEY,"No pool selected"));
            finish();
            return;
        }else{
            switch (data.getIntExtra(SCRAP_POOL,-1)){
                case PRIVATE_POOL:
                    pool = PRIVATE;
                    break;
                case PUBLIC_POOL:
                    pool = PUBLIC;
                    break;

                    default:
                        setResult(ScrapingEndedReason.FAILURE,new Intent().putExtra(ScrapingEndedReason.REASON_KEY,"Unknown pool code"));
                        finish();
                        return;
            }
        }

        compositeDisposable = new CompositeDisposable();

        targetUrlProxy = PublishSubject.create();
        scrapedResultProxy = PublishSubject.create();

        UploadToServerHandler resultHandler = new UploadToServerHandler(api);
        processor = new ScrapingProcessor(scrapingCore,resultHandler);
        processor.setListener(new StateMachine.OnStateChangedListener() {
            @Override
            public void onStateChanged(ScrapingProcessor.State newState) {
                Log.d("STATE",newState.name());
                switch (newState){
                    case IDLE:
                        idle=true;
                        finishedNoUrls();
                        break;
                    case SCRAPING:
                    case HANDLING_RESULT:
                        idle=false;
                        break;
                }
            }
        });

        compositeDisposable.add(
                subscribeUploading()
        );

        compositeDisposable.add(
                subscribeEngine()
        );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private Disposable subscribeEngine(){
        return targetUrlProxy.subscribe(
                urlinfo-> {
                    dialog.setMessage(urlinfo.toString());
                    dialog.show();
                    compositeDisposable.add(
                            processor.startScraping(urlinfo)
                                    .subscribe(
                                            result->{
                                                scrapedResultProxy.onNext(result);
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                startScraping();
                                            },
                                            err -> {
                                                showToastMsg(err.getMessage());
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                startScraping();
                                            }
                                    )
                    );
                }
        );
    }
    private Disposable subscribeUploading(){
        return scrapedResultProxy.subscribe(
                result -> {
                    compositeDisposable.add(
                            processor.handleScrapingResult(result)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            () -> showToastMsg("Scrap uploaded"),
                                            err -> showToastMsg("Failed to upload scrap")
                                    )
                    );
                });
    }
    private Disposable subscribeUrlSource(){
        return api.getNextUrl(pool)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        targetUrlProxy::onNext,
                        this::handleError
                );
    }

    @Override
    protected void finishScraping(int reason) {
        compositeDisposable.clear();
        scrapingCore.stopScrapingImmediately();
        setResult(reason);
        finish();
    }

    @Override
    public void startScraping(){
        compositeDisposable.add(
              subscribeUrlSource()
        );
    }

    private void handleError(Throwable t){
        if(t instanceof NoAvailableUrlsExceptions) {
            Log.d("STATE","NO_URLS");
            noUrls=true;
            finishedNoUrls();
        }
        else if(t instanceof ReachedIPLimitException)finishedIPLimit();
//TODO handle device blocking
//        else if(t instanceof DeviceBlockedException);
        else finishFailure();
    }

    public void finishedNoUrls(){
        if(idle && noUrls){
            finishScraping(ScrapingEndedReason.NO_URLS);
        }
    }

    private void showToastMsg(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void stopScrapingImmediately() {
        finishedByUser();
    }
}