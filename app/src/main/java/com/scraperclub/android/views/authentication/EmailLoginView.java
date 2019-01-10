package com.scraperclub.android.views.authentication;

public interface EmailLoginView extends LoginView{
    void emailInvalid(String reason);
    void passwordInvalid(String reason);
}