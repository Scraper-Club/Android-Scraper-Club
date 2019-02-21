package com.scraperclub.android.views.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.scraperclub.android.api.APIUrlsResolver;
import com.scraperclub.android.api.model.UserCredentials;
import com.scraperclub.android.presenter.EmailLoginController;

public final class EmailLoginActivity extends EmailLoginActivityBase implements EmailLoginView {

    private static final int API_KEY_REQUEST_CODE = 4321;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setController(new EmailLoginController(scraperCore.getApi()));
        apiKeyLink.setOnClickListener(
                v->{
                    Intent apikeyLogin = new Intent(EmailLoginActivity.this,ApiKeyLoginActivity.class);
                    startActivityForResult(apikeyLogin,API_KEY_REQUEST_CODE);
                }
        );

        signupLink.setOnClickListener(
                v->{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(APIUrlsResolver.SIGNUP_URL));
                    startActivity(browserIntent);
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == API_KEY_REQUEST_CODE && resultCode == RESULT_OK){
            setResult(resultCode, data);
            finish();
        }
    }

    @Override
    protected UserCredentials getInput() {
        String email = emailEditText.getText().toString().trim();
        String secret = passwordEditText.getText().toString().trim();
        return new UserCredentials(email,secret);
    }

    @Override
    public void emailInvalid(String reason) {
        showSoftKeyboard(emailEditText);
        emailEditText.setError(reason);
    }

    @Override
    public void passwordInvalid(String reason) {
        showSoftKeyboard(passwordEditText);
        passwordEditText.setError(reason);
    }

    @Override
    public void clearErrorMessages() {
        super.clearErrorMessages();
        emailEditText.setError(null);
        passwordEditText.setError(null);
    }
}
