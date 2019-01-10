package com.scraperclub.android.api;

import com.scraperclub.android.api.errors.DeviceBlockedException;
import com.scraperclub.android.api.errors.DeviceNotRegisteredException;
import com.scraperclub.android.api.errors.NoAvailableUrlsExceptions;
import com.scraperclub.android.api.errors.ReachedIPLimitException;
import com.scraperclub.android.api.errors.ScraperAPIException;

import org.json.JSONObject;

import io.reactivex.functions.Consumer;

public class ScraperServerResponseParser implements Consumer<JSONObject> {
    @Override
    public void accept(JSONObject jsonObject) throws Exception {
        boolean success = jsonObject.getBoolean("ok");
        if(success) return;

        String detail = jsonObject.getString("detail");
        if(jsonObject.has("code")){
            switch (jsonObject.getInt("code")){
                case 0:
                    throw new DeviceNotRegisteredException(detail);
                case 1:
                    throw new ScraperAPIException(detail);
                case 2:
                    throw new ReachedIPLimitException(detail);
                case 3:
                    throw new DeviceBlockedException(detail);
                case 4:
                    throw new NoAvailableUrlsExceptions(detail);
            }
        }else{
            throw new ScraperAPIException(detail);
        }

    }
}
