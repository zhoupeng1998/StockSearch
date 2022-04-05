package com.zhoupeng.stockandroid.data;

import com.alibaba.fastjson.JSON;
import com.zhoupeng.stockandroid.bean.Profile;
import com.zhoupeng.stockandroid.bean.Candle;
import com.zhoupeng.stockandroid.bean.EarningsEntry;
import com.zhoupeng.stockandroid.bean.Latest;
import com.zhoupeng.stockandroid.bean.NewsEntry;
import com.zhoupeng.stockandroid.bean.RecommendationEntry;
import com.zhoupeng.stockandroid.bean.Social;

import java.util.List;
import java.util.Observable;

public class DataService extends Observable {

    private boolean invalidFlag;
    private boolean marketOpenFlag;

    private boolean profileLoaded;
    private boolean latestLoaded;
    private boolean newsLoaded;
    private boolean recommendationLoaded;
    private boolean socialLoaded;
    private boolean peersLoaded;
    private boolean earningsLoaded;
    private boolean historyCandleLoaded;
    private boolean summaryCandleLoaded;

    private Profile profileData;
    private Latest latestData;
    private List<NewsEntry> newsListData;
    private List<RecommendationEntry> recommendationListData;
    private Social socialData;
    private List<String> peersListData;
    private List<EarningsEntry> earningsListData;
    private Candle historyCandleData;
    private Candle summaryCandleData;

    private String summaryCandleDataString;
    private String historyCandleDataString;

    private static class DataServiceSingletonInstance {
        private static final DataService instance = new DataService();
    }

    private DataService() {
        resetDataService();
    }

    public static DataService getInstance() {
        return DataServiceSingletonInstance.instance;
    }

    public void resetDataService() {
        invalidFlag = false;
        marketOpenFlag = false;

        profileLoaded = false;
        latestLoaded = false;
        newsLoaded = false;
        recommendationLoaded = false;
        socialLoaded = false;
        peersLoaded = false;
        earningsLoaded = false;
        historyCandleLoaded = false;
        summaryCandleLoaded = false;
    }

    private synchronized void checkLoadComplete() {
        if (profileLoaded && latestLoaded && newsLoaded && recommendationLoaded && socialLoaded &&
                peersLoaded && earningsLoaded && historyCandleLoaded && summaryCandleLoaded) {
            setChanged();
            this.notifyObservers();
        }
    }

    private void setInvalid() {
        invalidFlag = true;
        setChanged();
        notifyObservers();
    }

    public boolean isDataInvalid() {
        return this.invalidFlag;
    }

    public boolean isMarketOpen() {
        return this.marketOpenFlag;
    }

    public void setMarketOpenFlag(boolean marketOpenFlag) {
        this.marketOpenFlag = marketOpenFlag;
    }

    public Profile getProfileData() {
        return profileData;
    }

    public Latest getLatestData() {
        return latestData;
    }

    public List<NewsEntry> getNewsListData() {
        return newsListData;
    }

    public List<RecommendationEntry> getRecommendationListData() {
        return recommendationListData;
    }

    public Social getSocialData() {
        return socialData;
    }

    public List<String> getPeersListData() {
        return peersListData;
    }

    public List<EarningsEntry> getEarningsListData() {
        return earningsListData;
    }

    public Candle getHistoryCandleData() {
        return historyCandleData;
    }

    public Candle getSummaryCandleData() {
        return summaryCandleData;
    }

    public String getHistoryCandleDataString() {
        return historyCandleDataString;
    }

    public String getSummaryCandleDataString() {
        return summaryCandleDataString;
    }

    public void setProfileData(String data) {
        profileData = JSON.parseObject(data, Profile.class);
        System.out.println(profileData);
        if (profileData.getTicker() == null) {
            setInvalid();
        } else {
            profileLoaded = true;
            checkLoadComplete();
        }
    }

    public void setLatestData(String data) {
        latestData = JSON.parseObject(data, Latest.class);
        latestLoaded = true;
        checkLoadComplete();
    }

    public void setNewsListData(String data) {
        newsListData = JSON.parseArray(data, NewsEntry.class);
        newsLoaded = true;
        checkLoadComplete();
    }

    public void setRecommendationListData(String data) {
        recommendationListData = JSON.parseArray(data, RecommendationEntry.class);
        recommendationLoaded = true;
        checkLoadComplete();
    }

    public void setSocialData(String data) {
        socialData = JSON.parseObject(data, Social.class);
        socialLoaded = true;
        checkLoadComplete();
    }

    public void setPeersListData(String data) {
        peersListData = JSON.parseArray(data, String.class);
        peersLoaded = true;
        checkLoadComplete();
    }

    public void setEarningsListData(String data) {
        earningsListData  = JSON.parseArray(data, EarningsEntry.class);
        earningsLoaded = true;
        checkLoadComplete();
    }

    public void setHistoryCandleData(String data) {
        historyCandleDataString = data;
        historyCandleData = JSON.parseObject(data, Candle.class);
        historyCandleLoaded = true;
        checkLoadComplete();
    }

    public void setSummaryCandleData(String data) {
        summaryCandleDataString = data;
        summaryCandleData = JSON.parseObject(data, Candle.class);
        summaryCandleLoaded = true;
        checkLoadComplete();
    }
}
