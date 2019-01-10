package com.scraperclub.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.scraperclub.android.api.ScraperAPI;
import com.scraperclub.android.api.ScraperHttpAPI;
import com.scraperclub.android.api.errors.ScraperAPIException;
import com.scraperclub.android.api.model.ApiKey;
import com.scraperclub.android.views.MainActivity;
import com.scraperclub.android.views.authentication.EmailLoginActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class StartupActivity extends AppCompatActivity {

    private final int DESCRIPTION_REQUEST = 2552;
    private final int AUTHENTICATION_REQUEST = 3456;
    private final int STATS_REQUEST = 1221;

    public static final int LOGOUT = 777;

    private PreferencesManager preferencesManager;
    private ScraperCore scraperCore;

    private TextView statusTextView, errorTextView;
    private ImageView logoImageView;
    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.status_msg);
        errorTextView = findViewById(R.id.error_msg);
        logoImageView = findViewById(R.id.logo);
        retryButton = findViewById(R.id.retry_button);

        ScraperAPI api = new ScraperHttpAPI();
        api.setDeviceId(Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID));

        preferencesManager = new PreferencesManager(this.getApplicationContext());
        scraperCore = ScraperCore.create(api,preferencesManager);


        retryButton.setOnClickListener(v->{
            hideErrorMessage();
            verifyToken();
        });

        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra("api_key")){
            preferencesManager.clearPrefs();
            preferencesManager.setToken(intent.getStringExtra("api_key"));
        }
        verifyToken();
    }

    private void startAuthActivity(){
        startActivityForResult(new Intent(this,EmailLoginActivity.class),AUTHENTICATION_REQUEST);
    }

    private void startStatsActivity(boolean firstTime){
        if(preferencesManager.isFirstLaunch()){
            startActivityForResult(new Intent(this, DescriptionActivity.class),DESCRIPTION_REQUEST);
        }else{
            Intent intent = new Intent(this,MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    .putExtra(MainActivity.FIRST_TIME_KEY,firstTime);
            startActivityForResult(intent, STATS_REQUEST);
        }
    }

    private void displayErrorMessage(String message){
        logoImageView.setVisibility(View.GONE);
        statusTextView.setVisibility(View.GONE);
        statusTextView.setText("");
        retryButton.setVisibility(View.VISIBLE);
        errorTextView.setText(message);

    }

    private void hideErrorMessage(){
        logoImageView.setVisibility(View.VISIBLE);
        statusTextView.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.GONE);
        errorTextView.setText("");

    }

    private void displayStatus(String status){
        statusTextView.setText(status);
    }

    private void clearPreferences(){
        scraperCore.clearPrefs();
    }

    @SuppressLint("CheckResult")
    private void verifyToken(){
        if(preferencesManager.isTokenAvailable()) {
            scraperCore.getApi()
                    .verifyApiKey(scraperCore.getToken())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(__->displayStatus("Verifying api key..."))
                    .doAfterTerminate(()->displayStatus(""))
                    .subscribe(
                            ()->{
                                scraperCore.getApi().setApiKey(scraperCore.getToken());
                                registerDevice();
                            },
                            err -> {
                                if(err instanceof ScraperAPIException){
                                    startAuthActivity();
                                }else {
                                    displayErrorMessage("Failed to verify api key:" + err.getMessage());
                                }
                            }
                    );
        }else{
            startAuthActivity();
        }
    }

    @SuppressLint("CheckResult")
    private void registerDevice() {
        scraperCore.getApi()
                .registerDevice()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__->displayStatus("Registering this device..."))
                .doOnTerminate(()->displayStatus(""))
                .subscribe(
                        ()->{
                            startStatsActivity(false);
                        },
                        err->{
                            displayErrorMessage("Failed to register device:" + err.getMessage());
                        }
                );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == AUTHENTICATION_REQUEST){
            if(resultCode == RESULT_OK){
                String token = data.getStringExtra("token");
                scraperCore.setApiKey(new ApiKey(token));
                scraperCore.getApi().setApiKey(scraperCore.getToken());
                registerDevice();
            } else {
                finish();
            }
        }else if(requestCode == STATS_REQUEST){
            if(resultCode == LOGOUT){
                clearPreferences();
                startAuthActivity();
            }else
                finish();
        }else if(requestCode == DESCRIPTION_REQUEST){
            if(resultCode == RESULT_OK){
                preferencesManager.dontShowDescription();
                startStatsActivity(true);
            }else{
                finish();
            }
        }
    }
}
