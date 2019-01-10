package com.scraperclub.android.views.authentication;

import android.os.Bundle;

import com.scraperclub.android.api.model.ApiKey;
import com.scraperclub.android.presenter.ApiKeyLoginController;

public final class ApiKeyLoginActivity extends ApiKeyLoginActivityBase implements ApiKeyLoginView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setController(new ApiKeyLoginController(scraperCore.getApi()));
    }

    @Override
    protected ApiKey getInput() {
        String apiKey = apiKeyInput.getText().toString().trim();
        return new ApiKey(apiKey);
    }

    @Override
    public void apiKeyInvalid(String reason) {
        showSoftKeyboard(apiKeyInput);
        apiKeyInput.setError(reason);
    }

    @Override
    public void clearErrorMessages() {
        super.clearErrorMessages();
        apiKeyInput.setError(null);
    }

}
