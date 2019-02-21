package com.scraperclub.android.views.authentication;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scraperclub.android.R;
import com.scraperclub.android.ScraperApp;

import java.net.MalformedURLException;
import java.net.URL;

public class ChangeServerDialog{

    public interface Listener {
        void onServerUpdate(String newServerUrl);
    }

    private AlertDialog dialog;
    private EditText serverUrl;

    public ChangeServerDialog(Context context, Listener listener) {
        View dialogView = View.inflate(context,R.layout.fragment_change_server,null);
        dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        serverUrl = dialogView.findViewById(R.id.change_server_url_input);
        Button clearButton = dialogView.findViewById(R.id.change_server_clear);
        Button cancelButton = dialogView.findViewById(R.id.change_server_cancel);
        Button saveButton = dialogView.findViewById(R.id.change_server_save);

        clearButton.setOnClickListener(v -> serverUrl.setText(""));
        cancelButton.setOnClickListener(v -> dismiss());

        saveButton.setOnClickListener(v -> {
            String serverUrlText = serverUrl.getText().toString();
            try {
                URL newServerUrl = new URL(serverUrlText);
                ScraperApp.getInstance().setServerUrl(newServerUrl);
                listener.onServerUpdate(serverUrlText);
                dismiss();
            } catch (MalformedURLException e) {
                Toast.makeText(context, "Please enter correct URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void show(){
        if(dialog!=null && !dialog.isShowing()){
            serverUrl.setText(ScraperApp.getInstance().getServerUrl());
            dialog.show();
        }

    }

    public void dismiss(){
        if(dialog!=null && dialog.isShowing())
            dialog.dismiss();
    }

    public void release(){
        dialog = null;
        serverUrl = null;
    }
}
