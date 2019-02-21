package com.scraperclub.android.views.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scraperclub.android.R;
import com.scraperclub.android.ScraperApp;
import com.scraperclub.android.api.model.ApiKey;
import com.scraperclub.android.ScraperCore;
import com.scraperclub.android.presenter.LoginControllerBase;

public abstract class LoginActivityBase<T> extends AppCompatActivity implements LoginView {

    protected ChangeServerDialog dialog;
    protected TextView changeServerLink;
    protected TextView serverUrlTextView;

    protected ProgressDialog progressDialog = null;
    protected String progressMessage;

    protected Button loginButton;
    protected LoginControllerBase<T> controller;

    protected Snackbar snackbar;
    protected ScraperCore scraperCore = ScraperCore.getInstance();


    protected void setController(LoginControllerBase<T> controller){
        this.controller = controller;
        controller.setLoginView(this);
        loginButton.setOnClickListener(
                v->{
                    controller.login(getInput());
                }
        );
        serverUrlTextView.setText(ScraperApp.getInstance().getServerUrl());
        changeServerLink.setOnClickListener(v -> showChangeServerDialog());
    }

    private void showChangeServerDialog(){
        if(dialog == null){
            dialog = new ChangeServerDialog(this, serverUrlTextView::setText);
        }
        dialog.show();
    }

    protected abstract T getInput();

    @Override
    public void showProgress() {
        if(Build.VERSION.SDK_INT<=25) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(progressMessage);
            }
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if(Build.VERSION.SDK_INT<=25) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    protected void showSoftKeyboard(View view){
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null)
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void hideKeyboard() {
        if(getCurrentFocus()!=null) {
            getCurrentFocus().clearFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(inputMethodManager!=null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void displayErrorMessage(String message) {
        snackbar = Snackbar.make(loginButton.getRootView(),message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", v-> {
            snackbar.dismiss();
            loginButton.performClick();
        });
        snackbar.show();
    }

    @Override
    public void onSuccessResult(ApiKey apiKey) {
        setResult(RESULT_OK, new Intent().putExtra("token", apiKey.getValue()));
        finish();
    }

    @Override
    public void clearErrorMessages() {
        if(snackbar!=null && snackbar.isShown())
            snackbar.dismiss();
    }
}
