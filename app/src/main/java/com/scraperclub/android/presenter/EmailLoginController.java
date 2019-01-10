package com.scraperclub.android.presenter;

import com.scraperclub.android.api.ScraperAPI;
import com.scraperclub.android.api.model.UserCredentials;
import com.scraperclub.android.views.authentication.EmailLoginView;
import com.scraperclub.android.views.authentication.LoginView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class EmailLoginController extends LoginControllerBase<UserCredentials>{

    private EmailLoginView emailLoginView;

    public EmailLoginController(ScraperAPI scraperAPI) {
        super(scraperAPI);
    }

    @Override
    public void setLoginView(LoginView loginView) {
        super.setLoginView(loginView);
        emailLoginView = (EmailLoginView)loginView;
    }

    @Override
    public void login(UserCredentials userCredentials) {
        loginView.hideKeyboard();
        loginView.clearErrorMessages();
        if(validateUserCredentials(userCredentials)){
            scraperAPI.login(userCredentials)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(__->loginView.showProgress())
                    .subscribe(this);
        }
    }

    private boolean validateUserCredentials(UserCredentials userCredentials){
        return validateEmail(userCredentials.getEmail()) && validatePassword(userCredentials.getSecret());
    }

    private boolean validateEmail(String email){
        if (email.isEmpty()) {
            emailLoginView.emailInvalid("Please provide an email");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLoginView.emailInvalid("Please provide valid email");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password){
        if (password.isEmpty()) {
            emailLoginView.passwordInvalid("Please provide password");
            return false;
        }
        return true;
    }
}
