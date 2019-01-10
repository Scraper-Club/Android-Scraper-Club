package com.scraperclub.android.views.authentication;


import com.scraperclub.android.api.model.ApiKey;

public interface LoginView {

    void showProgress();
    void hideProgress();
    void displayErrorMessage(String message);
    void hideKeyboard();
    void onSuccessResult(ApiKey apiKey);

    void clearErrorMessages();

}
