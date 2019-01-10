package com.scraperclub.android.presenter;

import com.scraperclub.android.api.ScraperAPI;
import com.scraperclub.android.api.model.ApiKey;
import com.scraperclub.android.views.authentication.ApiKeyLoginView;
import com.scraperclub.android.views.authentication.LoginView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ApiKeyLoginController extends LoginControllerBase<ApiKey> {

    private ApiKeyLoginView apiKeyLoginView;

    public ApiKeyLoginController(ScraperAPI scraperAPI) {
        super(scraperAPI);
    }

    @Override
    public void setLoginView(LoginView loginView) {
        super.setLoginView(loginView);
        apiKeyLoginView = (ApiKeyLoginView)loginView;
    }

    @Override
    public void login(ApiKey credentials) {
        loginView.hideKeyboard();
        loginView.clearErrorMessages();
        if(validateInput(credentials)) {
            compositeDisposable.add(
                scraperAPI.verifyApiKey(credentials)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(__ -> loginView.showProgress())
                    .subscribe(()->onSuccess(credentials),this::onError)
            );
        }
    }

    private boolean validateInput(ApiKey input){
        if(input.getValue().isEmpty()){
            apiKeyLoginView.apiKeyInvalid("Please provide api key");
            return false;
        }
        return true;
    }

}
