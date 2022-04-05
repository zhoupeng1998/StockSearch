package com.zhoupeng.stockandroid.jsinterface;

import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.zhoupeng.stockandroid.bean.EarningsEntry;
import com.zhoupeng.stockandroid.bean.RecommendationEntry;
import com.zhoupeng.stockandroid.bean.Social;

import java.util.ArrayList;
import java.util.List;

public class InsightsInterface {

    private String ticker;
    private String companyName;

    private Social social;
    private List<RecommendationEntry> recommendationEntries;
    private List<EarningsEntry> earningsEntries;

    private List<String> recommendationPeriods;
    private List<Integer> recommendationStrongBuy;
    private List<Integer> recommendationBuy;
    private List<Integer> recommendationHold;
    private List<Integer> recommendationSell;
    private List<Integer> recommendationStrongSell;

    private List<String> earningsPeriods;
    private List<Double> earningsActual;
    private List<Double> earningsEstimate;

    public InsightsInterface(String ticker, String companyName, Social social,
                             List<RecommendationEntry> recommendationEntries,
                             List<EarningsEntry> earningsEntries) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.social = social;
        this.recommendationEntries = recommendationEntries;
        this.earningsEntries = earningsEntries;

        this.recommendationPeriods = new ArrayList<>();
        this.recommendationStrongBuy = new ArrayList<>();
        this.recommendationBuy = new ArrayList<>();
        this.recommendationHold = new ArrayList<>();
        this.recommendationSell = new ArrayList<>();
        this.recommendationStrongSell = new ArrayList<>();

        this.earningsPeriods = new ArrayList<>();
        this.earningsActual = new ArrayList<>();
        this.earningsEstimate = new ArrayList<>();

        for (int i = 0; i < recommendationEntries.size(); i++) {
            RecommendationEntry entry = this.recommendationEntries.get(i);
            this.recommendationPeriods.add(entry.getPeriod());
            this.recommendationStrongBuy.add(entry.getStrongBuy());
            this.recommendationBuy.add(entry.getBuy());
            this.recommendationHold.add(entry.getHold());
            this.recommendationSell.add(entry.getSell());
            this.recommendationStrongSell.add(entry.getStrongSell());
        }

        for (int i = 0; i < earningsEntries.size(); i++) {
            EarningsEntry entry = this.earningsEntries.get(i);
            this.earningsPeriods.add(entry.getPeriod());
            this.earningsActual.add(entry.getActual());
            this.earningsEstimate.add(entry.getEstimate());
        }
    }

    @JavascriptInterface
    public String getTicker() {
        return ticker;
    }

    @JavascriptInterface
    public String getCompanyName() {
        return companyName;
    }

    @JavascriptInterface
    public String getRedditMention() {
        return this.social.getRedditMention().toString();
    }

    @JavascriptInterface
    public String getRedditPositiveMention() {
        return this.social.getRedditPositiveMention().toString();
    }

    @JavascriptInterface
    public String getRedditNegativeMention() {
        return this.social.getRedditNegativeMention().toString();
    }

    @JavascriptInterface
    public String getTwitterMension() {
        return this.social.getTwitterMension().toString();
    }

    @JavascriptInterface
    public String getTwitterPositiveMention() {
        return this.social.getTwitterPositiveMention().toString();
    }

    @JavascriptInterface
    public String getTwitterNegativeMention() {
        return this.social.getTwitterNegativeMention().toString();
    }

    @JavascriptInterface
    public String getRecommendationPeriods() {
        return JSON.toJSONString(recommendationPeriods);
    }

    @JavascriptInterface
    public String getRecommendationStrongBuy() {
        return JSON.toJSONString(recommendationStrongBuy);
    }

    @JavascriptInterface
    public String getRecommendationBuy() {
        return JSON.toJSONString(recommendationBuy);
    }

    @JavascriptInterface
    public String getRecommendationHold() {
        return JSON.toJSONString(recommendationHold);
    }

    @JavascriptInterface
    public String getRecommendationSell() {
        return JSON.toJSONString(recommendationSell);
    }

    @JavascriptInterface
    public String getRecommendationStrongSell() {
        return JSON.toJSONString(recommendationStrongSell);
    }

    @JavascriptInterface
    public String getEarningsPeriods() {
        return JSON.toJSONString(earningsPeriods);
    }

    @JavascriptInterface
    public String getEarningsActual() {
        return JSON.toJSONString(earningsActual);
    }

    @JavascriptInterface
    public String getEarningsEstimate() {
        return JSON.toJSONString(earningsEstimate);
    }
}
