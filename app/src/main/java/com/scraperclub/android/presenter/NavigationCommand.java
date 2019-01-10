package com.scraperclub.android.presenter;

import android.content.Intent;

public abstract class NavigationCommand {
    public interface Listener{
        void onResult(int result, Intent data);
    }

    private Listener listener;
    public void setListener(Listener listener){
        this.listener = listener;
    }
    public void onResult(int result, Intent data){
        if(listener!=null)
            listener.onResult(result, data);
    }

    public abstract void navigate();
}
