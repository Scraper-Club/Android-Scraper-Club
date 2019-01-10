package com.scraperclub.android.presenter;
import com.scraperclub.android.api.model.DeviceStatistic;

public interface MainPresenter {

    interface View {
        enum NavigationAction {
            LOGOUT, SCRAP_PUBLIC, SCRAP_PRIVATE
        }

        enum Mode{
            PRIVATE, PUBLIC
        }

        void displayDeviceStats(DeviceStatistic deviceStatistic);
        void displayServerStatus(boolean available);
        void displayApiKey(String apiKey);
        void appendLogMessage(String message);

        void disableScraping();

        void changeMode(Mode newMode);

        void navigate(NavigationCommand command);
        NavigationCommand getCommand(NavigationAction action);
    }

    void startUpdate();
    void stopUpdate();

    void privatePoolSwitchChanged(boolean state);
    void publicPoolSwitchChanged(boolean state);
    void logout();

    void destroy();
}
