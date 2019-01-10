package com.scraperclub.android.api.model;

import org.json.JSONObject;

import io.reactivex.functions.Function;

public class DeviceStatistic {

    private int totalScrapes;
    private int availableUrls;
    private int lastHourScrapes;
    private String currentIP;

    private boolean allowPrivate;
    private int privatePool;

    private static JsonMapper jsonMapper;

    public DeviceStatistic() { }

    public DeviceStatistic(int availableUrls, int totalScrapes, int lastHourScrapes, String currentIp) {
        this.totalScrapes = totalScrapes;
        this.availableUrls = availableUrls;
        this.lastHourScrapes = lastHourScrapes;
        this.currentIP = currentIp;
    }

    public DeviceStatistic currentIP(String ip) {
        this.currentIP = ip;
        return this;
    }
    public DeviceStatistic totalScrapes(int totalScrapes) {
        this.totalScrapes = totalScrapes;
        return this;
    }
    public DeviceStatistic scrapsForLastHour(int lastHourScrapes) {
        this.lastHourScrapes = lastHourScrapes;
        return this;
    }
    public DeviceStatistic availableUrls(int availableUrls) {
        this.availableUrls = availableUrls;
        return this;
    }
    public DeviceStatistic allowPrivate(boolean allowPrivate) {
        this.allowPrivate = allowPrivate;
        return this;
    }
    public DeviceStatistic privatePool(int privatePool) {
        this.privatePool = privatePool;
        return this;
    }

    public int getTotalScrapes() {
        return totalScrapes;
    }
    public int getAvailableUrls() {
        return availableUrls;
    }
    public int getLastHourScrapes() {
        return lastHourScrapes;
    }
    public String getCurrentIP() {
        return currentIP;
    }
    public boolean isAllowPrivate() {
        return allowPrivate;
    }
    public int getPrivatePool() {
        return privatePool;
    }

    public static JsonMapper getJsonMapper(){
        if(jsonMapper==null)jsonMapper = new JsonMapper();
        return jsonMapper;
    }

    private static class JsonMapper implements Function<JSONObject,DeviceStatistic> {

        @Override
        public DeviceStatistic apply(JSONObject jsonObject) throws Exception {
            JSONObject stats = jsonObject.getJSONObject("stats");
            return new DeviceStatistic()
                    .currentIP(stats.getString("ip"))
                    .availableUrls(stats.getInt("available"))
                    .scrapsForLastHour(stats.getInt("last_hour_scrapes"))
                    .totalScrapes(stats.getInt("total_scrapes"))
                    .allowPrivate(stats.getBoolean("allow_private"))
                    .privatePool(stats.getInt("private_pool"));
        }
    }

}
