package com.scraperclub.android.views.authentication;

import android.os.Bundle;
import android.widget.EditText;

import com.scraperclub.android.R;
import com.scraperclub.android.api.model.ApiKey;

abstract class ApiKeyLoginActivityBase extends LoginActivityBase<ApiKey> {

    protected EditText apiKeyInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_key_login);
        loginButton = findViewById(R.id.button_login);
        apiKeyInput = findViewById(R.id.input_api_key);

        serverUrlTextView = findViewById(R.id.server_url);
        changeServerLink = findViewById(R.id.change_server);
        progressMessage = "Verifying key...";
    }
}
