package com.zhoupeng.stockandroid.bean;

import com.alibaba.fastjson.JSON;

public class Social {
    private String symbol;
    private SocialEntry redditEntry;
    private SocialEntry twitterEntry;

    public Social() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getReddit() {
        return redditEntry.toString();
    }

    public void setReddit(String reddit) {
        this.redditEntry = JSON.parseObject(reddit, SocialEntry.class);
    }

    public String getTwitter() {
        return twitterEntry.toString();
    }

    public void setTwitter(String twitter) {
        this.twitterEntry = JSON.parseObject(twitter, SocialEntry.class);
    }

    public Integer getRedditMention() {
        return this.redditEntry.getMention();
    }

    public Integer getRedditPositiveMention() {
        return this.redditEntry.getPositiveMention();
    }

    public Integer getRedditNegativeMention() {
        return this.redditEntry.getNegativeMention();
    }

    public Integer getTwitterMension() {
        return this.twitterEntry.getMention();
    }

    public Integer getTwitterPositiveMention() {
        return this.twitterEntry.getPositiveMention();
    }

    public Integer getTwitterNegativeMention() {
        return this.twitterEntry.getNegativeMention();
    }

    @Override
    public String toString() {
        return "Social{" +
                "symbol='" + symbol + '\'' +
                ", redditMention=" + getRedditMention() +
                ", redditPositiveMention=" + getRedditPositiveMention() +
                ", redditNegativeMention=" + getRedditNegativeMention() +
                ", twitterMension=" + getTwitterMension() +
                ", twitterPositiveMention=" + getTwitterPositiveMention() +
                ", twitterNegativeMention=" + getTwitterNegativeMention() +
                '}';
    }
}

