package com.scraperclub.android;

import android.support.design.widget.Snackbar;
import android.view.View;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

public class NetworkExceptionsHandler {

    private Snackbar snackbar;
    public NetworkExceptionsHandler(View rootView, View.OnClickListener retryAction){
        snackbar = Snackbar.make(rootView,"",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", v -> {
            snackbar.dismiss();
            retryAction.onClick(v);
        });
    }

    public boolean handle(IOException e){
        if(e instanceof ConnectException){
            snackbar.setText("Failed to connect server");
            snackbar.show();
        }else if(e instanceof SocketException){
            snackbar.setText("Network connection is unavailable");
            snackbar.show();
        }else{
            return false;
        }
        return true;
    }

}
