package com.scraperclub.android.scraping.experimental;

public class StateMachine{
    public interface OnStateChangedListener{
        void onStateChanged(ScrapingProcessor.State newState);
    }
    private ScrapingProcessor.State currentState = ScrapingProcessor.State.IDLE;
    private OnStateChangedListener listener;
    private boolean isScraping;
    private boolean isUploading;

    public ScrapingProcessor.State getCurrentState(){
        if(isScraping) return ScrapingProcessor.State.SCRAPING;
        if(isUploading) return ScrapingProcessor.State.HANDLING_RESULT;

        return ScrapingProcessor.State.IDLE;
    }

    public void setListener(OnStateChangedListener listener) {
        this.listener = listener;
    }

    public void setScraping(boolean scraping) {
        isScraping = scraping;
        updateState();
    }

    public void setUploading(boolean uploading) {
        isUploading = uploading;
        updateState();
    }

    private void updateState(){
        ScrapingProcessor.State lastState = currentState;
        currentState = getCurrentState();
        if(!lastState.equals(currentState) && listener!=null)
            listener.onStateChanged(currentState);
    }
}
