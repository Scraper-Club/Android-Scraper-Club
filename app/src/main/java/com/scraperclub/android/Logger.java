package com.scraperclub.android;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {

    private TextView logView;
    private SimpleDateFormat dateFormat;

    public Logger(TextView logView) {
        this.logView = logView;
        dateFormat = new SimpleDateFormat("[MMM dd HH:mm:ss]", Locale.getDefault());
    }

    public void addLogMessage(String message){
        String logMsg = String.format("%s %s\n",dateFormat.format(new Date()),message);
        logView.append(logMsg);
    }

}
