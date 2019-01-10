package com.scraperclub.android.api;

import com.scraperclub.android.api.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class ScraperAPIUrls {

    private static String SERVER_URL = BuildConfig.SERVER_URL;
    private static String API_URL = SERVER_URL + "api/";

    public static String SIGNUP_URL = SERVER_URL + "signup/";

    public static URL login(){
        return fromString(API_URL + "auth/login/");
    }

    public static URL upload(int urlId){
        return fromString(API_URL + String.format(Locale.US,"scrap/%d/upload/",urlId));
    }


    public static URL verifyApiKey(){
        return fromString(API_URL + "auth/check/");
    }

    public static URL deviceStatistics(){
        return fromString(API_URL + "device/stats/");
    }

    public static URL checkDevice(){
        return fromString(API_URL + "device/check/");
    }

    public static URL registerDevice(){
        return fromString(API_URL + "device/register/");
    }

    public static URL getNextUrl(){
        return fromString(API_URL + "geturl/");
    }

    public static URL getNextUrl(String pool){
        return fromString(API_URL + "v1/geturl/?pool="+pool);
    }


    private static URL fromString(String urlString){
        try {
            return new URL(urlString);
        }catch (MalformedURLException e){
            throw new RuntimeException(e);
        }
    }
}
