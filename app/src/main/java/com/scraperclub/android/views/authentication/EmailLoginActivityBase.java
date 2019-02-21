package com.scraperclub.android.views.authentication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.scraperclub.android.R;
import com.scraperclub.android.api.model.UserCredentials;


abstract class EmailLoginActivityBase extends LoginActivityBase<UserCredentials> {

    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected TextView signupLink;
    protected TextView apiKeyLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.input_email);
        passwordEditText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.button_login);
        signupLink = findViewById(R.id.link_signup);
        apiKeyLink = findViewById(R.id.link_apikey_login);

        serverUrlTextView = findViewById(R.id.server_url);
        changeServerLink = findViewById(R.id.change_server);
        progressMessage = "Logging in...";
    }
}
