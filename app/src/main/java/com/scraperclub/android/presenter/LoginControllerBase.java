package com.scraperclub.android.presenter;

import com.scraperclub.android.api.ScraperAPI;
import com.scraperclub.android.api.model.ApiKey;
import com.scraperclub.android.views.authentication.LoginView;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class LoginControllerBase<T> implements SingleObserver<ApiKey> {

    protected LoginView loginView;
    protected ScraperAPI scraperAPI;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected LoginControllerBase(ScraperAPI scraperAPI) {
        this.scraperAPI = scraperAPI;
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public abstract void login(T credentials);

    @Override
    public void onSubscribe(Disposable d) {
        compositeDisposable.add(d);
    }

    @Override
    public void onSuccess(ApiKey apiKey) {
        loginView.hideProgress();
        loginView.onSuccessResult(apiKey);
    }

    @Override
    public void onError(Throwable e) {
        loginView.hideProgress();
        if(e instanceof IOException){
            if(e instanceof ConnectException){
                loginView.displayErrorMessage("Failed to connect server");
            }else if(e instanceof SocketException){
                loginView.displayErrorMessage("Network connection is unavailable");
            }
        }else{
            loginView.displayErrorMessage(e.getMessage());
        }
    }

    public void destroy(){
        compositeDisposable.clear();
    }
}
